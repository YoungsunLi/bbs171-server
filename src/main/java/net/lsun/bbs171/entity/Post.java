package net.lsun.bbs171.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Post {
    private int id;
    private String phone;
    private String title;
    private String content;
    private Timestamp datetime;
    private int category;
    private int views;
    private int status;
    private int report;
    private int comment;
}
