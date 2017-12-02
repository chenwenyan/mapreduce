package com.nenu.wychen.canTree;

import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;
import com.nenu.wychen.node.TreeNode;
import sun.reflect.generics.tree.Tree;

import java.awt.*;
import java.util.LinkedList;

/**
 * CanTree
 * Top down方式挖掘频繁模式
 *
 * Author： wychen
 * Date: 2017/10/26
 * Time: 14:59
 */
public class CanTree {


    public LinkedList<TreeNode> buildHeadTable() {
        LinkedList<TreeNode> headTable = new LinkedList<TreeNode>();
        for (char i = 'a'; i <= 'z'; i++) {
            TreeNode node = new TreeNode(String.valueOf(i), 0);
            headTable.add(node);
        }
        return headTable;
    }

    public TreeNode buildCanTree(LinkedList<LinkedList<String>> translations,
                             LinkedList<TreeNode> headTable){
        TreeNode root = new TreeNode("root", 0);
        for(LinkedList<String> record : translations){
            if(record.size() > 0){
                addCanNode(root, record, headTable);
            }
        }
        return root;
    }

    private TreeNode addCanNode(TreeNode root, LinkedList<String> record, LinkedList<TreeNode> headTable) {
        if(record.size() <= 0){
            return null;
        }
        String item = record.poll();
        TreeNode node = root.findChild(item);
        if(node == null){
            node = new TreeNode(item,1);
            node.setParent(root);
            root.addChild(node);

            for(TreeNode head : headTable){
                if(head.getName().equals(item)){
                    head.Sum(1);
                    while(head.getNextNamesake() != null){
                        head = head.getNextNamesake();
                    }
                    head.setNextNamesake(node);
                    break;
                }
            }
        } else {
            node.Sum(1);
        }
        addCanNode(node, record, headTable);
        return root;
    }

    public void canTreeGrowth(LinkedList<LinkedList<String>> translations,
                              int miniSupport){
        LinkedList<TreeNode> headTable = buildHeadTable();

        TreeNode canTree = buildCanTree(translations,headTable);

        if(canTree == null){
            return;
        }

        for(int i = 0 ; i <= headTable.size() - 1; i++ ){
            TreeNode head = headTable.get(i);
            if(head.getCount() >= miniSupport){
                System.out.println( "{" + head.getName() + ":" + head.getCount() + "}");
                //标记是否只是在树结构的第一层出现
                Boolean tag = false;
                if(head.getNextNamesake() != null){
                    while (head.getNextNamesake() != null){
                        head = head.getNextNamesake();
                        if(head.getParent().getName() == "root"){
                            tag = true;
                            if(head.getCount() < head.getParent().getCount()){
                                //修改该节点的父节点的计数
                                head.getParent().setCount(head.getCount());
                                toRootByLeaf(head.getParent());
                            }
                        }
                    }


                }

            }
        }

    }

    public void toRootByLeaf(TreeNode node){
        if(node.getParent() == null){
            return;
        }
//        record.add(node.getName());
        node.getParent().setCount(node.getChildren().size());
        toRootByLeaf(node.getParent());
    }


    public static void main(String[] args) {
        String input = Constants.DICTIONARY_INCREMENT_FILE_PATH;
        int miniSupport = 2;
        LinkedList<LinkedList<String>> translations = LoadDataUtils.loadTransListByFilepath2(input);
        CanTree cantree = new CanTree();
        cantree.canTreeGrowth(translations,miniSupport);
    }


}
