package net.lsun.bbs171.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Star {
    private int id;
    private int user_id;
    private int post_id;
    private Timestamp datetime;
}
