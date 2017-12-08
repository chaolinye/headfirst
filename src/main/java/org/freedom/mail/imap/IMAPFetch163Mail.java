package org.freedom.mail.imap;

import com.sun.mail.imap.IMAPStore;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

public class IMAPFetch163Mail extends IMAPFetchMail {

    private static final String DEFAULT_HOST = "imap.163.com";

    public IMAPFetch163Mail(String username, String password) {
        this(DEFAULT_HOST, username, password);
    }
    public IMAPFetch163Mail(String host,String username, String password) {
        super(host, username, password);
    }

    public IMAPFetch163Mail(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    @Override
    protected void postConnect() throws MessagingException {
        // 163邮箱不遵循标准的IMAP协议，需要使用扩展的ID命令指定客户端的值
        IMAPStore imapStore = (IMAPStore) getStore();
        Map<String, String> clientParams = new HashMap<String, String>();
        clientParams.put("name", "my-imap");
        clientParams.put("version", "1.0");
        imapStore.id(clientParams);
    }
}
