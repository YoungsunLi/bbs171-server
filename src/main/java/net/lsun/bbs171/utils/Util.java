package net.lsun.bbs171.utils;

import java.sql.Timestamp;
import java.util.Date;

public class Util {
    /**
     * Timestamp转xxx时间前
     *
     * @param datetime Timestamp
     * @return xxx时间前
     */
    public static String parseTimestampToXxxBefore(Timestamp datetime) {
        String newDatetime;
        long minutes = (new Date().getTime() - datetime.getTime()) / (1000 * 60);
        if (minutes < 60) {
            newDatetime = minutes + "分钟前";
        } else if (minutes < 1440) {
            newDatetime = minutes / 60 + "小时前";
        } else if (minutes < 43200) {
            newDatetime = minutes / (60 * 24) + "天前";
        } else {
            newDatetime = datetime.toString().substring(0, datetime.toString().indexOf("."));
        }
        return newDatetime;
    }

    /**
     * Timestamp转人类可读时间
     *
     * @param datetime Timestamp
     * @return 人类可读时间
     */
    public static String parseTimestampToString(Timestamp datetime) {
        return datetime.toString().substring(0, datetime.toString().length() - 2);
    }

    /**
     * 板块标识符转转中文名
     *
     * @param category 板块标识符
     * @return 板块中文名
     */
    public static String parseCategory(int category) {
        switch (category) {
            case 2:
                return "学习";
            case 3:
                return "生活";
            case 1:
            default:
                return "默认";
        }
    }
}
