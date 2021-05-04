package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class PostForIndex {
    private int id;
    private String title;
    private Timestamp datetime;
    private int category;
    private String category_name;
    private int views;
    private int comment;
    private int highlight;
    private int user_id;
    private String username;
    private String level_name;
    private String avatar;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
