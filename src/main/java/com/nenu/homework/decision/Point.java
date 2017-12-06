package com.nenu.homework.decision;

/**
 * Point
 * Author： wychen
 * Date: 2017/12/2
 * Time: 20:06
 */
public class Point {
    private Double sv;  //出现次数
    private Double entropySv; //信息熵

    public Double getSv() {
        return sv;
    }

    public void setSv(Double sv) {
        this.sv = sv;
    }

    public Double getEntropySv() {
        return entropySv;
    }

    public void setEntropySv(Double entropySv) {
        this.entropySv = entropySv;
    }
}
