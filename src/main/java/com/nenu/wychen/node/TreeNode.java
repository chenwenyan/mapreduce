package com.nenu.wychen.node;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeNode
 * Author： wychen
 * Date: 2017/10/26
 * Time: 8:54
 */
public class TreeNode implements Comparable<TreeNode>{

    private String name;

    private int count;

    private TreeNode parent;

    private List<TreeNode> children;

    private TreeNode nextNamesake;

    public TreeNode(){

    }

    public TreeNode(String name){
        this.name = name;
    }

    public TreeNode(String name,Integer count){
        this.name = name;
        this.count = count;
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

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getNextNamesake() {
        return nextNamesake;
    }

    public void setNextNamesake(TreeNode nextNamesake) {
        this.nextNamesake = nextNamesake;
    }

    /**
     * 支持度计数加1
     *
     * @param count
     */
    public void Sum(Integer count) {
        this.count = this.count + count;
    }

    /**
     * 添加一个孩子节点
     *
     * @param child
     */
    public void addChild(TreeNode child){
        List<TreeNode> children = new ArrayList<TreeNode>();
        if(this.getChildren() == null){
            children.add(child);
        }else{
            this.getChildren().add(child);
            children = this.getChildren();
        }
        this.setChildren(children);
    }

    /**
     * 是否存在着该孩子节点,存在返回该节点，不存在返回空
     *
     * @param name
     * @return
     */
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


    /**
     * 修改排序顺序，降序排列
     *
     * @param o
     * @return
     */
    public int compareTo(TreeNode o) {
        int count = o.getCount();
        return count - this.getCount();
    }
}
