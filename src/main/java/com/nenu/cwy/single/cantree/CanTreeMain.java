package com.nenu.cwy.single.cantree;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;
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

    /**
     * 每行记录按照字母序列排序
     *
     * @param record
     */
    public LinkedList<String> sortByDictionary(List<String> record){
        LinkedList<String> result = null;
        if(record.size() > 0){
            Collections.sort(record);
        }
        return result;
    }

    /**
     * 构建Cantree
     *
     * @param translations
     * @return
     */
    public CanTreeNode buildCanTree(List<List<String>> translations){
        CanTreeNode rootNode = new CanTreeNode();

        for (List<String> recordList : translations){
            LinkedList<String>  record = sortByDictionary(recordList);
            CanTreeNode subTreeNode = rootNode;
            CanTreeNode tmpRoot = null;
            if(rootNode.getChildren() != null){
                while(!record.isEmpty() && (tmpRoot = subTreeNode.findChild(record.peek())) != null){
                    tmpRoot.setCount(tmpRoot.getCount() + 1);
                    subTreeNode = tmpRoot;
                    record.poll();
                }
            }
            addNodes(subTreeNode, record);
        }
        return rootNode;
    }

    /**
     * 添加节点
     *
     * @param parent
     * @param orderedRecord
     */
    public void addNodes(CanTreeNode parent, LinkedList<String> orderedRecord){
       if(orderedRecord.size() > 0){
           while(orderedRecord.size() > 0){
             String item = orderedRecord.poll();
             CanTreeNode leafNode = new CanTreeNode(item);
             leafNode.setCount(1);
             leafNode.setParent(parent);
             parent.addChild(leafNode);

             //TODO:下一个同名节点设置

             addNodes(leafNode,orderedRecord);

           }
       }
    }

    public void canTreeGrowth(List<List<String>> translations, List<String> postPattern){
        //构建CanTree
        CanTreeNode treeRoot = buildCanTree(translations);
        //若根节点子节点为空，则返回空
        if(treeRoot.getChildren() == null || treeRoot.getChildren().size() == 0){
            return;
        }else{
            //TODO:频繁模式集
            for(char a = 'a'; a < 'z'; a++){
                CanTreeNode canTreeNode = new CanTreeNode(String.valueOf(a));
                canTreeNode.setCount(1);
                //后缀模式基增加一项
                List<String> newPostPattern = new LinkedList<String>();
                newPostPattern.add(String.valueOf(a));
                if(postPattern != null){
                    newPostPattern.addAll(postPattern);
                }
                //查询字符的条件模式基，放入到newTransRecords中
                List<List<String>> newTransRecords = new LinkedList<List<String>>();
                CanTreeNode backnode = canTreeNode.getNextHomonym();
                while (backnode != null) {
                    int counter = backnode.getCount();
                    List<String> prenodes = new ArrayList<String>();
                    CanTreeNode parent = backnode;
                    // 遍历backnode的祖先节点，放到prenodes中
                    while ((parent = parent.getParent()).getName() != null) {
                        prenodes.add(parent.getName());
                    }
                    while (counter-- > 0) {
                        newTransRecords.add(prenodes);
                    }
                    backnode = backnode.getNextHomonym();
                }
                //递归
                canTreeGrowth(newTransRecords, newPostPattern);
            }
        }

    }



    public static void main(String[] args) {
        int minSupport = 2;
        //扫描数据库 获取事务集合
        List<List<String>> translations = LoadDataUtils.loadTransListByFilepath(input);
        if(!translations.isEmpty()){
            CanTreeMain canTreeMain = new CanTreeMain();
            canTreeMain.canTreeGrowth(translations,null);

        }else{
            System.out.println("数据集为空！");
        }

    }

}
