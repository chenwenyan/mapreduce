package com.nenu.wychen.fpgrowth;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;
import com.nenu.wychen.node.TreeNode;

import java.util.*;

/**
 * FPtree
 * Author： wychen
 * Date: 2017/10/26
 * Time: 9:09
 */
public class FPtree {

    /**
     * 构建项头表
     *
     * @param translations
     * @param miniSupport
     * @return
     */
    public LinkedList<TreeNode> buildHeaderTable(LinkedList<LinkedList<String>> translations,
                                                 int miniSupport) {
        if (translations.size() <= 0) {
            return null;
        }
        LinkedList<TreeNode> headerTable = new LinkedList<TreeNode>();
        Map<String, TreeNode> map = new HashMap<String, TreeNode>();
        for (LinkedList<String> line : translations) {
            if (line.size() <= 0) {
                break;
            }
            for (String item : line) {
                if (map.containsKey(item)) {
                    map.get(item).Sum(1);
                } else {
                    TreeNode treeNode = new TreeNode(item, 1);
                    map.put(item, treeNode);
                }
            }
        }

        //大于等于支持度阈值的节点放到headerTable中
        Set<String> names = map.keySet();
        for (String name : names) {
            TreeNode node = map.get(name);
            if (node.getCount() >= miniSupport) {
                headerTable.add(node);
            }
        }
        sort(headerTable);
        return headerTable;
    }

    /**
     * 冒泡排序
     * 按照支持度计数降序
     *
     * @param headerTable
     * @return
     */
    public LinkedList<TreeNode> sort(LinkedList<TreeNode> headerTable) {
        if (headerTable.size() <= 0) {
            return null;
        }
        for (int i = 0; i < headerTable.size() - 1; i++) {
            for (int j = i + 1; j < headerTable.size(); j++) {
                TreeNode node1 = headerTable.get(i);
                TreeNode node2 = headerTable.get(j);
                if (node1.getCount() < node2.getCount()) {
                    TreeNode temp = new TreeNode();
                    temp = node2;
                    headerTable.remove(j);
                    headerTable.add(j, node1);
                    headerTable.remove(i);
                    headerTable.add(i, temp);
                }
            }
        }
        return headerTable;
    }


    /**
     * 构建fp树结构
     *
     * @param translations
     * @param headerTable
     * @return
     */
    public TreeNode buildFPTree(LinkedList<LinkedList<String>> translations,
                                LinkedList<TreeNode> headerTable) {
        if (translations.size() <= 0) {
            return null;
        }
        TreeNode root = new TreeNode();
        for (LinkedList<String> record : translations) {
            LinkedList<String> newRecord = sortByHeader(record,headerTable);
            addTreeNode(root, newRecord, headerTable);
        }
        return root;
    }

    /**
     * 处理每一条记录 按照项头表顺序排列，去除支持度计数小于支持度阈值的项
     *
     * @param record
     * @param headerTable
     * @return
     */
    public LinkedList<String> sortByHeader(LinkedList<String> record,
                             LinkedList<TreeNode> headerTable){
        LinkedList<String> sortedRecord = new LinkedList<String>();
        if(record.size() <= 0){
            return null;
        }
        int i = 0;
        for(TreeNode treeNode : headerTable){
            for(String item : record){
                if(treeNode.getName().equals(item)){
                    sortedRecord.add(i,item);
                    i++;
                }
            }
        }
        return sortedRecord;
    }

    /**
     * 添加节点
     *
     * @param root
     * @param record
     * @param headerTable
     * @return
     */
    private TreeNode addTreeNode(TreeNode root,
                             LinkedList<String> record,
                             LinkedList<TreeNode> headerTable) {
        if (record.size() <= 0) {
            return null;
        }

        String item = record.poll();
        TreeNode node = root.findChild(item);
        if (node == null) {
            node = new TreeNode(item, 1);
            root.addChild(node);
            node.setParent(root);

            //headerTable中添加同名节点的链接
            for(TreeNode treeNode : headerTable){
                if(treeNode.getName().equals(item)){
                    while (treeNode.getNextNamesake() != null){
                        treeNode = treeNode.getNextNamesake();
                    }
                    treeNode.setNextNamesake(node);
                    break;
                }
            }
        } else {
            node.Sum(1);
        }
        addTreeNode(node, record, headerTable);
        return root;
    }

    /**
     * 挖掘频繁模式
     *
     * @param translations
     * @param miniSupport
     * @param item
     */
    public void fpGrowth(LinkedList<LinkedList<String>> translations,
                         int miniSupport,
                         String item){

        LinkedList<TreeNode> headerTable = buildHeaderTable(translations,miniSupport);

        TreeNode fptree = buildFPTree(translations,headerTable);

        //存储条件模式基
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();

        if(fptree == null || headerTable.size() <= 0 || translations.size() <= 0){
            return;
        }
        //从底向上寻找条件模式基
        for(int i = headerTable.size()-1; i >= 0; i--){
            TreeNode header = headerTable.get(i);
            String name;
            if(item == null){
                name = header.getName();
            } else {
                name = header.getName() + "," + item;
            }
            while (header.getNextNamesake() != null){
                header = header.getNextNamesake();
                int count = header.getCount();
                for(int n = 0; n < count; n++){
                    LinkedList<String> record = new LinkedList<String>();
                    findConditionPatternBase(header.getParent(), record);
                    if(record.size() > 0){
                        records.add(record);
                    }
                }
            }
            //递归循环找出以name为结尾的条件模式基的集合
            fpGrowth(records,miniSupport,name);
        }

        //挖掘频繁项集
        for (int i = headerTable.size()-1; i >= 0 ; i--) {
            TreeNode node = headerTable.get(i);
            if(node.getName().equals(item)){
                break;
            }
            int num = 0;
            while (node.getNextNamesake() != null){
                node = node.getNextNamesake();
                num = num + node.getCount();
            }
            if(item != null){
                System.out.println("{" + node.getName() + ","  + item + ":" + num  + "}");
            }else{
                System.out.println("{" + node.getName() + ":" + num + "}" );
            }
        }
    }

    /**
     * 获取某个项的条件模式基
     *
     * @param node
     * @param record
     */
    public void findConditionPatternBase(TreeNode node, LinkedList<String> record) {
       if(node.getParent() == null){
           return;
       }
       String name = node.getName();
       record.add(name);
       findConditionPatternBase(node.getParent(),record);
    }


    public static void main(String[] args) {
       String input = Constants.DICTIONARY_INCREMENT_FILE_PATH;
       int miniSupport = 2;
       LinkedList<LinkedList<String>> translations = LoadDataUtils.loadTransListByFilepath2(input);
       FPtree fPtree = new FPtree();
       fPtree.fpGrowth(translations,miniSupport,null);
    }
}
