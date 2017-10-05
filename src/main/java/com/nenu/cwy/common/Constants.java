package com.nenu.cwy.common;

/**
 * Constants
 * Author： wychen
 * Date: 2017/9/25
 * Time: 14:26
 */
public final class Constants {

    //项目路径
    public static final String USER_DIR = System.getProperty("user.dir");
    //数据源根路径
    public static final String DATA_ROOT_PATH = USER_DIR + "/src/main/java/com/nenu/cwy/dataset/";

    //具体数据源路径
    public static final String T10I4D100K = DATA_ROOT_PATH + "input/T10I4D100K.dat";
    public static final String T40I10D100K = DATA_ROOT_PATH + "input/T40I10D100K.dat";
    public static final String WORD_COUNT_FILE_PATH = DATA_ROOT_PATH + "input/wordCount.txt";
    public static final String DICTIONARY_FILE_PATH = DATA_ROOT_PATH + "input/dictionary.txt";

    //输出结果路径
    public static final String PFPGROWTH_RESULT_PATH = DATA_ROOT_PATH + "output/PFPResult.txt";
    public static final String FPGROWTH_RESULT_PATH = DATA_ROOT_PATH + "output/FPResult.txt";
    public static final String CANTREE_RESULT_PATH = DATA_ROOT_PATH + "output/cantree_output.txt";


    public static final String WORD_COUNT_FILE_OUT_PATH = DATA_ROOT_PATH + "output/wordCountResult.txt";

}
