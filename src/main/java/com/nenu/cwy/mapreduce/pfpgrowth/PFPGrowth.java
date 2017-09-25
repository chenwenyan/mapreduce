package com.nenu.cwy.mapreduce.pfpgrowth;

import com.nenu.cwy.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.apache.mahout.common.iterator.StringRecordIterator;
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextStatusUpdater;
import org.apache.mahout.fpm.pfpgrowth.convertors.SequenceFileOutputCollector;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.StringOutputConverter;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPGrowth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PFPGrowth
 * Author： wychen
 * Date: 2017/9/25
 * Time: 20:37
 */
public class PFPGrowth {
    public static void main(String[] args) throws IOException{

        //获取当前时间
        long startTime = System.currentTimeMillis();
        //只存储不重复的项
        Set<String> features = new HashSet<String>();
        //支持度阈值
        int minSupport = 10;
        //top-k  大根堆的大小
        int maxHeapSize = 50;
        //测试数据路径
        String input = Constants.T10I4D100K;

        String pattern = " \"[ ,\\t]*[,|\\t][ ,\\t]*\" ";//do not know why
        Charset encoding = Charset.forName("utf8");//设置编码格式
        FPGrowth<String> fp = new FPGrowth<String>();
        String output = Constants.PFPGROWTH_RESULT_PATH; //输出结果文件
        Path path = new Path(output);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf); //通过配置文件获取一个FileSystem实例

        SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);
        fp.generateTopKFrequentPatterns(
                new StringRecordIterator(
                        new FileLineIterable(
                                new File(input), encoding, false),
                        pattern),
                fp.generateFList(new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern),
                        minSupport),
                minSupport,
                maxHeapSize,
                features,
                new StringOutputConverter(new SequenceFileOutputCollector<Text, TopKStringPatterns>(writer)),
                new ContextStatusUpdater(null));
        writer.close();

        List<Pair<String, TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern(conf, path);
        for (Pair<String, TopKStringPatterns> entry : frequentPatterns) {
//            System.out.println(entry.getFirst());
            System.out.println(entry.getSecond());
//            System.out.println(entry.toString());
        }
        System.out.println("\n end......");

        //获取结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("程序运行时间:"+ costTime + "ms");

        File resultPath = new File(Constants.PFPGROWTH_RESULT_PATH);
        try{
            if(!resultPath.exists()){
                resultPath.createNewFile();
            }
            FileWriter fw = new FileWriter(resultPath,true);
            PrintWriter printWriter = new PrintWriter(fw);
            printWriter.println("测试数据：" + input);
            printWriter.println("支持度阈值：" + minSupport);
            printWriter.println("程序运行时间:"+ costTime + "ms");
            printWriter.println("\n");
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
