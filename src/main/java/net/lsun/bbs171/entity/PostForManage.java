package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class PostForManage {
    private int id;
    private String title;
    private Timestamp datetime;
    private int category;
    private int views;
    private int status;
    private int report;
    private int comment;
    private int user_id;
    private String username;

    public String getDatetime() {
        return Util.parseTimestampToString(datetime);
    }

    public String getCategory() {
        return Util.parseCategory(category);
    }
}
