package org.freedom.testflight.imap;

import javax.mail.Message;
import java.util.List;

public interface IMAPMail {

    void connect();

    List<Message> getMessages(MessageFilter messageFilter);

    void disconnect();
}
