package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.*;
import net.lsun.bbs171.repository.CommentRepository;
import net.lsun.bbs171.repository.MessageRepository;
import net.lsun.bbs171.repository.PostRepository;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.utils.SensitiveFilterUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentHandler {

    @Resource
    CommentRepository commentRepository;

    @Resource
    PostRepository postRepository;

    @Resource
    MessageRepository messageRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    private SensitiveFilterUtil sensitiveFilter;

    /**
     * 回复帖子
     *
     * @param comment 回复
     * @return msg
     */
    @PostMapping("/submit")
    public JSONObject submit(@RequestBody Comment comment) {
        JSONObject res = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setFrom_id(authId);

        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        commentRepository.submit(comment);
        postRepository.updateCommentCount(comment.getPost_id(), 1);

        // 添加 message 冗余数据
        PostDetail postDetail = postRepository.findPostDetail(comment.getPost_id(), 0);
        int userId = comment.getTo_id() != 0 ? comment.getTo_id() : postDetail.getUser_id();
        if (userId != comment.getFrom_id()) {
            Message message = new Message();
            String fromName = userRepository.findById(comment.getFrom_id()).getUsername();

            message.setUser_id(userId);
            message.setPost_id(comment.getPost_id());
            message.setPost_title(postDetail.getTitle());
            message.setFrom_id(authId);
            message.setFrom_name(fromName);
            message.setType(0);
            message.setContent(comment.getContent());

            messageRepository.addMessage(message);
        }

        res.put("success", true);
        res.put("msg", "回复成功!");

        userRepository.updateExperience(authId, 5);

        return res;
    }


    /**
     * 根据评论id删除该评论
     *
     * @param id 该评论id
     * @return msg
     */
    @GetMapping("del_comment")
    public JSONObject delComment(@Param("id") int id) {
        JSONObject res = new JSONObject();
        Comment comment = commentRepository.findComment(id);
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        User dbUser = userRepository.findById(authId);

        if (comment.getFrom_id() == authId || dbUser.getRole() == 0) {
            commentRepository.delComment(id);
            postRepository.updateCommentCount(comment.getPost_id(), -1);
            res.put("success", true);
            res.put("msg", "删除成功!");
        } else {
            res.put("success", false);
            res.put("msg", "删除失败!");
        }

        return res;
    }

    /**
     * 通过帖子id获取其所有评论以及作者信息
     *
     * @param id 帖子id
     * @return 包含所有评论的ArrayList
     */
    @GetMapping("/get_all")
    public JSONObject getAll(@Param("id") int id) {
        JSONObject res = new JSONObject();
        List<CommentAndUser> comment = commentRepository.findAllComment(id);

        res.put("success", true);
        res.put("data", comment);

        return res;
    }
}
