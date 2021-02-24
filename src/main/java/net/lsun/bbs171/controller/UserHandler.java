package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.RegUser;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.AliyunUtil;
import net.lsun.bbs171.utils.CacheUtil;
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
     *
     * @param user 需要 phone password
     * @return 结果
     */
    @PostMapping("/token")
    public JSONObject token(@RequestBody User user) {
        JSONObject json = new JSONObject();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            User dbUser = userRepository.findByPhone(user.getPhone());
            if (dbUser != null) {
                String dbPassWord = dbUser.getPassword();
                if (bCryptPasswordEncoder.matches(user.getPassword(), dbPassWord)) {
                    // 创建 token
                    String token = JWTUtil.generateToken(user.getPhone());

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
     *
     * @param user 仅需要 phone
     * @return 结果
     */
    @PostMapping("/send_code")
    public JSONObject sendCode(@RequestBody User user) {
        CacheUtil.clearData();

        User dbUser = userRepository.findByPhone(user.getPhone());
        if (dbUser != null) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("msg", "该手机号码已注册!");

            return json;
        }
        // 生成一个6位纯数字随机验证码
        String code = (Math.random() + "").substring(2, 8);

        CacheUtil.putData(user.getPhone(), code);

        return AliyunUtil.sendCode(user.getPhone(), code);
    }

    /**
     * 注册
     *
     * @param regUser 用户注册信息
     * @return 结果
     */
    @PostMapping("/reg")
    public JSONObject reg(@RequestBody RegUser regUser) {
        JSONObject json = new JSONObject();
        String code = CacheUtil.getData(regUser.getPhone());
        if (code == null) {
            json.put("success", false);
            json.put("msg", "验证码无效!");
        } else if (!code.equals(regUser.getCode())) {
            json.put("success", false);
            json.put("msg", "验证码错误!");
        } else {
            User user = new User();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            user.setPhone(regUser.getPhone());
            user.setUsername(regUser.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(regUser.getPassword()));
            user.setGender(regUser.getGender());

            userRepository.save(user);

            json.put("success", true);
            json.put("msg", "注册成功!");
        }

        return json;
    }

}
