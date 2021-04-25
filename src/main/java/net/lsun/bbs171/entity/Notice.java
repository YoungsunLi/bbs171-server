package net.lsun.bbs171.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Notice {
    private int id;
    private int user_id;
    private String content;
    private Timestamp datetime;
    private int status;
}
