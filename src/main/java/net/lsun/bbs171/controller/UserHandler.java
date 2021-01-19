package net.lsun.bbs171.controller;

import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
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

    @PostMapping("/login")
    public User login(@RequestBody String phone, @RequestBody String password) {
        System.out.println(phone + "---");
        return userRepository.login(phone, password);
    }

}
