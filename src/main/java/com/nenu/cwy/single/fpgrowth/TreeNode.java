package com.nenu.cwy.single.fpgrowth;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeNode
 * Author： wychen
 * Date: 2017/9/25
 * Time: 11:01
 */
public class TreeNode  implements Comparable<TreeNode>{

    //数据项名称
    private String name;
    //事务数据库中出现次数
    private int count;
    //父节点
    private TreeNode parent;
    //子节点
    private List<TreeNode> children;
    //下一同名节点
    private TreeNode nextHomonym;

    public TreeNode() {

    }

    public TreeNode(String name) {
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

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        if (this.getChildren() == null) {
            List<TreeNode> list = new ArrayList<TreeNode>();
            list.add(child);
            this.setChildren(list);
        } else {
            this.getChildren().add(child);
        }
    }

    public TreeNode findChild(String name) {
        List<TreeNode> children = this.getChildren();
        if (children != null) {
            for (TreeNode child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void printChildrenName() {
        List<TreeNode> children = this.getChildren();
        if (children != null) {
            for (TreeNode child : children) {
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

    @Override
    public int compareTo(TreeNode arg0) {
        int count0 = arg0.getCount();
        // 跟默认的比较大小相反，导致调用Arrays.sort()时是按降序排列
        return count0 - this.count;
    }

}
