package com.joe.commom_library.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joe
 * @time 8/8/2018 10:52 AM
 */
public class CheckUtil {
    /**
     * 验证邮箱
     */
    public static boolean checkEmail(String email) {
        if (email.length() > 30) {
            return false;
        }
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception ignored) {
        }
        return flag;
    }

    /**
     * 验证密码
     */
    public static boolean checkPassword(String password) {
        Pattern p = Pattern.compile("^[!-~]*$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 集合判断是否为空
     *
     * @param collection 使用泛型
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        if (collection != null) {
            Iterator<T> iterator = collection.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (next != null) {
                        return true;
                    }
                }
            }
        }
        return false;

    }
}
