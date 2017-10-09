package com.nenu.cwy.single.myfptree;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;

import java.io.*;
import java.util.*;

/**
 * Mycantree
 * Author： wychen
 * Date: 2017/10/5
 * Time: 16:08
 */
public class Mycantree {

    /**
     * 创建表头 按照字母序列排列
     *
     * @return
     */
    public LinkedList<TreeNode2> buildHeaderLink() {
        LinkedList<TreeNode2> header = new LinkedList<TreeNode2>();

        for (char i = 'a'; i <= 'z'; i++) {
          TreeNode2 item = new TreeNode2(String.valueOf(i));
          header.add(item);
        }
        return header;
    }

    /**
     * 创建表头 按照数字大小序列排列
     *
     * @return
     */
    public LinkedList<TreeNode2> buildHeaderLinkByInt() {
        LinkedList<TreeNode2> header = new LinkedList<TreeNode2>();

        for (int i = 0; i < 1000; i++) {
            TreeNode2 item = new TreeNode2(String.valueOf(i));
            header.add(item);
        }
        return header;
    }

    /**
     * @param records 构建树的记录,如I1,I2,I3
     * @param header  韩书中介绍的表头
     * @return 返回构建好的树
     */
    public TreeNode2 buildCanTree(LinkedList<LinkedList<String>> records, List<TreeNode2> header) {
        TreeNode2 root = new TreeNode2();
        if (records.size() <= 0) {
            return null;
        }
        for (LinkedList<String> items : records) {
            //从小到大排序
            Collections.sort(items);
            addNode(root, items, header);
        }
        return root;
    }

    /**
     * 给每一个节点添加子节点
     * 
     * @param root 根节点
     * @param items  项集
     * @param header  项头表
     * @return
     */
    public TreeNode2 addNode(TreeNode2 root, LinkedList<String> items, List<TreeNode2> header) {
        if (items.size() <= 0)
            return null;
        String item = items.poll();
        //当前节点的孩子节点不包括该节点，那么另外创建一支分支。

        TreeNode2 node = root.findChild(item);
        if (node == null) {
            node = new TreeNode2();
            node.setName(item);
            node.setCount(1);
            node.setParent(root);
            root.addChild(node);

            //将各个节点加到链头中
            for (TreeNode2 head : header) {
                if (head.getName().equals(item)) {
                    while (head.getNextHomonym() != null) {
                        head = head.getNextHomonym();
                    }
                    head.setNextHomonym(node);
                    break;
                }
            }
        } else {
            node.setCount(node.getCount() + 1);
        }
        addNode(node, items, header);
        return root;
    }

    /**
     * 根据叶子节点找到根节点root
     * 
     * @param node
     * @param newrecord
     */
    public void findRootByLeaf(TreeNode2 node, LinkedList<String> newrecord) {
        if (node.getParent() == null){
            return;
        }
        String name = node.getName();
        newrecord.add(name);
        findRootByLeaf(node.getParent(), newrecord);
    }

    /**
     * cantree挖掘
     *
     * @param records
     * @param item
     */
    public void cantreeGrowth(LinkedList<LinkedList<String>> records, String item, int support) {
        //保存新的条件模式基的各个记录，以又一次构造can tree
        LinkedList<LinkedList<String>> newrecords = new LinkedList<LinkedList<String>>();
        //构建链头
      LinkedList<TreeNode2> header = buildHeaderLink();
//        LinkedList<TreeNode2> header = buildHeaderLinkByInt();
        //构建can tree
        TreeNode2 cantree = buildCanTree(records, header);
        //结束递归的条件
        if (cantree == null) {
            return;
        }
        //打印结果,输出频繁项集
        if (item != null) {
            //寻找条件模式基,从链头开始
            for (int i = 0; i < header.size(); i++) {
                TreeNode2 head = header.get(i);
                Integer count = 0;
                while (head.getNextHomonym() != null) {
                    head = head.getNextHomonym();
                    //叶子count等于多少，就算多少条记录
                    count = count + head.getCount();
                }
                //打印频繁项集
                if(count >= support){
                    System.out.println(head.getName() + "," + item + "\t" + count);
                }
//                else{
//                    System.out.println("there is no frequent patterns which support is bigger than minsupport ");
//                    break;
//                }
            }
        }
        //寻找条件模式基,从链头开始
        for (int i = 0; i < header.size(); i++) {
            TreeNode2 head = header.get(i);
            String itemName;
            //再组合
            if (item == null) {
                itemName = head.getName();
            } else {
                itemName = head.getName() + "," + item;
            }

            while (head.getNextHomonym() != null) {
                head = head.getNextHomonym();
                //叶子count等于多少，就算多少条记录
                Integer count = head.getCount();
                for (int n = 0; n < count; n++) {
                    LinkedList<String> record = new LinkedList<String>();
                    findRootByLeaf(head.getParent(), record);
                    newrecords.add(record);
                }
            }
            //递归之,以求子can tree
            cantreeGrowth(newrecords, itemName, support);
        }
    }


    public static void main(String[] args) throws IOException {

        //获取开始时间
        long startTime = System.currentTimeMillis();

        //支持度阈值
        int support = 3;

        //读取数据
        Mycantree mycantree = new Mycantree();
        LinkedList<LinkedList<String>> records = LoadDataUtils.loadTransListByFilepath2(Constants.DICTIONARY_FILE_PATH);
//        LinkedList<LinkedList<String>> records = LoadDataUtils.loadTransListByFilepath2(Constants.T10I4D100K);
        mycantree.cantreeGrowth(records, null, support);

        //获取结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("程序运行时间:" + costTime + "ms");

//        File resultPath = new File(Constants.CANTREE_RESULT_PATH);
//        try {
//            if (!resultPath.exists()) {
//                resultPath.createNewFile();
//            }
//            FileWriter fw = new FileWriter(resultPath, true);
//            PrintWriter printWriter = new PrintWriter(fw);
//            printWriter.println("测试数据：" + Constants.DICTIONARY_FILE_PATH);
//            printWriter.println("支持度阈值：" + support);
//            printWriter.println("程序运行时间：" + costTime + "ms");
//
//            Runtime rt = Runtime.getRuntime();
//            long totalMemory = rt.totalMemory();
//            long freeMemory = rt.freeMemory();
//            System.out.println("占用内存：" + (totalMemory - freeMemory) / (1024 * 1024) + "MB");
//            printWriter.println("占用内存：" + (totalMemory - freeMemory) / (1024 * 1024) + "MB");
//
//            printWriter.println("\n");
//            fw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
