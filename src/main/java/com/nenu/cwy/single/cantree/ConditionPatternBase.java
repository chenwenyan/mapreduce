package com.nenu.cwy.single.cantree;

import java.util.LinkedList;
import java.util.List;

/**
 * ConditionPatternBase
 * 条件模式基
 * Author： wychen
 * Date: 2017/10/3
 * Time: 16:41
 */
public class ConditionPatternBase {

    //条件模式基value
    private LinkedList<String> value;

    //出现次数
    private Integer count;

    public LinkedList<String> getValue() {
        return value;
    }

    public void setValue(LinkedList<String> value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ConditionPatternBase(){

    }

    public ConditionPatternBase(LinkedList<String> value, Integer count){
        this.value = value;
        this.count = count;
    }
}
