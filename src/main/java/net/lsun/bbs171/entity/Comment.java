package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class Comment {
    private int id;
    private int post_id;
    private int from_id;
    private int to_id;
    private String content;
    private Timestamp datetime;
    private int status;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
