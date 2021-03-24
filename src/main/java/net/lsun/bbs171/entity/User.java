package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class User {
    private Long id;
    private int role;
    private String phone;
    private String username;
    private String password;
    private String gender;
    private String sign;
    private String avatar;
    private Timestamp datetime;

    public String getDatetime() {
        return Util.parseTimestampToString(datetime);
    }
}
