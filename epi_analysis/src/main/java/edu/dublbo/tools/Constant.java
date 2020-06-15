package edu.dublbo.tools;

import java.util.*;

public class Constant {
    // 对应 detail 数据表中的字段
    public static int DATE = 1;
    public static int PROVINCE = 2;
    public static int CITY = 3;
    public static int CONFIRM = 4;
    public static int CONFIRM_ADD = 5;
    public static int HEAL = 6;
    public static int DEAD = 7;


    // 根据 Map中的value值降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int value = (o1.getValue()).compareTo(o2.getValue());
            return -value;
        });
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }



}
