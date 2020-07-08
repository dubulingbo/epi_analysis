package edu.dublbo.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {


    public static void main(String[] args) throws ParseException {
        String _tmp = "hdfs://hadoop100:9000/user/epidemic/con_week_count_job/week_count/2020-06-07";
        String tmp = _tmp.substring(_tmp.lastIndexOf("/") + 1);
//        System.out.println(DateUtil.stringToDate(tmp).toString());

        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(tmp);
        for(int i=6;i>=0;i--)
            System.out.println(i + " 天前的日期：" + df.format(new Date(d.getTime() - (long)i * 24 * 60 * 60 * 1000)));
//        System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + (long)3 * 24 * 60 * 60 * 1000)));

    }
}
