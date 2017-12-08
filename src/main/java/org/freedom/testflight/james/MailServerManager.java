package org.freedom.testflight.james;

import org.freedom.testflight.bean.MailUser;

import java.util.List;

public interface MailServerManager {

    boolean addUser(String userAccount,String userPassword);

    boolean deleteUser(String userAccount);

    List<MailUser> listUsers();

    int countUsers();

}
