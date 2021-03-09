package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class PostsForIndex {
    private int id;
    private String title;
    private Timestamp datetime;
    private int category;
    private int views;
    private int comment;
    private int highlight;
    private int user_id;
    private String username;
    private String avatar;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }

    public String getCategory() {
        return Util.parseCategory(category);
    }
}
