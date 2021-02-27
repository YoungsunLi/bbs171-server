package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.Post;
import net.lsun.bbs171.entity.PostDetail;
import net.lsun.bbs171.entity.PostsForIndex;
import net.lsun.bbs171.repository.PostRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 查询帖子列表以及对应的作者信息(首页)
     *
     * @param category 分类: 0=综合, 1=默认, 2=学习, 3=生活.
     * @param sort     排序方式: 0=按最新, 1=按热度, 2=按评论.
     * @param keywords 搜索标题关键字
     * @return 帖子列表
     */
    @GetMapping("/get_posts_for_index")
    public JSONObject getPostsForIndex(@Param("category") int category, @Param("sort") int sort, @Param("keywords") String keywords) {
        JSONObject json = new JSONObject();
        String sortStr;
        switch (sort) {
            case 1:
                sortStr = "views";
                break;
            case 2:
                sortStr = "comment";
                break;
            default:
                sortStr = "datetime";
        }

        List<PostsForIndex> posts = postRepository.findPostsForIndex(category, sortStr, keywords);
        json.put("success", true);
        json.put("data", posts);

        return json;
    }

    /**
     * 通过帖子id获取单个帖子信息和作者信息
     *
     * @param id 帖子id
     * @return 帖子详情
     */
    @GetMapping("/detail")
    public JSONObject getPostForDetail(@Param("id") int id) {
        JSONObject json = new JSONObject();
        PostDetail post = postRepository.findPostDetail(id);

        if (post == null || post.getStatus() == 2) {
            json.put("success", false);
            json.put("msg", "该帖子不存在!");
        } else {
            json.put("success", true);
            json.put("data", post);
        }

        return json;
    }
}
