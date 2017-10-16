package com.nenu.cwy.single.cantree;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;

import java.util.Collections;
import java.util.LinkedList;

/**
 * CanTreeGrowth
 * Author： wychen
 * Date: 2017/10/16
 * Time: 20:48
 */
public class CanTreeGrowth {

    //测试数据集
    private static String input = Constants.DICTIONARY_FILE_PATH;
    //支持度阈值
    private static int minSupport = 2;

    /**
     * 根据字典序列构建项头表
     *
     * @param translations
     * @return
     */
    public LinkedList<CanTreeNode> buildHeaderTableByDictionary(LinkedList<LinkedList<String>> translations) {
        if (translations.size() <= 0) {
            return null;
        }
        LinkedList<CanTreeNode> canTreeNodeList = new LinkedList<CanTreeNode>();
        for (char letter = 'a'; letter <= 'g'; letter++) {
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
    public CanTreeNode buildCanTree(LinkedList<LinkedList<String>> translations, LinkedList<CanTreeNode> headerTable) {
        if (translations.size() <= 0) {
            return null;
        }
        //创建根节点
        CanTreeNode root = new CanTreeNode("root");
        for (LinkedList<String> record : translations) {
            //按照字母序排列
            Collections.sort(record);
            addNode(root, record, headerTable);
        }
        //返回根节点，即返回一个canTree的树结构
        return root;
    }

    public CanTreeNode addNode(CanTreeNode root, LinkedList<String> record, LinkedList<CanTreeNode> headerTable) {
        if (record.size() <= 0) {
            return null;
        }
        //取出一行数据的第一个元素
        String item = record.poll();
        //查找根元素的子节点中是否已有该元素
        CanTreeNode node = root.findChild(item);
        //若为空，则将item作为root的子节点
        if (node == null) {
            node = new CanTreeNode(item);
            node.setCount(1);
            node.setParent(root);
            root.addChild(node);

            //遍历项头表中的元素，若有元素与item相同，则查找item的下一个同名节点，
            //直到最后一个同名节点结束while循环，并把item锁对应的node加入到header的同名节点中
            for (CanTreeNode header : headerTable) {
                if (header.getName().equals(item)) {
                    while (header.getNextHomonym() != null) {
                        header = header.getNextHomonym();
                    }
                    header.setNextHomonym(node);
                    break;
                }
            }
        } else {
            //若root中根节点中已有该元素，则直接count加1
            node.setCount(node.getCount() + 1);
        }
        //递归循环，直到record中元素全部被取出
        addNode(node, record, headerTable);
        return root;
    }

    /**
     * 第一次挖掘频繁模式集
     *
     * @param translations
     * @param item
     */
    public void canTreeGrowth(LinkedList<LinkedList<String>> translations, String item) {
        //条件模式基
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
        //构建项头表
        LinkedList<CanTreeNode> headerTable = buildHeaderTableByDictionary(translations);
        //构建canTree
        CanTreeNode canTree = buildCanTree(translations, headerTable);

        //树为空，则直接返回
        if (canTree == null) {
            return;
        }
        //从项头表尾部开始依次寻找条件模式基
        for (int i = headerTable.size() - 1; i >= 0; i--) {
            CanTreeNode header = headerTable.get(i);
            String name;
            if (item == null) {
                name = header.getName();
            } else {
                name = header.getName() + ',' + item;
            }
            while (header.getNextHomonym() != null) {
                header = header.getNextHomonym();
                Integer count = header.getCount();
                for (int n = 0; n < count; n++) {
                    LinkedList<String> record = new LinkedList<String>();
                    findRootByLeaf(header.getParent(), record);
                    records.add(record);
                }
            }
            //递归
            canTreeGrowth(records, name);
        }

        //输出频繁项集
        if (item != null) {
            for (int i = headerTable.size() - 1; i >= 0; i--) {
                CanTreeNode header = headerTable.get(i);
                Integer count = 0;
                while (header.getNextHomonym() != null) {
                    header = header.getNextHomonym();
                    //叶子count等于多少 就算多少条记录
                    count = count + header.getCount();
                }
                if(count >= minSupport){
                    System.out.println("[" + header.getName() + "," + item + "]" + count);
                }
            }
        }
    }

    /**
     * 从底向上查找父节点，直到没有父节点为止
     *
     * @param node
     * @param record
     */
    public void findRootByLeaf(CanTreeNode node, LinkedList<String> record) {
        if (node.getParent() == null) {
            return;
        }
        String name = node.getName();
        record.add(name);
        //递归向上查找
        findRootByLeaf(node.getParent(), record);
    }

    public static void main(String[] args) {

        //获取开始时间
        long startTime = System.currentTimeMillis();

        //扫描数据库 获取事务集合
        LinkedList<LinkedList<String>> translations = LoadDataUtils.loadTransListByFilepath2(input);
        if (!translations.isEmpty()) {
            CanTreeGrowth canTreeGrowth = new CanTreeGrowth();
            canTreeGrowth.canTreeGrowth(translations, null);
        } else {
            System.out.println("数据集为空！");
        }

        //获取结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("程序运行时间:" + costTime + "ms");
    }

}
