package net.lsun.bbs171.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Views {
    private int id;
    private int post_id;
    private int viewer_id;
    private String user_agent;
    private String ip;
    private Timestamp datetime;
}
