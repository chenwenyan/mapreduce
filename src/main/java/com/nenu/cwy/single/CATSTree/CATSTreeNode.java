package com.nenu.cwy.single.CATSTree;

import com.nenu.cwy.single.fpgrowth.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * CATSTreeNode
 * Author： wychen
 * Date: 2017/10/24
 * Time: 16:42
 */
public class CATSTreeNode {
    //数据项名称
    private String name;
    //事务数据库中出现次数
    private int count;
    //父节点
    private CATSTreeNode parent;
    //子节点
    private List<CATSTreeNode> children;
    //下一同名节点
    private TreeNode nextHomonym;

    public CATSTreeNode() {

    }

    public CATSTreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CATSTreeNode getParent() {
        return parent;
    }

    public void setParent(CATSTreeNode parent) {
        this.parent = parent;
    }

    public List<CATSTreeNode> getChildren() {
        return children;
    }

    public void addChild(CATSTreeNode child) {
        if (this.getChildren() == null) {
            List<CATSTreeNode> list = new ArrayList<CATSTreeNode>();
            list.add(child);
            this.setChildren(list);
        } else {
            this.getChildren().add(child);
        }
    }

    public CATSTreeNode findChild(String name) {
        List<CATSTreeNode> children = this.getChildren();
        if (children != null) {
            for (CATSTreeNode child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void setChildren(List<CATSTreeNode> children) {
        this.children = children;
    }

    public void printChildrenName() {
        List<CATSTreeNode> children = this.getChildren();
        if (children != null) {
            for (CATSTreeNode child : children) {
                System.out.print(child.getName() + " ");
            }
        } else {
            System.out.print("null");
        }
    }

    public TreeNode getNextHomonym() {
        return nextHomonym;
    }

    public void setNextHomonym(TreeNode nextHomonym) {
        this.nextHomonym = nextHomonym;
    }

    public void countIncrement(int n) {
        this.count += n;
    }

    public int compareTo(TreeNode arg0) {
        int count0 = arg0.getCount();
        // 跟默认的比较大小相反，导致调用Arrays.sort()时是按降序排列
        return count0 - this.count;
    }
}
