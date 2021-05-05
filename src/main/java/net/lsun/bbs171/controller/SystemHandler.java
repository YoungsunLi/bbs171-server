package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Category;
import net.lsun.bbs171.entity.SystemInfoDTO;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.SystemRepository;
import net.lsun.bbs171.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
        JSONObject json = new JSONObject();

        SystemInfoDTO systemInfo = systemRepository.getInfo();

        json.put("success", true);
        json.put("data", systemInfo);

        return json;
    }

    /**
     * 获取管理员列表
     *
     * @return users
     */
    @GetMapping("get_admin_users")
    public JSONObject getAdminUsers() {
        JSONObject json = new JSONObject();

        if (auth()) {
            List<User> users = systemRepository.getAdminUsers();

            json.put("success", true);
            json.put("data", users);
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 添加管理员
     *
     * @return msg
     */
    @PostMapping("add_admin_user")
    public JSONObject addAdminUser(@RequestBody User user) {
        JSONObject json = new JSONObject();

        if (auth()) {
            User dbUser = userRepository.findByPhone(user.getPhone());

            if (dbUser != null) {
                if (dbUser.getRole() != 1) {
                    json.put("success", false);
                    json.put("msg", "用户 " + dbUser.getUsername() + " 已经是管理员!");
                } else {
                    systemRepository.updateUserRoleToAdmin(user);

                    json.put("success", true);
                    json.put("msg", "添加成功!");
                }
            } else {
                json.put("success", false);
                json.put("msg", "手机号为 " + user.getPhone() + " 的用户不存在!");
            }
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }


    /**
     * 删除管理员
     *
     * @return msg
     */
    @PostMapping("del_admin_user")
    public JSONObject delAdminUser(@RequestBody User user) {
        JSONObject json = new JSONObject();

        if (auth()) {
            User dbUser = userRepository.findByPhone(user.getPhone());

            if (dbUser != null) {
                if (dbUser.getRole() == 0) {
                    systemRepository.updateUserRoleToUser(user);

                    json.put("success", true);
                    json.put("msg", "删除成功!");
                } else {
                    json.put("success", false);
                    json.put("msg", "用户 " + dbUser.getUsername() + " 不是普通管理员!");
                }
            } else {
                json.put("success", false);
                json.put("msg", "手机号为 " + user.getPhone() + " 的用户不存在!");
            }
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 添加板块(分类)
     *
     * @return users
     */
    @PostMapping("add_category")
    public JSONObject addCategory(@RequestBody Category category) {
        JSONObject json = new JSONObject();

        if (auth()) {
            systemRepository.addCategory(category);

            json.put("success", true);
            json.put("msg", "添加成功!");
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 删除板块(分类)
     *
     * @return users
     */
    @PostMapping("del_category")
    public JSONObject delCategory(@RequestBody Category category) {
        JSONObject json = new JSONObject();

        if (auth()) {
            int count = systemRepository.getCategoryUseCount(category);

            if (count > 0) {
                json.put("success", false);
                json.put("msg", "删除失败: 该板块已关联 " + count + " 个帖子!");
            } else {
                systemRepository.delCategory(category);

                json.put("success", true);
                json.put("msg", "删除成功!");
            }
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 修改板块(分类)
     *
     * @return users
     */
    @PostMapping("update_category")
    public JSONObject updateCategory(@RequestBody Category category) {
        JSONObject json = new JSONObject();

        if (auth()) {
            systemRepository.updateCategory(category);

            json.put("success", true);
            json.put("msg", "修改成功!");
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    private boolean auth() {
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        return dbUser.getRole() == 100;
    }

}
