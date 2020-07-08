package edu.dublbo.china.summary_province;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SumProvinceJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(SumProvinceJob.class);

        job.setMapperClass(SumProvinceMapper.class);
        job.setReducerClass(SumProvinceReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(HistoryDataBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(HistoryDataBean.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
