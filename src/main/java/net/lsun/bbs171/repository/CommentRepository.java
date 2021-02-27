package net.lsun.bbs171.repository;

import net.lsun.bbs171.entity.Comment;
import net.lsun.bbs171.entity.CommentAndUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentRepository {
    void submit(Comment comment);

    List<CommentAndUser> findAllComment(@Param("id") int id);
}
