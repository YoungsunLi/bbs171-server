package net.lsun.bbs171.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import net.lsun.bbs171.utils.JWTUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String url = request.getRequestURI();

        // 跳过不需要验证的路径
        if (Arrays.asList(SecurityConfiguration.AUTH_WHITELIST).contains(url)) {
            chain.doFilter(request, response);
            return;
        }

        // 跳过验不验证都行的路径
        if (Arrays.asList(SecurityConfiguration.AUTH_ANYWAY).contains(url)) {
            try {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ignored) {
            }
            chain.doFilter(request, response);
            return;
        }

        JSONObject res = new JSONObject();
        String header = request.getHeader(JWTUtil.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith(JWTUtil.TOKEN_PREFIX)) {
            res.put("success", false);
            res.put("msg", "请登录后操作!");
            response.getWriter().write(JSON.toJSONString(res));
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            res.put("success", false);
            res.put("msg", "请重新登录!");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("Token已过期: " + e);
        } catch (UnsupportedJwtException e) {
            res.put("success", false);
            res.put("msg", "Token格式错误");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("Token格式错误: " + e);
        } catch (MalformedJwtException e) {
            res.put("success", false);
            res.put("msg", "Token没有被正确构造");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("Token没有被正确构造: " + e);
        } catch (SignatureException e) {
            res.put("success", false);
            res.put("msg", "Token签名失败");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("签名失败: " + e);
        } catch (IllegalArgumentException e) {
            res.put("success", false);
            res.put("msg", "Token非法参数异常");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("非法参数异常: " + e);
        } catch (Exception e) {
            res.put("success", false);
            res.put("msg", "Invalid Token");
            response.getWriter().write(JSON.toJSONString(res));
            logger.error("Invalid Token " + e.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWTUtil.AUTHORIZATION);
        if (token != null) {
            // 校验 token
            String id = JWTUtil.validateToken(token);
            if (StringUtils.isNotBlank(id)) {
                return new UsernamePasswordAuthenticationToken(id, null, new ArrayList<>());
            }

            return null;
        }
        return null;
    }

}