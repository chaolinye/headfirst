package org.freedom.mail;

import javax.mail.Message;

public interface MessageFilter {
    boolean filter(Message message);
}
