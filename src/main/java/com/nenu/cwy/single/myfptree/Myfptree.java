package com.nenu.cwy.single.myfptree;

import com.nenu.cwy.common.Constants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Myfptree
 * Author： wychen
 *
 * 源代码：http://www.cnblogs.com/liguangsunls/p/6892482.html
 *
 * Date: 2017/10/5
 * Time: 15:28
 */
public class Myfptree {
    public static final int support = 30; // 设定最小支持频次为2
    //保存第一次的次序
    public Map<String, Integer> ordermap = new HashMap<String, Integer>();

    public LinkedList<LinkedList<String>> readF1() throws IOException {
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();

//        String filePath = Constants.T10I4D100K;
        String filePath = Constants.DICTIONARY_FILE_PATH;
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.length() == 0 || "".equals(line))
                continue;
            String[] str = line.split(" ");
            LinkedList<String> litm = new LinkedList<String>();
            for (int i = 0; i < str.length; i++) {
                litm.add(str[i].trim());
            }
            records.add(litm);
        }
        br.close();
        return records;
    }

    //创建表头链
    public LinkedList<TreeNode2> buildHeaderLink(LinkedList<LinkedList<String>> records) {
        LinkedList<TreeNode2> header = null;
        if (records.size() > 0) {
            header = new LinkedList<TreeNode2>();
        } else {
            return null;
        }
        Map<String, TreeNode2> map = new HashMap<String, TreeNode2>();
        for (LinkedList<String> items : records) {

            for (String item : items) {
                //假设存在数量增1，不存在则新增
                if (map.containsKey(item)) {
                    map.get(item).Sum(1);
                } else {
                    TreeNode2 node = new TreeNode2();
                    node.setName(item);
                    node.setCount(1);
                    map.put(item, node);
                }
            }
        }
        // 把支持度大于（或等于）minSup的项增加到F1中
        Set<String> names = map.keySet();
        for (String name : names) {
            TreeNode2 tnode = map.get(name);
            if (tnode.getCount() >= support) {
                header.add(tnode);
            }
        }
        sort(header);
        return header;
    }

    //选择法排序,假设次数相等，则按名字排序,字典顺序,先小写后大写
    public List<TreeNode2> sort(List<TreeNode2> list) {
        int len = list.size();
        for (int i = 0; i < len; i++) {

            for (int j = i + 1; j < len; j++) {
                TreeNode2 node1 = list.get(i);
                TreeNode2 node2 = list.get(j);
                if (node1.getCount() < node2.getCount()) {
                    TreeNode2 tmp = new TreeNode2();
                    tmp = node2;
                    list.remove(j);
                    //list指定位置插入，原来的>=j元素都会往下移，不会删除,所以插入前要删除掉原来的元素
                    list.add(j, node1);
                    list.remove(i);
                    list.add(i, tmp);
                }
                //假设次数相等，则按名字排序,字典顺序,先小写后大写
                if (node1.getCount() == node2.getCount()) {
                    String name1 = node1.getName();
                    String name2 = node2.getName();
                    int flag = name1.compareTo(name2);
                    if (flag > 0) {
                        TreeNode2 tmp = new TreeNode2();
                        tmp = node2;
                        list.remove(j);
                        //list指定位置插入，原来的>=j元素都会往下移。不会删除,所以插入前要删除掉原来的元素
                        list.add(j, node1);
                        list.remove(i);
                        list.add(i, tmp);
                    }
                }
            }
        }
        return list;
    }

    //选择法排序。降序,假设同名按L 中的次序排序
    public List<String> itemsort(LinkedList<String> lis, List<TreeNode2> header) {
        //List<String> list=new ArrayList<String>();
        //选择法排序
        int len = lis.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                String key1 = lis.get(i);
                String key2 = lis.get(j);
                Integer value1 = findcountByname(key1, header);
                if (value1 == -1) continue;
                Integer value2 = findcountByname(key2, header);
                if (value2 == -1) continue;
                if (value1 < value2) {
                    String tmp = key2;
                    lis.remove(j);
                    lis.add(j, key1);
                    lis.remove(i);
                    lis.add(i, tmp);
                }
                if (value1 == value2) {
                    int v1 = ordermap.get(key1);
                    int v2 = ordermap.get(key2);
                    if (v1 > v2) {
                        String tmp = key2;
                        lis.remove(j);
                        lis.add(j, key1);
                        lis.remove(i);
                        lis.add(i, tmp);
                    }
                }
            }
        }
        return lis;
    }

    public Integer findcountByname(String itemname, List<TreeNode2> header) {
        Integer count = -1;
        for (TreeNode2 node : header) {
            if (node.getName().equals(itemname)) {
                count = node.getCount();
            }
        }
        return count;
    }

    /**
     * @param records 构建树的记录,如I1,I2,I3
     * @param header  韩书中介绍的表头
     * @return 返回构建好的树
     */
    public TreeNode2 builderFpTree(LinkedList<LinkedList<String>> records, List<TreeNode2> header) {

        TreeNode2 root;
        if (records.size() <= 0) {
            return null;
        }
        root = new TreeNode2();
        for (LinkedList<String> items : records) {
            itemsort(items, header);
            addNode(root, items, header);
        }
        return root;
    }

    //当已经有分枝存在的时候。推断新来的节点是否属于该分枝的某个节点。或所有重合，递归
    public TreeNode2 addNode(TreeNode2 root, LinkedList<String> items, List<TreeNode2> header) {
        if (items.size() <= 0)
            return null;
        String item = items.poll();
        //当前节点的孩子节点不包括该节点，那么另外创建一支分支。

        TreeNode2 node = root.findChild(item);
        if (node == null) {
            node = new TreeNode2();
            node.setName(item);
            node.setCount(1);
            node.setParent(root);
            root.addChild(node);

            //加将各个节点加到链头中
            for (TreeNode2 head : header) {
                if (head.getName().equals(item)) {
                    while (head.getNextHomonym() != null) {
                        head = head.getNextHomonym();
                    }
                    head.setNextHomonym(node);
                    break;
                }
            }
            //加将各个节点加到链头中
        } else {
            node.setCount(node.getCount() + 1);
        }

        addNode(node, items, header);
        return root;
    }

    //从叶子找到根节点。递归之
    public void toroot(TreeNode2 node, LinkedList<String> newrecord) {
        if (node.getParent() == null)
            return;
        String name = node.getName();
        newrecord.add(name);
        toroot(node.getParent(), newrecord);
    }

    //对条件FP-tree树进行组合，以求出频繁项集
    public void combineItem(TreeNode2 node, LinkedList<String> newrecord, String Item) {
        if (node.getParent() == null) return;
        String name = node.getName();
        newrecord.add(name);
        toroot(node.getParent(), newrecord);
    }

    //fp-growth
    public void fpgrowth(LinkedList<LinkedList<String>> records, String item) {
        //保存新的条件模式基的各个记录，以又一次构造FP-tree
        LinkedList<LinkedList<String>> newrecords = new LinkedList<LinkedList<String>>();
        //构建链头
        LinkedList<TreeNode2> header = buildHeaderLink(records);
        //创建FP-Tree
        TreeNode2 fptree = builderFpTree(records, header);
        //结束递归的条件
        if (header.size() <= 0 || fptree == null) {
            return;
        }
        //打印结果,输出频繁项集
        if (item != null) {
            //寻找条件模式基,从链尾開始
            for (int i = header.size() - 1; i >= 0; i--) {
                TreeNode2 head = header.get(i);
                String itemname = head.getName();
                Integer count = 0;
                while (head.getNextHomonym() != null) {
                    head = head.getNextHomonym();
                    //叶子count等于多少。就算多少条记录
                    count = count + head.getCount();
                }
                //打印频繁项集
                System.out.println(head.getName() + "," + item + "\t" + count);
            }
        }
        //寻找条件模式基,从链尾开始
        for (int i = header.size() - 1; i >= 0; i--) {
            TreeNode2 head = header.get(i);
            String itemname;
            //再组合
            if (item == null) {
                itemname = head.getName();
            } else {
                itemname = head.getName() + "," + item;
            }

            while (head.getNextHomonym() != null) {
                head = head.getNextHomonym();
                //叶子count等于多少，就算多少条记录
                Integer count = head.getCount();
                for (int n = 0; n < count; n++) {
                    LinkedList<String> record = new LinkedList<String>();
                    toroot(head.getParent(), record);
                    newrecords.add(record);
                }
            }
            //递归之,以求子FP-Tree
            fpgrowth(newrecords, itemname);
        }
    }

    //保存次序。此步也能够省略，为了降低再加工结果的麻烦而加
    public void orderF1(LinkedList<TreeNode2> orderheader) {
        for (int i = 0; i < orderheader.size(); i++) {
            TreeNode2 node = orderheader.get(i);
            ordermap.put(node.getName(), i);
        }
    }

    public static void main(String[] args) throws IOException {

        //获取开始时间
        long startTime = System.currentTimeMillis();

        //读取数据
        Myfptree fpg = new Myfptree();
        LinkedList<LinkedList<String>> records = fpg.readF1();
        LinkedList<TreeNode2> orderheader = fpg.buildHeaderLink(records);
        fpg.orderF1(orderheader);
        fpg.fpgrowth(records, null);

        //获取结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("程序运行时间:" + costTime + "ms");
    }
}
