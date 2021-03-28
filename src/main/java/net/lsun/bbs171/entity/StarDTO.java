package net.lsun.bbs171.entity;

import lombok.Data;
import net.lsun.bbs171.utils.Util;

import java.sql.Timestamp;

@Data
public class StarDTO {
    private int id;
    private String title;
    private Timestamp datetime;

    public String getDatetime() {
        return Util.parseTimestampToXxxBefore(datetime);
    }
}
