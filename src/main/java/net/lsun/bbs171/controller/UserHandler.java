package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.AliyunUtil;
import net.lsun.bbs171.utils.JWTUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class UserHandler {

    @Resource
    private UserRepository userRepository;

    @GetMapping("/findAll")
    public JSONObject findALl() {
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("data", userRepository.findAll());

        return json;
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
     * success:  true or false 执行成功或失败
     * msg:
     */
    @PostMapping("/token")
    public JSONObject token(@RequestBody User _user) {
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
                    json.put("msg", "登陆成功!");
                    json.put("token", token);
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
     * @param _user 仅需要 phone
     * @return 结果
     */
    @PostMapping("/send_code")
    public JSONObject sendCode(@RequestBody User _user) {
        String code = (Math.random() + "").substring(2, 8);

        // TODO code 要存起来做校验
        System.out.println(code);

        return AliyunUtil.sendCode(_user.getPhone(), code);
    }

}
