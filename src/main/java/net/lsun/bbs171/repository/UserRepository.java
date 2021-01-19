package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    void save(User user);

    void update(User user);

    void deleteById(Long id);

    User login(String phone, String password);
}
