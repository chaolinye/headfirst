package org.freedom.testflight.james;

import org.apache.commons.net.telnet.TelnetClient;
import org.freedom.testflight.bean.MailUser;
import org.freedom.testflight.constants.MailConstants;
import org.freedom.testflight.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JamesManager implements MailServerManager {
    private String account;
    private String password;
    private String host;
    private int port;
    private InputStream inputStream;
    private OutputStream outputStream;

    public JamesManager(String account, String password, String host) {
        this(account,password,host,MailConstants.JAMES_DEFAULT_PROT);
    }

    public JamesManager(String account, String password, String host, int port) {
        this.account = account;
        this.password = password;
        this.host = host;
        this.port = port;
        init();
    }

    private void init(){
        connect();
        login();
    }

    private void connect() {
        try {
            TelnetClient tc = new TelnetClient();
            tc.connect(host, port);
            inputStream = tc.getInputStream();
            outputStream = tc.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(){
        gotoLoginFlag();
        exec(account);
        exec(password);
    }

    private void gotoLoginFlag(){
        System.out.println(IOUtils.readUntil(MailConstants.LOING_FLAG, inputStream));
    }

    public boolean addUser(String userAccount, String userPassword) {
        String cmd = "adduser "+userAccount+" "+userPassword;
        exec(cmd);
        return true;
    }

    public boolean deleteUser(String userAccount) {
        return false;
    }

    public List<MailUser> listUsers() {
        return null;
    }

    public int countUsers() {
        return 0;
    }

    private String exec(String cmd){
        IOUtils.writeUtil(cmd,outputStream);
        String result = IOUtils.readUntil(MailConstants.END_FLAG,inputStream);
        System.out.println(result);
        return result;
    }
}
