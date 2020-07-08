package edu.dublbo.china.summary_province;

import edu.dublbo.tools.Constant;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;

public class SumProvinceMapper extends Mapper<LongWritable, Text, Text, HistoryDataBean> {
    private Text keyData = new Text();
    private HistoryDataBean valueData = new HistoryDataBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        keyData.set(fields[Constant.DATE].split(" ")[0]+"\t"+fields[Constant.PROVINCE]);
        valueData.set(Integer.parseInt(fields[Constant.CONFIRM]), Integer.parseInt(fields[Constant.CONFIRM_ADD]),
                Integer.parseInt(fields[Constant.HEAL]), Integer.parseInt(fields[Constant.DEAD]));
        context.write(this.keyData, this.valueData);
    }


}
