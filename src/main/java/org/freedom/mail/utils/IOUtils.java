package org.freedom.mail.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {
    public static void writeUtil(String cmd, OutputStream os) {
        try {
            cmd = cmd + "\n";
            os.write(cmd.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readUntil(String endFlag, InputStream in) {

        InputStreamReader isr = new InputStreamReader(in);

        char[] charBytes = new char[1024];
        int n = 0;
        boolean flag = false;
        String str = "";
        try {
            while ((n = isr.read(charBytes)) != -1) {
                for (int i = 0; i < n; i++) {
                    char c = (char) charBytes[i];
                    str += c;
                    //当拼接的字符串以指定的字符串结尾时,不在继续读
                    if (str.endsWith(endFlag)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
