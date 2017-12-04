package com.nenu.homework.decision;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyID3
 * Author： wychen
 * Date: 2017/12/2
 * Time: 20:03
 */

public class MyID3 {

    private static LinkedList<String> attribute = new LinkedList<String>(); // 存储属性的名称
    private static LinkedList<ArrayList<String>> attributevalue = new LinkedList<ArrayList<String>>(); // 存储每个属性的取值
    private static LinkedList<String[]> data = new LinkedList<String[]>();// 原始数据

    public static final String patternString = "@attribute(.*)[{](.*?)[}]";
    public static String[] yesNo;
    public static TreeNode root;

    /**
     * 读取arff文件，给attribute、attributevalue、data赋值
     *
     * @param file  文件路径
     */
    public void readARFF(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            Pattern pattern = Pattern.compile(patternString);
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@decision")) {
                    line = br.readLine();
                    if (line == "")
                        continue;
                    yesNo = line.split(",");
                }
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    attribute.add(matcher.group(1).trim());
                    String[] values = matcher.group(2).split(",");
                    ArrayList<String> al = new ArrayList<String>(values.length);
                    for (String value : values) {
                        al.add(value.trim());
                    }
                    attributevalue.add(al);
                } else if (line.startsWith("@data")) {
                    while ((line = br.readLine()) != null) {
                        if (line == "")
                            continue;
                        String[] row = line.split(",");
                        data.add(row);
                    }
                } else {
                    continue;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param lines 传入要分析的数据集
     * @param index attribute的index
     */
    public Double getGain(LinkedList<String[]> lines, int index) {
        Double gain = -1.0;
        List<Double> li = new ArrayList<Double>();
        //统计Yes No的次数
        for (int i = 0; i < yesNo.length; i++) {
            Double sum = 0.0;
            for (int j = 0; j < lines.size(); j++) {
                String[] line = lines.get(j);
                //data为结构化数据,如果数据最后一列==yes,sum+1
                if (line[line.length - 1].equals(yesNo[i])) {
                    sum = sum + 1;
                }
            }
            li.add(sum);
        }
        //计算Entropy(S)计算Entropy(S) 见参考书《机器学习 》Tom.Mitchell著  第3.4.1.2节
        Double entropyS = TheMath.getEntropy(lines.size(), li);
        //下面计算gain

        List<String> la = attributevalue.get(index);
        List<Point> lasv = new ArrayList<Point>();
        for (int n = 0; n < la.size(); n++) {
            String attvalue = la.get(n);
            //统计Yes No的次数
            List<Double> lisub = new ArrayList<Double>();//如：sunny 是yes时发生的次数，是no发生的次数
            Double Sv = 0.0;//公式3.4中的Sv 见参考书《机器学习(Tom.Mitchell著)》
            for (int i = 0; i < yesNo.length; i++) {
                Double sum = 0.0;
                for (int j = 0; j < lines.size(); j++) {
                    String[] line = lines.get(j);
                    //data为结构化数据,如果数据最后一列==yes,sum+1
                    if (line[index].equals(attvalue) && line[line.length - 1].equals(yesNo[i])) {
                        sum = sum + 1;
                    }
                }
                Sv = Sv + sum;//计算总数
                lisub.add(sum);
            }
            //计算Entropy(S) 见参考书《机器学习(Tom.Mitchell著)》
            Double entropySv = TheMath.getEntropy(Sv.intValue(), lisub);
            //
            Point p = new Point();
            p.setSv(Sv);
            p.setEntropySv(entropySv);
            lasv.add(p);
        }
        gain = TheMath.getGain(entropyS, lines.size(), lasv);
        return gain;
    }

    //寻找最大的信息增益,将最大的属性定为当前节点，并返回该属性所在list的位置和gain值
    public Maxgain getMaxGain(LinkedList<String[]> lines) {
        if (lines == null || lines.size() <= 0) {
            return null;
        }
        Maxgain maxgain = new Maxgain();
        Double maxvalue = 0.0;
        int maxindex = -1;
        for (int i = 0; i < attribute.size(); i++) {
            Double tmp = getGain(lines, i);
            if (maxvalue < tmp) {
                maxvalue = tmp;
                maxindex = i;
            }
        }
        maxgain.setMaxgain(maxvalue);
        maxgain.setMaxindex(maxindex);
        return maxgain;
    }

    //剪取数组
    public LinkedList<String[]> filterLines(LinkedList<String[]> lines, String attvalue, int index) {
        LinkedList<String[]> newlines = new LinkedList<String[]>();
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i);
            if (line[index].equals(attvalue)) {
                newlines.add(line);
            }
        }

        return newlines;
    }

    public void createDTree() {
        root = new TreeNode();
        Maxgain maxgain = getMaxGain(data);
        if (maxgain == null) {
            System.out.println("没有数据集，请检查!");
        }
        int maxKey = maxgain.getMaxindex();
        String nodename = attribute.get(maxKey);
        root.setName(nodename);
        root.setLiatts(attributevalue.get(maxKey));
        insertNode(data, root, maxKey);
    }

    /**
     * @param lines 传入的数据集，作为新的递归数据集
     * @param node  深入此节点
     * @param index 属性位置
     */
    public void insertNode(LinkedList<String[]> lines, TreeNode node, int index) {
        List<String> liatts = node.getLiatts();
        for (int i = 0; i < liatts.size(); i++) {
            String attname = liatts.get(i);
            LinkedList<String[]> newlines = filterLines(lines, attname, index);
            if (newlines.size() <= 0) {
                System.out.println("出现异常，循环结束");
                return;
            }
            Maxgain maxgain = getMaxGain(newlines);
            double gain = maxgain.getMaxgain();
            Integer maxKey = maxgain.getMaxindex();
            //不等于0继续递归，等于0说明是叶子节点，结束递归。
            if (gain != 0) {
                TreeNode subnode = new TreeNode();
                subnode.setParent(node);
                subnode.setFatherAttribute(attname);
                String nodename = attribute.get(maxKey);
                subnode.setName(nodename);
                subnode.setLiatts(attributevalue.get(maxKey));
                node.addChild(subnode);
                //不等于0，继续递归
                insertNode(newlines, subnode, maxKey);
            } else {
                TreeNode subnode = new TreeNode();
                subnode.setParent(node);
                subnode.setFatherAttribute(attname);
                //叶子节点是yes还是no?取新行中最后一个必是其名称,因为只有完全是yes,或完全是no的情况下才会是叶子节点
                String[] line = newlines.get(0);
                String nodename = line[line.length - 1];
                subnode.setName(nodename);
                node.addChild(subnode);
            }
        }
    }

    //输出决策树
    public void printDTree(TreeNode node) {
        if (node.getChildren() == null) {
            System.out.println("--" + node.getName());
            return;
        }
        System.out.println(node.getName());
        List<TreeNode> childs = node.getChildren();
        for (int i = 0; i < childs.size(); i++) {
            System.out.println(childs.get(i).getFatherAttribute());
            printDTree(childs.get(i));
        }
    }

    public static void main(String[] args) {

        //项目路径
        String USER_DIR = System.getProperty("user.dir");
        //数据源根路径
        String DATA_ROOT_PATH = USER_DIR + "/src/main/java/com/nenu/";

        MyID3 myid3 = new MyID3();
        myid3.readARFF(new File(DATA_ROOT_PATH + "datafile/decisiontree/test/in/weather.nominal.arff"));
        myid3.createDTree();
        myid3.printDTree(root);
    }
}
