package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Category;
import net.lsun.bbs171.entity.Level;
import net.lsun.bbs171.entity.SystemInfoDTO;
import net.lsun.bbs171.entity.User;

import java.util.List;

public interface SystemRepository {
    SystemInfoDTO getInfo();

    List<User> getAdminUsers();

    void addCategory(Category category);

    void delCategory(Category category);

    int getCategoryUseCount(Category category);

    void updateCategory(Category category);

    void updateUserRoleToAdmin(User user);

    void updateUserRoleToUser(User user);
}
