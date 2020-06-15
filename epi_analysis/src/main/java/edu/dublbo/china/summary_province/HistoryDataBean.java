package edu.dublbo.china.summary_province;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * 该类记录当前日期之前的各地区疫情的汇总
 * 统计的主要指标：
 * suspect    confirm    heal    dead
 * 统计维度：
 * 国内：
 *      1.以省为单位
 *      2.以国家为单位
 * 国外：
 *      以国家为单位
 **/
public class HistoryDataBean implements Writable {

//    private String province;        // 省份   (key)
//    private String handleDate;        // 统计日期
//    private String area;            // 地区（市/区）
    private int confirm;            // 累计确诊
    private int confirm_add;        // 新增确诊
    private int heal;               // 累计治愈
    private int dead;               // 累计死亡

    public String toString(){
        return confirm + "\t" + confirm_add + "\t" + heal + "\t" + dead;
    }

    public void set(int confirm, int confirm_add, int heal, int dead){
        this.confirm = confirm;
        this.confirm_add = confirm_add;
        this.heal = heal;
        this.dead = dead;
    }

    public int getConfirm_add() {
        return confirm_add;
    }

    public void setConfirm_add(int confirm_add) {
        this.confirm_add = confirm_add;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(confirm);
        dataOutput.writeInt(confirm_add);
        dataOutput.writeInt(heal);
        dataOutput.writeInt(dead);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        confirm = dataInput.readInt();
        confirm_add = dataInput.readInt();
        heal = dataInput.readInt();
        dead = dataInput.readInt();
    }
}
