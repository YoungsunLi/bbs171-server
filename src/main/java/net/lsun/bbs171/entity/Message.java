package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class Message {
    private int id;
    private int user_id;
    private int post_id;
    private String post_title;
    private int from_id;
    private String from_name;
    private int type;
    private String content;
    private int read;
    private Timestamp datetime;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
