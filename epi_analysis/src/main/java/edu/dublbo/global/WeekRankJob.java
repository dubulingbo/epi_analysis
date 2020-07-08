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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeekRankJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        String tmp = args[0].substring(args[0].lastIndexOf("/") + 1);
        if (tmp.isEmpty()) {
            System.err.println("主程序提供的路径不合法！");
            System.exit(2); // 提供的路径不合法！
        }

        Configuration conf = new Configuration();
        conf.set("weekend_date", tmp);
        Job job = Job.getInstance(conf);

        job.setJarByClass(WeekRankJob.class);

        job.setMapperClass(WRMapper.class);
        job.setReducerClass(WRReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Continent.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Continent.class);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(tmp);
        String inputPath = "hdfs://hadoop100:9000/user/epidemic/con_week_count_job/day_count";
        for (int i = 6; i >= 0; i--) {
            tmp = inputPath + "/" + df.format(new Date(d.getTime() - (long) i * 24 * 60 * 60 * 1000)) + "/deal_data";
            FileInputFormat.addInputPath(job, new Path(tmp));
        }
        FileOutputFormat.setOutputPath(job, new Path(args[0] + "/deal_data"));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}

/**
 * 自定义 Mapper 类
 * 0: update_time
 * 1: continent
 * 2: country
 * 3: confirm_add
 * 4: now_confirm_add
 */
class WRMapper extends Mapper<LongWritable, Text, Text, Continent> {
    private Text keyData = new Text();
    private Continent valueData = new Continent();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        String tmp = context.getConfiguration().get("weekend_date");
        if (tmp.isEmpty()) {
            System.err.println("日期属性为空，异常结束！");
            System.exit(3);
        } else {
            this.keyData.set(tmp + "\t" + fields[1]);
            this.valueData.set(Integer.valueOf(fields[2]), Integer.valueOf(fields[3]));
            context.write(this.keyData, this.valueData);
        }
    }
}


/**
 * 自定义 Reducer 类
 */

class WRReducer extends Reducer<Text, Continent, Text, Continent> {
    private Continent weekContinentData = new Continent();

    @Override
    protected void reduce(Text key, Iterable<Continent> values, Context context) throws IOException, InterruptedException {
        int weekConfirm = 0;
        int weekNow_confirm = 0;
        for (Continent value : values) {
            weekConfirm += value.getConfirm();
            weekNow_confirm += value.getNow_confirm();
        }
        this.weekContinentData.set(weekConfirm, weekNow_confirm);
        context.write(key, this.weekContinentData);
    }
}