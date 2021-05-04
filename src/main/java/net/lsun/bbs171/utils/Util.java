package net.lsun.bbs171.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
        } else if (minutes < 4320) { // 3天
            newDatetime = minutes / (60 * 24) + "天前";
        } else {
            newDatetime = parseTimestampToString(datetime);
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
     * 排序类型转列名
     *
     * @param sort 排序类型
     * @return 列名
     */
    public static String parseSort(int sort) {
        String sortStr;
        switch (sort) {
            case 1:
                sortStr = "views";
                break;
            case 2:
                sortStr = "comment";
                break;
            default:
                sortStr = "datetime";
        }

        return sortStr;
    }


    /**
     * 获取字符串的 md5
     *
     * @param s 字符串
     * @return md5
     */
    public static String getMD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
