package com.nenu.cwy.common;

import com.nenu.cwy.single.cantree.CanTreeNode;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * LoadDataUtils
 * Author： wychen
 * Date: 2017/9/25
 * Time: 9:46
 */
public class LoadDataUtils {

    public static final ArrayList<String> dataList = new ArrayList<String>();

    /**
     * 初始化测试数据集
     *
     * @return
     */
    public static ArrayList<String> loadTranslistStatic(){
        dataList.add("1 2 5 ");
        dataList.add("2 4 ");
        dataList.add("2 3 ");
        dataList.add("1 2 4 ");
        dataList.add("1 3 ");
        dataList.add("2 3 ");
        dataList.add("1 3 ");
        dataList.add("1 2 3 5 ");
        dataList.add("1 2 3 ");
        return dataList;
    }

    /**
     * 根据url读取数据
     *
     * @param url
     * @return
     */
    public static List<List<String>> loadTransListByUrl(String url) {
        //数据集合
        List<List<String>> translations = new ArrayList<List<String>>();
        //存放每行数据
        String a = "";
        //计数
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new URL(url).openConnection().getInputStream(), "utf8"));
            System.out.println("translations are:*********************************");
            while ((a = br.readLine()) != null) {
                i++;
                System.out.println("第"+ i + "行" + a);
                translations.add(Arrays.asList(a.split(" ")));
            }
            System.out.println("load translations end*****************************");
        } catch (IOException e) {
            System.out.println("读取网页数据出错");
            e.printStackTrace();
        }
        return translations;
    }

    /**
     * 根据文件路径读取数据
     *
     * @param filePath
     * @return
     */
    public static List<List<String>> loadTransListByFilepath(String filePath) {
        //数据集合
        List<List<String>> translations = new ArrayList<List<String>>();
        //存放每行数据
        String a = "";
        //计数
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            System.out.println("translations are:*********************************");
            while ((a = br.readLine()) != null) {
                i++;
                System.out.println("第"+ i + "行" + a);
                translations.add(Arrays.asList(a.split(" ")));
            }
            System.out.println("load translations end*****************************");
        } catch (IOException e) {
            System.out.println("读取文件数据出错");
            e.printStackTrace();
        }
        return translations;
    }

    /**
     * 根据文件路径读取数据
     *
     * @param filePath
     * @return
     */
    public static LinkedList<LinkedList<String>> loadTransListByFilepath2(String filePath) {
        //数据集合
        LinkedList<LinkedList<String>> translations = new LinkedList<LinkedList<String>>();
        //存放每行数据
        String a = "";
        //计数
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            System.out.println("translations are:*********************************");
            while ((a = br.readLine()) != null) {
                i++;
                System.out.println("第"+ i + "行" + a);
                String[] line = a.split(" ");
                LinkedList<String> item = new LinkedList<String>();
                for(int k = 0; k < line.length; k++){
                     item.add(line[k]);
                }
                translations.add(item);
            }
            System.out.println("load translations end*****************************");
        } catch (IOException e) {
            System.out.println("读取文件数据出错");
            e.printStackTrace();
        }
        return translations;
    }


    /**
     * 根据字典序列构建项头表
     *
     * @return
     */
    public static LinkedList<CanTreeNode> buildHeaderTableByDictionary() {
        LinkedList<CanTreeNode> canTreeNodeList = new LinkedList<CanTreeNode>();
        for (char letter = 'a'; letter <= 'g'; letter++) {
            CanTreeNode canTreeNode = new CanTreeNode(String.valueOf(letter));
            canTreeNode.setCount(0);
            canTreeNodeList.add(canTreeNode);
        }
        return canTreeNodeList;
    }

}
