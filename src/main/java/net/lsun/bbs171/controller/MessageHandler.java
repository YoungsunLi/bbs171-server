package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Message;
import net.lsun.bbs171.entity.User;
import net.lsun.bbs171.repository.MessageRepository;
import net.lsun.bbs171.repository.UserRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageHandler {

    @Resource
    MessageRepository messageRepository;

    @Resource
    UserRepository userRepository;

    /**
     * 获取未读消息数
     *
     * @return count
     */
    @GetMapping("get_count")
    public JSONObject getCount() {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        int count = messageRepository.findMessageCount(authId);
        User dbUser = userRepository.findById(authId);
        res.put("success", true);

        if (dbUser.getStatus() != 1) {
            res.put("msg", "你已被限制登录!");
            res.put("illegal", true);
        } else {
            // 更新在线时间
            userRepository.updateLastTime(authId);

            res.put("count", count);
        }

        return res;
    }

    /**
     * 获取未读消息
     *
     * @return messages
     */
    @GetMapping("read")
    public JSONObject read() {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Message> messages = messageRepository.findMessagesByUserId(authId);

        res.put("success", true);
        res.put("messages", messages);

        return res;
    }

    /**
     * 删除一条消息
     *
     * @return msg
     */
    @GetMapping("remove")
    public JSONObject remove(@Param("id") int id) {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Message message = messageRepository.findMessageById(id);

        if (message == null || message.getUser_id() != authId) {
            res.put("success", false);
            res.put("msg", "消息不存在!");
        } else {
            messageRepository.remove(id);

            res.put("success", true);
            res.put("msg", "删除成功!");
        }

        return res;
    }

    /**
     * 删除全部消息
     *
     * @return msg
     */
    @GetMapping("remove_all")
    public JSONObject remove() {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        messageRepository.removeAll(authId);

        res.put("success", true);
        res.put("msg", "删除成功!");

        return res;
    }
}
