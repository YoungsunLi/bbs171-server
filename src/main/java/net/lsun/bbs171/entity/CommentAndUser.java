package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class CommentAndUser {
    private int id;
    private int post_id;
    private int from_id;
    private String content;
    private Timestamp datetime;
    private int user_id;
    private int role;
    private String username;
    private String gender;
    private String sign;
    private String avatar;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
