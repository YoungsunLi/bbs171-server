package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class Notice {
    private int id;
    private int user_id;
    private String username;
    private String content;
    private Timestamp datetime;
    private int status;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
