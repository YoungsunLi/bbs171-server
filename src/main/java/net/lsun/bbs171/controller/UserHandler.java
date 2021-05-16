package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.*;
import net.lsun.bbs171.repository.PostRepository;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.AliyunUtil;
import net.lsun.bbs171.utils.CacheUtil;
import net.lsun.bbs171.utils.JWTUtil;
import net.lsun.bbs171.utils.Util;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserHandler {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Resource
    private UserRepository userRepository;

    @Resource
    private PostRepository postRepository;

    @GetMapping("/findAll")
    public JSONObject findALl() {
        JSONObject res = new JSONObject();
        res.put("success", true);
        res.put("data", userRepository.findAll());

        return res;
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return user & posts
     */
    @GetMapping("/get_user")
    public JSONObject getUser(@Param("id") int id) {
        JSONObject res = new JSONObject();
        User user = userRepository.findById(id);

        if (user == null) {
            res.put("success", false);
            res.put("msg", "用户不存在!");

        } else {
            user.setPassword(null);
            List<PostForUserHome> posts = postRepository.findPostsForUserHome(id);
            user.setPhone(null);

            res.put("success", true);
            res.put("user", user);
            res.put("posts", posts);
        }

        return res;
    }

    @PostMapping("/save")
    public void save(@RequestBody User user) {
        userRepository.save(user);
    }

    /**
     * 修改用户资料
     *
     * @param user 可修改 username gender sign
     * @return user
     */
    @PostMapping("/update")
    public JSONObject update(@RequestBody User user) {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        user.setId(authId);

        userRepository.update(user);
        User dbUser = userRepository.findById(authId);
        dbUser.setPassword(null);

        res.put("success", true);
        res.put("msg", "修改成功!");
        res.put("user", dbUser);

        return res;
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") int id) {
        userRepository.deleteById(id);
    }

    /**
     * 获取 token
     *
     * @param user 需要 phone password
     * @return token
     */
    @PostMapping("/token")
    public JSONObject token(@RequestBody User user) {
        JSONObject res = new JSONObject();
        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser != null) {
            if (dbUser.getStatus() != 1) {
                res.put("success", false);
                res.put("msg", "你已被限制登录!");
            } else {
                if (bCryptPasswordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                    // 创建 token
                    String token = JWTUtil.generateToken(dbUser.getId());
                    dbUser.setPassword(null);

                    res.put("success", true);
                    res.put("msg", "登陆成功!");
                    res.put("token", token);
                    res.put("user", dbUser);
                } else {
                    res.put("success", false);
                    res.put("msg", "密码错误!");
                }
            }
        } else {
            res.put("success", false);
            res.put("msg", "用户不存在!");
        }

        return res;
    }

    /**
     * 发送短信验证码
     *
     * @param user 仅需要 phone
     * @return msg
     */
    @PostMapping("/send_code")
    public JSONObject sendCode(@RequestBody User user) {
        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser != null) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("msg", "该手机号码已注册!");

            return res;
        }
        String code = AliyunUtil.generateCode();
        CacheUtil.putData(user.getPhone(), code);

        System.out.println("验证码: " + code);

        return AliyunUtil.sendCode(user.getPhone(), code);
    }

    /**
     * 发送短信验证码 (已注册用户)
     *
     * @param user 仅需要 phone
     * @return msg
     */
    @PostMapping("/send_code_exist")
    public JSONObject sendCodeExist(@RequestBody User user) {
        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser == null) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("msg", "该手机号码未注册!");

            return res;
        }
        String code = AliyunUtil.generateCode();
        CacheUtil.putData(user.getPhone(), code);

        System.out.println("验证码: " + code);

        return AliyunUtil.sendCode(user.getPhone(), code);
    }

    /**
     * 注册
     *
     * @param regUserDTO 用户注册信息
     * @return msg
     */
    @PostMapping("/reg")
    public JSONObject reg(@RequestBody RegUserDTO regUserDTO) {
        JSONObject res = new JSONObject();
        String code = CacheUtil.getData(regUserDTO.getPhone());
        if (code == null) {
            res.put("success", false);
            res.put("msg", "验证码无效!");
        } else if (!code.equals(regUserDTO.getCode())) {
            res.put("success", false);
            res.put("msg", "验证码错误!");
        } else {
            User user = new User();

            user.setPhone(regUserDTO.getPhone());
            user.setUsername(regUserDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(regUserDTO.getPassword()));
            user.setGender(regUserDTO.getGender());
            user.setAvatar(Util.getMD5(regUserDTO.getPhone()));

            userRepository.save(user);

            res.put("success", true);
            res.put("msg", "注册成功!");
        }

        return res;
    }

    /**
     * 重置密码
     *
     * @param regUserDTO 需要 phone code password
     * @return msg
     */
    @PostMapping("/reset_password")
    public JSONObject resetPassword(@RequestBody RegUserDTO regUserDTO) {
        JSONObject res = new JSONObject();
        String code = CacheUtil.getData(regUserDTO.getPhone());
        if (code == null) {
            res.put("success", false);
            res.put("msg", "验证码无效!");
        } else if (!code.equals(regUserDTO.getCode())) {
            res.put("success", false);
            res.put("msg", "验证码错误!");
        } else {
            User user = new User();

            user.setPhone(regUserDTO.getPhone());
            user.setPassword(bCryptPasswordEncoder.encode(regUserDTO.getPassword()));

            userRepository.resetPassword(user);

            res.put("success", true);
            res.put("msg", "重置成功!");
        }

        return res;
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDTO 当前密码 跟新密码
     * @return msg
     */
    @PostMapping("/update_password")
    public JSONObject updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (bCryptPasswordEncoder.matches(updatePasswordDTO.getOldPassword(), dbUser.getPassword())) {
            userRepository.updatePassword(authId, bCryptPasswordEncoder.encode(updatePasswordDTO.getNewPassword()));
            res.put("success", true);
            res.put("msg", "修改成功!");
        } else {
            res.put("success", false);
            res.put("msg", "当前密码错误!");
        }

        return res;
    }

    /**
     * 收藏帖子
     *
     * @param id     帖子ID
     * @param action 0=取消收藏 其他=收藏
     * @return msg
     */
    @GetMapping("/star_post")
    public JSONObject starPost(@Param("id") int id, @Param("action") int action) {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Star star = new Star();

        star.setUser_id(authId);
        star.setPost_id(id);
        if (action == 0) {
            userRepository.unstarPost(star);
            res.put("success", true);
            res.put("msg", "取消成功!");
        } else {
            userRepository.starPost(star);
            res.put("success", true);
            res.put("msg", "收藏成功!");
        }

        return res;
    }

    /**
     * 获取收藏帖子列表
     *
     * @return stars
     */
    @GetMapping("/get_stars")
    public JSONObject getStars() {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Star> stars = userRepository.getStars(authId);

        res.put("success", true);
        res.put("stars", stars);

        return res;
    }


    /**
     * 获取等级信息
     *
     * @return level
     */
    @GetMapping("get_level")
    public JSONObject getLevel() {
        JSONObject res = new JSONObject();

        List<Level> levels = userRepository.getLevel();

        res.put("success", true);
        res.put("data", levels);

        return res;
    }
}
