package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.StarDTO;
import net.lsun.bbs171.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User findByPhone(String phone);

    void save(User user);

    void update(User user);

    void resetPassword(User user);

    void deleteById(Long id);

    void updatePassword(String phone, String password);

    void starPost(StarDTO starDTO);

    void unstarPost(Long id);
}
