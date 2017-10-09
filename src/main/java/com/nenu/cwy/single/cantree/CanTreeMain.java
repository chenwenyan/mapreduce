package com.nenu.cwy.single.cantree;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;
import com.nenu.cwy.mapreduce.cantree.CanTree;
import com.nenu.cwy.single.fpgrowth.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * CanTreeMain
 * Author： wychen
 * Date: 2017/10/2
 * Time: 11:08
 */
public class CanTreeMain {

    //测试数据集
    private static String input = Constants.DICTIONARY_FILE_PATH;
    //支持度阈值
    private static int minSupport = 10;

    /**
     * 根据字典序列构建项头表
     *
     * @param translations
     * @return
     */
    public LinkedList<CanTreeNode> buildHeaderTableByDictionary(LinkedList<LinkedList<String>> translations){
        if(translations.size() <= 0){
            return null;
        }
        LinkedList<CanTreeNode> canTreeNodeList = new LinkedList<CanTreeNode>();
        for(char letter = 'a'; letter <= 'g'; letter++){
            CanTreeNode canTreeNode = new CanTreeNode(String.valueOf(letter));
            canTreeNode.setCount(0);
            canTreeNodeList.add(canTreeNode);
        }
        return canTreeNodeList;
    }

    /**
     * 构建canTree
     *
     * @param translations
     * @param headerTable
     * @return
     */
    public  CanTreeNode buildCanTree(LinkedList<LinkedList<String>> translations, LinkedList<CanTreeNode> headerTable){
        if(translations.size() <= 0){
            return null;
        }
        //创建根节点
        CanTreeNode root = new CanTreeNode("root");
        for(LinkedList<String> record : translations){
            //按照字母序排列
            Collections.sort(record);
            addNode(root,record,headerTable);
        }
        //返回根节点，即返回一个canTree的树结构
        return root;
    }

    public CanTreeNode addNode(CanTreeNode root, LinkedList<String> record, LinkedList<CanTreeNode> headerTable){
        if(record.size() <= 0){
            return null;
        }
        //取出一行数据的第一个元素
        String item = record.poll();
        //查找根元素的子节点中是否已有该元素
        CanTreeNode node = root.findChild(item);
        //若为空，则将item作为root的子节点
        if(node == null){
           node = new CanTreeNode(item);
           node.setCount(1);
           node.setParent(root);
           root.addChild(node);

           //遍历项头表中的元素，若有元素与item相同，则查找item的下一个同名节点，
           //直到最后一个同名节点结束while循环，并把item锁对应的node加入到header的同名节点中
           for(CanTreeNode header : headerTable){
               if(header.getName().equals(item)){
//                   header.setCount(header.getCount() + 1);
                   while(header.getNextHomonym() != null){
                       header = header.getNextHomonym();
                   }
                   header.setNextHomonym(node);
                   break;
               }
           }
        }else {
            //若root中根节点中已有该元素，则直接count加1
            node.setCount(node.getCount() + 1);
        }
        //递归循环，直到record中元素全部被取出
        addNode(node, record, headerTable);
        return root;
    }

    public void canTreeGrowth(LinkedList<LinkedList<String>> translations, String item){
        //条件模式基
//        LinkedList<ConditionPatternBase> conditionPatternBases = new LinkedList<ConditionPatternBase>();
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
        //构建项头表
        LinkedList<CanTreeNode> headerTable = buildHeaderTableByDictionary(translations);
        //构建canTree
        CanTreeNode canTree = buildCanTree(translations,headerTable);
        //树为空，则直接返回
        if(canTree == null){
            return;
        }
        //从项头表尾部开始依次寻找条件模式基
        for(int i = headerTable.size()-1; i >= 0; i--){
            CanTreeNode header = headerTable.get(i);
            String name;
            if(item == null){
                name = header.getName();
            }else{
                name = header.getName() + ',' + item;
            }

            while(header.getNextHomonym() != null){
                header = header.getNextHomonym();
                int count = header.getCount();
                //当出现次数大于或者等于支持度阈值，查找频繁项集
//                if(count > minSupport){
                    for(int n = 0; n < count; n++){
                        LinkedList<String> record = new LinkedList<String>();
                        findRootByLeaf(header.getParent(),record);
//                        ConditionPatternBase conditionPatternBase = new ConditionPatternBase(record,count);
//                        System.out.println("[" + conditionPatternBase.getValue() + "]" + ":" + conditionPatternBase.getCount());
//                        conditionPatternBases.add(conditionPatternBase);
                        records.add(record);
//                    }
                }
            }
            //递归
            canTreeGrowth(translations, name);
        }
    }

    /**
     * 从底向上查找父节点，直到没有父节点为止
     *
     * @param node
     * @param record
     */
    public void findRootByLeaf(CanTreeNode node, LinkedList<String> record){
        if(node.getParent() == null){
            return;
        }
        String name = node.getName();
        record.add(name);
        //递归向上查找
        findRootByLeaf(node.getParent(), record);
    }


    public static void main(String[] args) {

        //扫描数据库 获取事务集合
        LinkedList<LinkedList<String>> translations = LoadDataUtils.loadTransListByFilepath2(input);
        if(!translations.isEmpty()){
            CanTreeMain canTreeMain = new CanTreeMain();
            canTreeMain.canTreeGrowth(translations,null);
        }else{
            System.out.println("数据集为空！");
        }

    }

}
