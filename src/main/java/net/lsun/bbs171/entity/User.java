package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class User {
    private int id;
    private int role;
    private String phone;
    private String username;
    private String password;
    private String gender;
    private String sign;
    private String avatar;
    private String level_name;
    private Timestamp datetime;
    private Timestamp lastTime;

    public String getDatetime() {
        return Util.parseTimestampToString(datetime);
    }

    public String getLastTime() {
        return Util.parseTimestampToString(lastTime);
    }
}
