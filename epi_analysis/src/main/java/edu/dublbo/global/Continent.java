package edu.dublbo.global;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Continent implements Writable {
    private int confirm;            // 截止到当前日期的累计确诊
    private int now_confirm;        // 截止到当前日期的现有确诊

    public String toString(){ return confirm + "\t" + now_confirm; }

    public void set(int confirm,int now_confirm){
        this.confirm = confirm;
        this.now_confirm = now_confirm;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getNow_confirm() {
        return now_confirm;
    }

    public void setNow_confirm(int now_confirm) {
        this.now_confirm = now_confirm;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(confirm);
        dataOutput.writeInt(now_confirm);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.confirm = dataInput.readInt();
        this.now_confirm = dataInput.readInt();
    }
}
