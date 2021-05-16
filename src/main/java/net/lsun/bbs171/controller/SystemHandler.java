package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Category;
import net.lsun.bbs171.entity.Notice;
import net.lsun.bbs171.entity.SystemInfoDTO;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.SystemRepository;
import net.lsun.bbs171.repository.UserRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("system")
public class SystemHandler {

    @Resource
    private SystemRepository systemRepository;

    @Resource
    private UserRepository userRepository;

    /**
     * 获取社区运行状况
     *
     * @return system info
     */
    @GetMapping("get_info")
    public JSONObject getInfo() {
        JSONObject res = new JSONObject();

        SystemInfoDTO systemInfo = systemRepository.getInfo();

        res.put("success", true);
        res.put("data", systemInfo);

        return res;
    }

    /**
     * 获取管理员列表
     *
     * @return users
     */
    @GetMapping("get_admin_users")
    public JSONObject getAdminUsers() {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            List<User> users = systemRepository.getAdminUsers();

            res.put("success", true);
            res.put("data", users);
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 添加管理员
     *
     * @return msg
     */
    @PostMapping("add_admin_user")
    public JSONObject addAdminUser(@RequestBody User user) {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            User dbUser = userRepository.findByPhone(user.getPhone());

            if (dbUser != null) {
                if (dbUser.getRole() != 1) {
                    res.put("success", false);
                    res.put("msg", "用户 " + dbUser.getUsername() + " 已经是管理员!");
                } else {
                    systemRepository.updateUserRoleToAdmin(user);

                    res.put("success", true);
                    res.put("msg", "添加成功!");
                }
            } else {
                res.put("success", false);
                res.put("msg", "手机号为 " + user.getPhone() + " 的用户不存在!");
            }
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }


    /**
     * 删除管理员
     *
     * @return msg
     */
    @PostMapping("del_admin_user")
    public JSONObject delAdminUser(@RequestBody User user) {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            User dbUser = userRepository.findByPhone(user.getPhone());

            if (dbUser != null) {
                if (dbUser.getRole() == 0) {
                    systemRepository.updateUserRoleToUser(user);

                    res.put("success", true);
                    res.put("msg", "删除成功!");
                } else {
                    res.put("success", false);
                    res.put("msg", "用户 " + dbUser.getUsername() + " 不是普通管理员!");
                }
            } else {
                res.put("success", false);
                res.put("msg", "手机号为 " + user.getPhone() + " 的用户不存在!");
            }
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 添加板块(分类)
     *
     * @return users
     */
    @PostMapping("add_category")
    public JSONObject addCategory(@RequestBody Category category) {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            systemRepository.addCategory(category);

            res.put("success", true);
            res.put("msg", "添加成功!");
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 删除板块(分类)
     *
     * @return users
     */
    @PostMapping("del_category")
    public JSONObject delCategory(@RequestBody Category category) {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            int count = systemRepository.getCategoryUseCount(category);

            if (count > 0) {
                res.put("success", false);
                res.put("msg", "删除失败: 该板块已关联 " + count + " 个帖子!");
            } else {
                systemRepository.delCategory(category);

                res.put("success", true);
                res.put("msg", "删除成功!");
            }
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 修改板块(分类)
     *
     * @return users
     */
    @PostMapping("update_category")
    public JSONObject updateCategory(@RequestBody Category category) {
        JSONObject res = new JSONObject();

        if (auth(100)) {
            systemRepository.updateCategory(category);

            res.put("success", true);
            res.put("msg", "修改成功!");
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }


    /**
     * 获取普通用户列表
     *
     * @return users
     */
    @GetMapping("get_users")
    public JSONObject getUsers(@Param("sort") int sort, @Param("status") int status, @Param("keywords") String keywords) {
        JSONObject res = new JSONObject();

        if (auth(0)) {
            String sortStr;
            switch (sort) {
                case 1:
                    sortStr = "lastTime";
                    break;
                case 2:
                    sortStr = "experience";
                    break;
                default:
                    sortStr = "datetime";
            }

            List<User> users = systemRepository.getUsers(sortStr, status, keywords);

            res.put("success", true);
            res.put("data", users);
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 重置用户默认密码
     *
     * @return users
     */
    @GetMapping("reset_password")
    public JSONObject resetPassword(@Param("id") int id) {
        JSONObject res = new JSONObject();
        if (auth(0)) {
            User dbUser = userRepository.findById(id);

            if (dbUser.getRole() != 1) {
                res.put("success", false);
                res.put("msg", "非法操作!");
            } else {
                String password = "123456";
                systemRepository.resetPassword(id, new BCryptPasswordEncoder().encode(password));

                res.put("success", true);
                res.put("msg", dbUser.getUsername() + " 的密码已重置为: " + password);
            }
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 更新用户状态
     *
     * @return users
     */
    @GetMapping("update_user_status")
    public JSONObject updateUserStatus(@Param("id") int id, @Param("status") int status) {
        JSONObject res = new JSONObject();
        if (auth(0)) {
            User dbUser = userRepository.findById(id);

            if (dbUser.getRole() != 1) {
                res.put("success", false);
                res.put("msg", "非法操作!");
            } else {
                systemRepository.updateUserStatus(id, status);

                res.put("success", true);
                res.put("msg", "操作成功!");
            }
        } else {
            res.put("success", false);
            res.put("msg", "非法操作!");
        }

        return res;
    }

    /**
     * 获取公告列表
     *
     * @return users
     */
    @GetMapping("get_notices")
    public JSONObject getNotices(@Param("status") int status, @Param("keywords") String keywords) {
        JSONObject json = new JSONObject();

        if (auth(0)) {
            List<Notice> notices = systemRepository.getNotices(status, keywords);

            json.put("success", true);
            json.put("data", notices);
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 更新公告状态
     *
     * @return users
     */
    @GetMapping("update_notice_status")
    public JSONObject updateNoticeStatus(@Param("id") int id, @Param("status") int status) {
        JSONObject json = new JSONObject();
        if (auth(0)) {
            systemRepository.updateNoticeStatus(id, status);

            json.put("success", true);
            json.put("msg", "操作成功!");
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    private boolean auth(int role) {
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        return dbUser.getRole() == role;
    }

}
