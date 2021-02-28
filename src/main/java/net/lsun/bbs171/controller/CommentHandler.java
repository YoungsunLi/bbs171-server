package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Comment;
import net.lsun.bbs171.entity.CommentAndUser;
import net.lsun.bbs171.repository.CommentRepository;
import net.lsun.bbs171.repository.PostRepository;
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

    /**
     * 回复帖子
     *
     * @param comment 回复
     * @return 结果
     */
    @PostMapping("/submit")
    public JSONObject submit(@RequestBody Comment comment) {
        JSONObject json = new JSONObject();
        comment.setFrom_phone(SecurityContextHolder.getContext().getAuthentication().getName());
        commentRepository.submit(comment);
        postRepository.updateCommentCount(comment.getPost_id(), 1);

        json.put("success", true);
        json.put("msg", "回复成功!");

        return json;
    }


    /**
     * 根据评论id删除该评论
     *
     * @param id 该评论id
     */
    @GetMapping("del_comment")
    public JSONObject delComment(@Param("id") int id) {
        JSONObject json = new JSONObject();
        Comment comment = commentRepository.findComment(id);

        if (comment.getFrom_phone().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            commentRepository.delComment(id);
            postRepository.updateCommentCount(comment.getPost_id(), -1);
            json.put("success", true);
            json.put("msg", "删除成功!");
        } else {
            json.put("success", false);
            json.put("msg", "删除失败!");
        }

        return json;
    }

    /**
     * 通过帖子id获取其所有评论以及作者信息
     *
     * @param id 帖子id
     * @return 包含所有评论的ArrayList
     */
    @GetMapping("/get_all")
    public JSONObject getAll(@Param("id") int id) {
        JSONObject json = new JSONObject();
        List<CommentAndUser> comment = commentRepository.findAllComment(id);

        json.put("success", true);
        json.put("data", comment);

        return json;
    }
}
