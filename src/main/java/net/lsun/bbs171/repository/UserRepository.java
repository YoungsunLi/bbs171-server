package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Star;
import net.lsun.bbs171.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(int id);

    User findByPhone(String phone);

    void save(User user);

    void update(User user);

    void resetPassword(User user);

    void deleteById(int id);

    void updatePassword(int id, String password);

    void starPost(Star star);

    void unstarPost(Star star);

    List<Star> getStars(int id);

    void updateLastTime(int id);
}
