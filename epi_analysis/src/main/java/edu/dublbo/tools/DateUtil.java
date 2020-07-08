package edu.dublbo.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 转换日期格式
     * yyyyMMdd -> yyyy-MM-dd
     */
    public static String transDataFormat(String dt) {
        String res = "1970-01-01";

        try {
            Date date = sdf1.parse(dt);
            res = sdf2.format(date);
        } catch (ParseException e) {
            System.out.print("日期转换失败：" + dt);
            e.printStackTrace();
        }
        return res;
    }

    public static Date stringToDate(String source) {
        try {
            return sdf2.parse(source);
        } catch (ParseException e) {
            System.out.print("日期转换失败：" + source);
            e.printStackTrace();
            return null;
        }
    }
}
