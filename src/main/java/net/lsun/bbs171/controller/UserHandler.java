package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.*;
import net.lsun.bbs171.repository.PostRepository;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.AliyunUtil;
import net.lsun.bbs171.utils.CacheUtil;
import net.lsun.bbs171.utils.JWTUtil;
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
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("data", userRepository.findAll());

        return json;
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return user
     */
    @GetMapping("/get_user")
    public JSONObject getUser(@Param("id") Long id) {
        JSONObject json = new JSONObject();
        User user = userRepository.findById(id);

        if (user == null) {
            json.put("success", false);
            json.put("msg", "用户不存在!");

        } else {
            user.setPassword(null);
            List<PostForUserHome> posts = postRepository.findPostsForUserHome(user.getPhone());
            user.setPhone(null);

            json.put("success", true);
            json.put("user", user);
            json.put("posts", posts);
        }

        return json;
    }

    @PostMapping("/save")
    public void save(@RequestBody User user) {
        userRepository.save(user);
    }

    /**
     * 修改用户资料
     *
     * @param user 可修改 username gender sign
     * @return 包括需改后的用户信息
     */
    @PostMapping("/update")
    public JSONObject update(@RequestBody User user) {
        JSONObject json = new JSONObject();

        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        user.setPhone(phone);

        userRepository.update(user);
        User dbUser = userRepository.findByPhone(phone);
        dbUser.setPassword(null);

        json.put("success", true);
        json.put("msg", "修改成功!");
        json.put("user", dbUser);

        return json;
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 获取 token
     *
     * @param user 需要 phone password
     * @return 结果
     */
    @PostMapping("/token")
    public JSONObject token(@RequestBody User user) {
        JSONObject json = new JSONObject();
        try {
            User dbUser = userRepository.findByPhone(user.getPhone());
            if (dbUser != null) {
                if (bCryptPasswordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                    // 创建 token
                    String token = JWTUtil.generateToken(user.getPhone());
                    dbUser.setPassword(null);

                    json.put("success", true);
                    json.put("msg", "登陆成功!");
                    json.put("token", token);
                    json.put("user", dbUser);
                } else {
                    json.put("success", false);
                    json.put("msg", "密码错误!");
                }
            } else {
                json.put("success", false);
                json.put("msg", "用户不存在!");
            }
        } catch (Exception e) {
            json.put("success", false);
            json.put("msg", e.getMessage());
        }

        return json;
    }

    /**
     * 发送短信验证码
     *
     * @param user 仅需要 phone
     * @return 结果
     */
    @PostMapping("/send_code")
    public JSONObject sendCode(@RequestBody User user) {
        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser != null) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("msg", "该手机号码已注册!");

            return json;
        }
        String code = AliyunUtil.generateCode();

        CacheUtil.putData(user.getPhone(), code);

        return AliyunUtil.sendCode(user.getPhone(), code);
    }

    /**
     * 发送短信验证码 (已注册用户)
     *
     * @param user 仅需要 phone
     * @return 结果
     */
    @PostMapping("/send_code_exist")
    public JSONObject sendCodeExist(@RequestBody User user) {
        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser == null) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("msg", "该手机号码未注册!");

            return json;
        }
        String code = AliyunUtil.generateCode();
        CacheUtil.putData(user.getPhone(), code);

        return AliyunUtil.sendCode(user.getPhone(), code);
    }

    /**
     * 注册
     *
     * @param regUserDTO 用户注册信息
     * @return 结果
     */
    @PostMapping("/reg")
    public JSONObject reg(@RequestBody RegUserDTO regUserDTO) {
        JSONObject json = new JSONObject();
        String code = CacheUtil.getData(regUserDTO.getPhone());
        if (code == null) {
            json.put("success", false);
            json.put("msg", "验证码无效!");
        } else if (!code.equals(regUserDTO.getCode())) {
            json.put("success", false);
            json.put("msg", "验证码错误!");
        } else {
            User user = new User();

            user.setPhone(regUserDTO.getPhone());
            user.setUsername(regUserDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(regUserDTO.getPassword()));
            user.setGender(regUserDTO.getGender());

            userRepository.save(user);

            json.put("success", true);
            json.put("msg", "注册成功!");
        }

        return json;
    }

    /**
     * 重置密码
     *
     * @param regUserDTO 需要 phone code password
     * @return json
     */
    @PostMapping("/reset_password")
    public JSONObject resetPassword(@RequestBody RegUserDTO regUserDTO) {
        JSONObject json = new JSONObject();
        String code = CacheUtil.getData(regUserDTO.getPhone());
        if (code == null) {
            json.put("success", false);
            json.put("msg", "验证码无效!");
        } else if (!code.equals(regUserDTO.getCode())) {
            json.put("success", false);
            json.put("msg", "验证码错误!");
        } else {
            User user = new User();

            user.setPhone(regUserDTO.getPhone());
            user.setPassword(bCryptPasswordEncoder.encode(regUserDTO.getPassword()));

            userRepository.resetPassword(user);

            json.put("success", true);
            json.put("msg", "重置成功!");
        }

        return json;
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDTO 当前密码 跟新密码
     * @return json
     */
    @PostMapping("/update_password")
    public JSONObject updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        System.out.println(updatePasswordDTO.getOldPassword());
        System.out.println(updatePasswordDTO.getNewPassword());
        JSONObject json = new JSONObject();
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        User dbUser = userRepository.findByPhone(phone);

        if (bCryptPasswordEncoder.matches(updatePasswordDTO.getOldPassword(), dbUser.getPassword())) {
            userRepository.updatePassword(phone, bCryptPasswordEncoder.encode(updatePasswordDTO.getNewPassword()));
            json.put("success", true);
            json.put("msg", "修改成功!");
        } else {
            json.put("success", false);
            json.put("msg", "当前密码错误!");
        }

        return json;
    }

    /**
     * 收藏帖子
     */
    @PostMapping("/star_post")
    public JSONObject starPost(@RequestBody StarDTO starDTO) {
        JSONObject json = new JSONObject();

        userRepository.starPost(starDTO);
        json.put("success", true);
        json.put("msg", "收藏成功!");

        return json;
    }

    /**
     * 取消收藏帖子
     */
    @GetMapping("/unstar_post")
    public JSONObject unstarPost(@Param("id") Long id) {
        JSONObject json = new JSONObject();

        userRepository.unstarPost(id);
        json.put("success", true);
        json.put("msg", "取消成功!");

        return json;
    }
}
