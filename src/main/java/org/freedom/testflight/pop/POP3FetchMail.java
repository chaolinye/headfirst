package org.freedom.testflight.pop;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class POP3FetchMail {
    public static void main(String[] args) {
        String protocol = "pop3";
        String pop3Server = "pop3.163.com";
        String username = "xyang0917@163.com";
        String password = "123456abc";

        // 创建一个有具体连接信息的Properties对象
        Properties properties = new Properties();
        properties.put("testflight.store.protocol", protocol);
        properties.put("testflight.pop3.host", pop3Server);

        // 使用Properties对象获得Session对象
        Session session = Session.getInstance(properties);
        session.setDebug(false);
        try {
            Store store = session.getStore(protocol);
            store.connect(pop3Server, username, password);

//获取收件箱
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);  //以只读权限打开收件箱
/**
 * Folder.READ_ONLY：表示只读权限。
 * Folder.READ_WRITE：表示可以修改并读取邮件夹中的邮件。
 **/

//获取收件箱中的邮件
            Message[] messages = folder.getMessages();
            System.out.println("总的邮件数目：" + messages.length);
            System.out.println("新邮件数目：" + folder.getNewMessageCount());
            System.out.println("未读邮件数目：" + folder.getUnreadMessageCount());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
