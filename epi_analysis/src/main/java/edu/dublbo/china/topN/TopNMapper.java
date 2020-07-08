package edu.dublbo.china.topN;

import edu.dublbo.tools.Constant;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * top10任务
 * 统计当天国内确诊数量排名前 10 个省份
 */
public class TopNMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    //    private int index;  // 需要统计的指标
    private Text keyData = new Text();
    private LongWritable valueData = new LongWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        keyData.set(fields[Constant.DATE - 1] + "\t" + fields[Constant.PROVINCE - 1]);
        // 使用 confirm 字段进行统计
        valueData.set(Long.parseLong(fields[Constant.CONFIRM - 2]));
        context.write(this.keyData, this.valueData);
    }
}
