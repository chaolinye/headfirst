package org.freedom.testflight.imap;

import javax.mail.Message;

public interface MessageFilter {
    boolean filter(Message message);
}
