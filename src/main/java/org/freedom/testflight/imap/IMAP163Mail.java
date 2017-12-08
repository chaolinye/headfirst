package org.freedom.testflight.imap;

import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import java.util.*;

public class IMAP163Mail implements IMAPMail {
    private static final String DEFAULT_PROTOCOL="imap";
    private static final String DEFAULT_HOST = "imap.163.com";
    private static final int DEFAULT_PORT =143;
    private String host;
    private int port;

    private String username;
    private String password;

    private Store store;
    private Folder folder;

    public IMAP163Mail(String username, String password) {
        this(DEFAULT_HOST,DEFAULT_PORT,username,password);
    }

    public IMAP163Mail(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public List<Message> getMessages(MessageFilter messageFilter) {
        List<Message> messagesToReturn=new ArrayList<Message>();
        try {
            // 获得收件箱的邮件列表
            Message[] messages = folder.getMessages();
            // 过滤邮件
            for (Message message : messages) {
                if (messageFilter==null||messageFilter.filter(message)) {
                    messagesToReturn.add(message);
                }
            }
        }catch (MessagingException e){
            System.out.println("getMessages exception");
            throw new RuntimeException("getMessage exception",e);
        }
        return messagesToReturn;
    }

    public void connect() {
        try {
            // 准备连接服务器的会话信息
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", DEFAULT_PROTOCOL);
            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", String.valueOf(port));
            // 创建Session实例对象
            Session session = Session.getInstance(props);
            // 创建IMAP协议的Store对象
            store = session.getStore(DEFAULT_PROTOCOL);
            // 连接邮件服务器
            store.connect(username, password);
            IMAPStore imapStore = (IMAPStore) store;
            // 163邮箱不遵循标准的IMAP协议，需要使用扩展的ID命令指定客户端的值
            Map<String, String> clientParams = new HashMap<String, String>();
            clientParams.put("name", "my-imap");
            clientParams.put("version", "1.0");
            imapStore.id(clientParams);
            // 获得收件箱
            folder = store.getFolder("INBOX");

            folder.open(Folder.READ_WRITE);
        }catch (MessagingException e){
            System.out.println("connect exception");
            throw new RuntimeException("connect exception",e);
        }
    }

    public void disconnect() {
        try {
            folder.close(false);
            store.close();
        } catch (MessagingException e){
            System.out.println("disconnect exception");
            throw new RuntimeException("disconnect exception",e);
        }
    }
}
