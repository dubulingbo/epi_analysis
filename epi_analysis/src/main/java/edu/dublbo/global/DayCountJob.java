package edu.dublbo.global;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class DayCountJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if(args.length != 3){
            System.err.println("Path arguments error!");
            System.exit(2);
        }
        // 创建一个作业 
        Job job = Job.getInstance(new Configuration());

        // 指定主类（包含入口函数 main）
        job.setJarByClass(DayCountJob.class);

        // 指定 Mapper 和 Reducer 类
        job.setMapperClass(DCMapper.class);
        job.setReducerClass(DCReducer.class);

        // 指定 MapReduce 的输入和输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Continent.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Continent.class);

        // 指定 要处理的数据的输入路径 和 结果数据的输出目录（一定要不存在）
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        // 提交作业
        boolean b = job.waitForCompletion(true);
    }
}

/**
 * 自定义 Mapper 类
 * 0: update_time
 * 1: continent
 * 2: country
 * 3: confirm
 * 4: now_confirm
 */
class DCMapper extends Mapper<LongWritable, Text, Text, Continent> {
    private Text keyData = new Text();
    private Continent valueData = new Continent();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        if (!fields[1].isEmpty() && !fields[1].equals("其他")) {
            this.keyData.set(fields[0].split(" ")[0] + "\t" + fields[1]);
            this.valueData.set(Integer.valueOf(fields[3]), Integer.valueOf(fields[4]));
            context.write(this.keyData, this.valueData);
        }
    }
}


/**
 * 自定义 Reducer 类
 */

class DCReducer extends Reducer<Text, Continent, Text, Continent> {
    private Continent sumContinentData = new Continent();

    @Override
    protected void reduce(Text key, Iterable<Continent> values, Context context) throws IOException, InterruptedException {
        int sumConfirm = 0;
        int sumNow_confirm = 0;
        for (Continent value : values) {
            sumConfirm += value.getConfirm();
            sumNow_confirm += value.getNow_confirm();
        }
        this.sumContinentData.set(sumConfirm, sumNow_confirm);
        context.write(key, this.sumContinentData);
    }
}