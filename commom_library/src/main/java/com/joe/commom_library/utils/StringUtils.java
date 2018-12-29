package com.joe.commom_library.utils;

/**
 * @author Joe
 * @time 8/17/2018 6:00 PM
 */
public class StringUtils {
    public static String creatAcacheKey(Object... param) {
        String key = "";
        for (Object o : param) {
            key += "-" + o;
        }
        return key.replaceFirst("-", "");
    }

    /**
     * 格式化小说内容。
     * <p/>
     * <li>小说的开头，缩进2格。在开始位置，加入2格空格。
     * <li>所有的段落，缩进2格。所有的\n,替换为2格空格。
     *
     * @param str
     * @return
     */
    public static String formatContent(String str) {
        str = str.replaceAll("[ ]*", "");//替换来自服务器上的，特殊空格
        str = str.replaceAll("[ ]*", "");//
        str = str.replace("\n\n", "\n");
        str = str.replace("\n", "\n" + getTwoSpaces());
        str = getTwoSpaces() + str;
//        str = convertToSBC(str);
        return str;
    }

    /**
     * Return a String that only has two spaces.
     *
     * @return
     */
    public static String getTwoSpaces() {
        return "\u3000\u3000";
    }
}
