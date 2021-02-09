package net.lsun.bbs171.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURI();
        String header = request.getHeader(JWTUtil.AUTHORIZATION);

        JSONObject json = new JSONObject();
        // 跳过不需要验证的路径
        if (Arrays.asList(SecurityConfiguration.AUTH_WHITELIST).contains(url)) {
            chain.doFilter(request, response);
            return;
        }
        if (StringUtils.isBlank(header) || !header.startsWith(JWTUtil.TOKEN_PREFIX)) {

            json.put("codeCheck", false);
            json.put("msg", "Token为空");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            //json.put("status", "-2");
            json.put("codeCheck", false);
            json.put("msg", "Token已过期");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("Token已过期: " + e);
        } catch (UnsupportedJwtException e) {
            //json.put("status", "-3");
            json.put("codeCheck", false);
            json.put("msg", "Token格式错误");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("Token格式错误: " + e);
        } catch (MalformedJwtException e) {
            //json.put("status", "-4");
            json.put("codeCheck", false);
            json.put("msg", "Token没有被正确构造");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("Token没有被正确构造: " + e);
        } catch (SignatureException e) {
            //json.put("status", "-5");
            json.put("codeCheck", false);
            json.put("msg", "Token签名失败");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("签名失败: " + e);
        } catch (IllegalArgumentException e) {
            //json.put("status", "-6");
            json.put("codeCheck", false);
            json.put("msg", "Token非法参数异常");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("非法参数异常: " + e);
        } catch (Exception e) {
            //json.put("status", "-9");
            json.put("codeCheck", false);
            json.put("msg", "Invalid Token");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(json));
            logger.error("Invalid Token " + e.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWTUtil.AUTHORIZATION);
        if (token != null) {
            String phone;

            // 校验 token
            phone = JWTUtil.validateToken(token);
            if (StringUtils.isNotBlank(phone)) {
                return new UsernamePasswordAuthenticationToken(phone, null, new ArrayList<>());
            }

            return null;
        }
        return null;
    }

}