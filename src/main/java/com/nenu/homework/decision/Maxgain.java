package com.nenu.homework.decision;

/**
 * MaxGain
 * Author： wychen
 * Date: 2017/12/2
 * Time: 20:08
 */
public class Maxgain {
    private Double maxgain;  //增益值
    private Integer maxindex; //属性所在位置（数组下标）

    public Double getMaxgain() {
        return maxgain;
    }

    public void setMaxgain(Double maxgain) {
        this.maxgain = maxgain;
    }

    public Integer getMaxindex() {
        return maxindex;
    }

    public void setMaxindex(Integer maxindex) {
        this.maxindex = maxindex;
    }
}
