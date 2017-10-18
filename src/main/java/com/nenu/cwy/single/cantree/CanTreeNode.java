package com.nenu.cwy.single.cantree;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeNode
 * Author： wychen
 * Date: 2017/9/30
 * Time: 15:35
 */
public class CanTreeNode implements Comparable<CanTreeNode>{

    //节点名称
    private String name;
    //节点出现次数
    private Integer count;
    //父节点
    private CanTreeNode parent;
    //孩子节点
    private List<CanTreeNode> children;
    //下一个同名节点
    private CanTreeNode nextHomonym;

    public CanTreeNode(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CanTreeNode getParent() {
        return parent;
    }

    public void setParent(CanTreeNode parent) {
        this.parent = parent;
    }

    public List<CanTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<CanTreeNode> children) {
        this.children = children;
    }

    public CanTreeNode getNextHomonym() {
        return nextHomonym;
    }

    public void setNextHomonym(CanTreeNode nextHomonym) {
        this.nextHomonym = nextHomonym;
    }

    /**
     * 添加子节点
     *
     * @param child
     */
    public void addChild(CanTreeNode child){
        if(this.getChildren() == null){
            List<CanTreeNode> children = new ArrayList<CanTreeNode>();
            children.add(child);
            this.setChildren(children);
        }else{
            this.getChildren().add(child);
        }
    }

    /**
     * 根据名称获取子节点
     *
     * @param name
     * @return
     */
    public CanTreeNode findChild(String name){
        List<CanTreeNode> children = this.getChildren();
        if(children != null){
            for(CanTreeNode treeNode : children){
                //equals 只比较字符串的内容
                if(treeNode.getName().equals(name)){
                    return treeNode;
                }
            }
        }
        return null;
    }

    public CanTreeNode(String name){
        this.name = name;
    }

    public CanTreeNode(String name, Integer count){
        this.name = name;
        this.count = count;
    }

//    @Override
    public int compareTo(CanTreeNode o) {
        int count = o.getCount();
        return count - this.count;
    }
}
