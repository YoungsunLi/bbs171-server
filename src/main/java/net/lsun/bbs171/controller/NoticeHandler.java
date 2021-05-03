package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Notice;
import net.lsun.bbs171.repository.NoticeRepository;
import net.lsun.bbs171.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notice")
public class NoticeHandler {

    @Resource
    private NoticeRepository noticeRepository;

    @Resource
    private UserRepository userRepository;

    /**
     * 发布公告
     *
     * @param notice 公告
     * @return msg
     */
    @PostMapping("/submit")
    public JSONObject submit(@RequestBody Notice notice) {
        JSONObject json = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        int role = userRepository.findById(authId).getRole();
        if (role != 0) {
            // 非管理员
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            notice.setUser_id(authId);
            noticeRepository.submit(notice);

            json.put("success", true);
            json.put("msg", "发布成功!");
        }

        return json;
    }

    /**
     * 获取最新的一条公告
     *
     * @return notice
     */
    @GetMapping("/get_latest")
    public JSONObject getLatest() {
        JSONObject json = new JSONObject();

        Notice notice = noticeRepository.getLatest();

        json.put("success", true);
        json.put("data", notice);

        return json;
    }
}