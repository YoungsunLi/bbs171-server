package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.JWTUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserHandler {

    @Resource
    private UserRepository userRepository;

    @GetMapping("/findAll")
    public List<User> findALl() {
        return userRepository.findAll();
    }

    @GetMapping("/findById/{id}")
    public User findById(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody User user) {
        userRepository.save(user);
    }

    @PutMapping("/update")
    public void update(@RequestBody User user) {
        userRepository.update(user);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 获取 token
     * 输入 phone password  手机号，密码
     * 输出：code: 状态码   1 为认证成功 0 为用户不存在 -1 为密码不一致 -2 表示程序错误
     * success:  true or false 执行成功或失败
     * message:
     */
    @PostMapping("/token")
    public String token(@RequestBody User _user) {
        JSONObject json = new JSONObject();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            User user = userRepository.findByPhone(_user.getPhone());
            if (user != null) {
                String dbPassWord = user.getPassword();
                if (bCryptPasswordEncoder.matches(_user.getPassword(), dbPassWord)) {
                    // 创建 token
                    String token = JWTUtil.generateToken(_user.getPhone());
                    json.put("success", true);
                    json.put("code", 1);
                    json.put("message", "登陆成功");
                    json.put("token", token);
                } else {
                    json.put("success", false);
                    json.put("code", -1);
                    json.put("message", "密码错误");
                }
            } else {
                json.put("success", false);
                json.put("code", 0);
                json.put("message", "用户不存在");
            }
        } catch (Exception e) {
            json.put("code", -2);
            json.put("success", false);
            json.put("message", e.getMessage());

        }

        return JSON.toJSONString(json);
    }

}
