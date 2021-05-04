package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Level;
import net.lsun.bbs171.entity.SystemInfoDTO;
import net.lsun.bbs171.repository.SystemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("system")
public class SystemHandler {

    @Resource
    private SystemRepository systemRepository;

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
     * 获取等级信息
     *
     * @return level
     */
    @GetMapping("get_user_level")
    public JSONObject getUserLevel() {
        JSONObject json = new JSONObject();

        List<Level> levels = systemRepository.getUserLevel();

        json.put("success", true);
        json.put("data", levels);

        return json;
    }
}
