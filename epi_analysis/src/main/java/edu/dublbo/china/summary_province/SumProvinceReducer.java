package edu.dublbo.china.summary_province;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SumProvinceReducer extends Reducer<Text, HistoryDataBean, Text, HistoryDataBean> {
    private HistoryDataBean sumHistoryData = new HistoryDataBean();

    @Override
    protected void reduce(Text key, Iterable<HistoryDataBean> values, Context context) throws IOException, InterruptedException {
        int sum_confirm_add = 0;
        int sum_confirm = 0;
        int sum_heal = 0;
        int sum_dead = 0;
        for(HistoryDataBean value : values){
            sum_confirm_add += value.getConfirm_add();
            sum_confirm += value.getConfirm();
            sum_heal += value.getHeal();
            sum_dead += value.getDead();
        }
        this.sumHistoryData.set(sum_confirm, sum_confirm_add, sum_heal, sum_dead);
        context.write(key, this.sumHistoryData);
    }
}
