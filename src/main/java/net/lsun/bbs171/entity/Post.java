package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class Post {
    private int id;
    private int user_id;
    private String title;
    private String content;
    private Timestamp datetime;
    private int category;
    private int views;
    private int status;
    private int report;
    private int comment;
    private int highlight;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
