package com.nenu.cwy.mapreduce.mpct;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MPCT
 * Author： wychen
 * Date: 2017/9/25
 * Time: 21:29
 */
public class MPCT {

    //测试数据集路径
    private static String input = Constants.T10I4D100K;
    //读取文件后存放数据集合
    private static List<List<String>> translations = new ArrayList<List<String>>();

    public static void main(String[] args) {
        try {
            translations = LoadDataUtils.loadTransListByFilepath(input);




        }catch (Exception e){
            System.out.println("MPCT程序运行过程中出错了.........");
            e.printStackTrace();
        }
    }
}
