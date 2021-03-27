package net.lsun.bbs171.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {
    // 设置过期时间 5 分钟
    private static final int TIMEOUT = 60000 * 5;
    // 设置list中的索引  0 表示数据索引
    private static final Integer DATA_INDEX = 0;
    // 设置list中的索引  1 表示保存的时间戳索引
    private static final Integer TIME_INDEX = 1;
    // 创建一个Map用来保存数据
    private static final Map<String, List<Object>> cache = new HashMap<>();

    // 构造方法私有化不能创建对象
    private CacheUtil() {
    }

    /**
     * @param code 验证码
     * @param key  储存数据的key
     */
    public static void putData(String key, String code) {
        clearData();

        List<Object> list = new ArrayList<>();
        list.add(code);
        list.add(System.currentTimeMillis());
        cache.put(key, list);
    }

    /**
     * @param key 取出数据的key
     * @return 返回数据值 null为不存在。
     */
    public static String getData(String key) {
        if (cache.get(key) == null) {
            return null;
        }
        List<Object> list = cache.get(key);
        String code = (String) list.get(DATA_INDEX);
        long setTime = (long) list.get(TIME_INDEX);
        long currentTime = System.currentTimeMillis();
        if (currentTime - setTime > TIMEOUT) {
            cache.remove(key);
            return null;
        }
        cache.remove(key);

        return code;
    }

    /**
     * 清理过期数据
     */
    public static void clearData() {
        for (String key : cache.keySet()) {
            List<Object> list = cache.get(key);
            long setTime = (long) list.get(TIME_INDEX);
            long currentTime = System.currentTimeMillis();
            if (currentTime - setTime > TIMEOUT) {
                cache.remove(key);
            }
        }
    }
}
