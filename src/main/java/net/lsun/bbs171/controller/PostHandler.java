package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Post;
import net.lsun.bbs171.repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/post")
public class PostHandler {

    @Resource
    private PostRepository postRepository;

    /**
     * 发布帖子
     *
     * @param post 帖子
     * @return 结果
     */
    @PostMapping("/submit")
    public JSONObject submit(@RequestBody Post post) {
        JSONObject json = new JSONObject();
        post.setPhone(SecurityContextHolder.getContext().getAuthentication().getName());
        postRepository.submit(post);
        json.put("success", true);
        json.put("msg", "发布成功!");

        return json;
    }
}
