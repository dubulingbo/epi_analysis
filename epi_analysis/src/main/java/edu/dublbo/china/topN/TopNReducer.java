package edu.dublbo.china.topN;

import edu.dublbo.tools.Constant;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TopNReducer extends Reducer<Text, LongWritable, Text, IntWritable> {
    private HashMap<String, Long> info = new HashMap<>();
    private Text k3 = new Text();
    private IntWritable rank = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) {
        long sum = 0L;
        for (LongWritable value : values) {
            sum += value.get();
        }
        info.put(key.toString(), sum);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        Map<String, Long> sortedMap = Constant.sortValue(info);
        Set<Map.Entry<String, Long>> entries = sortedMap.entrySet();
        Iterator<Map.Entry<String, Long>> it = entries.iterator();
        int count = 1;
        while (count <= 10 && it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            // 封装 k3, v3
            k3.set(entry.getKey());
            rank.set(count);
//            System.out.println(k3.toString() + " " + v3.get());
            context.write(k3, rank);
            ++count;
        }
    }
}
