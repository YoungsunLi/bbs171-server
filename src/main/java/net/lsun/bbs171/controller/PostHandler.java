package net.lsun.bbs171.controller;

import com.alibaba.fastjson.JSONObject;
import net.lsun.bbs171.entity.*;
import net.lsun.bbs171.repository.PostRepository;
import net.lsun.bbs171.repository.UserRepository;
import net.lsun.bbs171.repository.ViewRepository;
import net.lsun.bbs171.utils.SensitiveFilterUtil;
import net.lsun.bbs171.utils.Util;
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

    @Resource
    private UserRepository userRepository;

    @Resource
    private ViewRepository viewRepository;

    @Resource
    private SensitiveFilterUtil sensitiveFilter;

    /**
     * 发布帖子
     *
     * @param post 帖子
     * @return msg
     */
    @PostMapping("/submit")
    public JSONObject submit(@RequestBody Post post) {
        JSONObject json = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        post.setUser_id(authId);

        post.setTitle(sensitiveFilter.filter(post.getContent()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        postRepository.submit(post);

        json.put("success", true);
        json.put("msg", "发布成功!");

        userRepository.updateExperience(authId, 20);

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
        String authIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        int authId = authIdStr.equals("anonymousUser") ? 0 : Integer.parseInt(authIdStr);

        PostDetail post = postRepository.findPostDetail(id, authId);

        if (post != null) { //如果帖子存在
            if (post.getStatus() == 1) { //如果帖子已审核
                postRepository.updateViews(id);

                Views views = new Views();
                views.setPost_id(id);
                views.setViewer_id(authId);
                viewRepository.submit(views);

                json.put("success", true);
                json.put("data", post);
                return json;
            } else { // 否则验证是否是管理员
                if (authId != 0) {
                    int role = userRepository.findById(authId).getRole();
                    if (role == 0) {
                        json.put("success", true);
                        json.put("data", post);
                        return json;
                    }
                }
            }
        }

        json.put("success", false);
        json.put("msg", "该帖子不存在!");

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
    public JSONObject getPostForIndex(@Param("category") int category, @Param("sort") int sort, @Param("keywords") String keywords) {
        JSONObject json = new JSONObject();
        String sortStr = Util.parseSort(sort);

        List<PostForIndex> posts = postRepository.findPostsForIndex(category, sortStr, keywords);
        json.put("success", true);
        json.put("data", posts);

        return json;
    }

    /**
     * 查询帖子列表以及对应的作者信息(管理帖子页)
     *
     * @param category 板块: 0=综合, 1=默认, 2=学习, 3=生活.
     * @param sort     排序方式: 0=按最新, 1=按热度, 2=按评论.
     * @param status   帖子状态: 3=全部, 0=未审核, 1=已审核, 2=已删除.
     * @param keywords 搜索标题关键字
     * @return 帖子列表
     */
    @GetMapping("/get_posts_for_manage")
    public JSONObject getPostForManage(@Param("category") int category, @Param("sort") int sort, @Param("status") int status, @Param("keywords") String keywords) {
        JSONObject json = new JSONObject();
        String sortStr = Util.parseSort(sort);
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User dbUser = userRepository.findById(authId);

        if (dbUser.getRole() != 0) {
            // 非管理员
            json.put("success", false);
            json.put("msg", "非法操作!");
        } else {
            List<PostForManage> posts = postRepository.findPostsForManage(category, sortStr, status, keywords);
            json.put("success", true);
            json.put("data", posts);
        }

        return json;
    }

    /**
     * 管理帖子(审核 删除)
     *
     * @param id     帖子id
     * @param status 帖子状态
     * @return msg
     */
    @GetMapping("/update_status")
    public JSONObject updateStatus(@Param("id") int id, @Param("status") int status) {
        JSONObject json = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        int role = userRepository.findById(authId).getRole();

        if (role != 0) {
            int postUserId = postRepository.findPostDetail(id, authId).getUser_id();
            if (authId != postUserId) {
                json.put("success", false);
                json.put("msg", "非法操作!");
                return json;
            } else {
                // 非管理员只能删除
                status = 2;
            }
        }

        postRepository.updateStatus(id, status);
        json.put("success", true);
        json.put("msg", "操作成功!");

        return json;
    }

    /**
     * 设置精华帖
     *
     * @param id        帖子id
     * @param highlight 0=非精华 1=精华
     * @return msg
     */
    @GetMapping("/update_highlight")
    public JSONObject updateHighlight(@Param("id") int id, @Param("highlight") int highlight) {
        JSONObject json = new JSONObject();
        int authId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        int role = userRepository.findById(authId).getRole();

        if (role == 0) {
            postRepository.updateHighlight(id, highlight);

            json.put("success", true);
            json.put("msg", "设置成功!");
        } else {
            json.put("success", false);
            json.put("msg", "非法操作!");
        }

        return json;
    }

    /**
     * 获取热帖
     *
     * @return posts
     */
    @GetMapping("/get_hot")
    public JSONObject getHot() {
        JSONObject json = new JSONObject();

        List<PostForHot> posts = postRepository.getHot();

        json.put("success", true);
        json.put("data", posts);

        return json;
    }

    /**
     * 获取分类信息
     *
     * @return category
     */
    @GetMapping("get_category")
    public JSONObject getCategory() {
        JSONObject json = new JSONObject();

        List<Category> categorys = postRepository.getCategory();

        json.put("success", true);
        json.put("data", categorys);

        return json;
    }
}
