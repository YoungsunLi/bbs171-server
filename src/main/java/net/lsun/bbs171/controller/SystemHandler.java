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

        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (dbUser.getRole() != 100) {
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            List<User> users = systemRepository.getAdminUsers();

            json.put("success", true);
            json.put("data", users);
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

        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (dbUser.getRole() != 100) {
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            systemRepository.addCategory(category);

            json.put("success", true);
            json.put("msg", "添加成功!");
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

        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (dbUser.getRole() != 100) {
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            int count = systemRepository.getCategoryUseCount(category);

            if (count > 0) {
                json.put("success", false);
                json.put("msg", "删除失败: 该板块已关联 " + count + " 个帖子!");
            } else {
                systemRepository.delCategory(category);

                json.put("success", true);
                json.put("msg", "删除成功!");
            }
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

        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (dbUser.getRole() != 100) {
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            systemRepository.updateCategory(category);

            json.put("success", true);
            json.put("msg", "修改成功!");

        }

        return json;
    }

}
