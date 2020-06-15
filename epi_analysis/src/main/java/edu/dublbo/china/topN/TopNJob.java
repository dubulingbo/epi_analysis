package edu.dublbo.china.topN;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;


/**
 * 统计每天确诊人数前 10名的城市
 */

public class TopNJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // Job 需要的配置参数
        Configuration conf = new Configuration();
        // 设置统计的指标
//        int index = Integer.parseInt(args[2]);
//        if(index < 4 || index > 7){
//            System.out.print("参数有误！");
//            System.exit(200);
//        }
////        String dt = DateUtil.transDataFormat(fields[fields.length-1]);
//        conf.set("index", args[2]);
        Job job = Job.getInstance(conf);

        job.setJarByClass(TopNJob.class);

        job.setMapperClass(TopNMapper.class);
        job.setReducerClass(TopNReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }




}
