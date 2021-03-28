package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class PostDetail {
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
    private int highlight;
    private int user_id;
    private int role;
    private String username;
    private String gender;
    private String sign;
    private String avatar;
    private int star;


    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }

    public String getCategory() {
        return Util.parseCategory(category);
    }
}
