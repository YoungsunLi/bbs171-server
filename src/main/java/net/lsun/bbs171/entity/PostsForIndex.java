package net.lsun.bbs171.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostsForIndex {
    private int id;
    private String title;
    private Timestamp datetime;
    private int category;
    private int views;
    private int comment;
    private int user_id;
    private String username;
    private String avatar;
}
