package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostRepository {
    void submit(Post post);

    List<PostForIndex> findPostsForIndex(@Param("category") int category, @Param("sort") String sort, @Param("keywords") String keywords);

    List<PostForManage> findPostsForManage(@Param("category") int category, @Param("sort") String sort, @Param("status") int status, @Param("keywords") String keywords);

    List<PostForUserHome> findPostsForUserHome(@Param("phone") String phone);

    PostDetail findPostsDetail(@Param("id") int id);

    void updateViews(@Param("id") int id);

    void updateCommentCount(@Param("id") int id, @Param("num") int num);

    void updateStatus(@Param("id") int id, @Param("status") int status);
}
