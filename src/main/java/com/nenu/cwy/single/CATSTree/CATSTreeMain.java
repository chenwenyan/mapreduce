package com.nenu.cwy.single.CATSTree;


import com.nenu.cwy.common.Constants;
import com.nenu.cwy.common.LoadDataUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * CATSTree
 * Author： wychen
 * Date: 2017/10/24
 * Time: 16:41
 */
public class CATSTreeMain {


    public CATSTreeNode buildCATSTree(LinkedList<LinkedList<String>> translations) {
        //若数据集为空，则直接返回
        if (translations.size() <= 0) {
            return null;
        }
        //数据集不为空
        CATSTreeNode root = new CATSTreeNode("root");
        for (LinkedList<String> record : translations) {
            if (record.size() > 0) {
                addNode(root, record);
            }
        }
        return root;
    }

    public CATSTreeNode addNode(CATSTreeNode root, LinkedList<String> record) {
        if (record.size() <= 0) {
            return null;
        }
        CATSTreeNode newNode = new CATSTreeNode();
        String item = record.poll();
        Boolean isExist = false;
        if (root.getChildren().size() > 0) {
            List<CATSTreeNode> children = root.getChildren();
            if (children.size() > 0) {
                for (CATSTreeNode catsTreeNode : children) {
                    if (catsTreeNode.getName().equals(item)) {
                        isExist = true;
                        catsTreeNode.setCount(catsTreeNode.getCount() + 1);
                        newNode = catsTreeNode;
//                        addNode(catsTreeNode, record);
                    }

                }
            }

        } else {
            if (isExist) {
                item = record.poll();
            } else {
                newNode.setName(item);
                newNode.setCount(1);
                newNode.setParent(root);
                root.addChild(newNode);
            }
        }
        addNode(newNode,record);
        return root;
    }

    public static void main(String[] args) {

        LinkedList<LinkedList<String>> translations = LoadDataUtils.loadTransListByFilepath2(Constants.DICTIONARY_FILE_PATH);
        CATSTreeMain catsTreeMain = new CATSTreeMain();
        CATSTreeNode catsTree = catsTreeMain.buildCATSTree(translations);
        System.out.println(catsTree.getChildren().size());

    }
}
