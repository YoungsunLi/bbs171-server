package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Category;
import net.lsun.bbs171.entity.Notice;
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

    List<User> getUsers(String sort, int status, String keywords);

    void resetPassword(int id, String password);

    void updateUserStatus(int id, int status);

    List<Notice> getNotices(int status, String keywords);

    void updateNoticeStatus(int id, int status);
}
