package com.joe.commom_library.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Joe on 2017/8/2.
 */

public class PersistUtil {
    private static final String gatewayDir = "";

    public static void persistData(final Serializable obj, String userName, String fileName) {
        if (null == obj) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                File files = new File(gatewayDir, userName);
                if (!files.exists()) {
                    files.mkdirs();
                }
                File file = new File(files + File.separator + fileName);
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream(file));
                    oos.writeObject(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != oos) {
                        try {
                            oos.close();
                            oos = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }

    public static Object readData(final String userName, final String fileName) {
        Object data = null;

        String filePath = gatewayDir + userName + File.separator + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(filePath));
                data = ois.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != ois) {
                    try {
                        ois.close();
                        ois = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }
}
