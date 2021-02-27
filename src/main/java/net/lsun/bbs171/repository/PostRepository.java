package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Post;
import net.lsun.bbs171.entity.PostDetail;
import net.lsun.bbs171.entity.PostsForIndex;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostRepository {
    void submit(Post post);

    List<PostsForIndex> findPostsForIndex(@Param("category") int category, @Param("sort") String sort, @Param("keywords") String keywords);

    PostDetail findPostDetail(@Param("id") int id);
}
