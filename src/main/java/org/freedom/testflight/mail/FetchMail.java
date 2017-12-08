package org.freedom.testflight.mail;

import org.freedom.testflight.imap.MessageFilter;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class FetchMail {
    private final String DEFAULT_FOLDER = "INBOX";

    private Store store;
    private Folder folder;

    private String host;
    private int port;
    private String username;
    private String password;

    public FetchMail(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    protected Store getStore() {
        return store;
    }

    protected Folder getFolder() {
        return folder;
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }

    protected void connect() {
        try {
            preConnect();
            connect(getSessionProperties());
            postConnect();
            selectFolder(DEFAULT_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("connect exception", e);
        }
    }

    ;

    protected void preConnect()throws Exception{

    }

    private void connect(Properties props) throws MessagingException {
        // 创建Session实例对象
        Session session = Session.getInstance(props);
        // 创建IMAP协议的Store对象
        store = session.getStore(getProtocol());
        // 连接邮件服务器
        store.connect(username, password);
    }

    /**
     * 准备连接服务器的会话信息
     * @return
     */
    protected abstract Properties getSessionProperties();

    protected abstract String getProtocol();

    protected void postConnect() throws Exception{
    }

    private void selectFolder(String folderName)throws MessagingException {
        // 获得收件箱
        folder = store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
    }


    public List<Message> getMessages(MessageFilter messageFilter) {
        List<Message> messagesToReturn = new ArrayList<Message>();
        try {
            // 获得收件箱的邮件列表
            Message[] messages = folder.getMessages();
            // 过滤邮件
            for (Message message : messages) {
                if (messageFilter == null || messageFilter.filter(message)) {
                    messagesToReturn.add(message);
                }
            }
        } catch (MessagingException e) {
            System.out.println("getMessages exception");
            throw new RuntimeException("getMessage exception", e);
        }
        return messagesToReturn;
    }

    public void disconnect(){
        try {
            folder.close();
            store.close();
        }catch (MessagingException e){
            throw new RuntimeException("disconnect exception",e);
        }

    };
}
