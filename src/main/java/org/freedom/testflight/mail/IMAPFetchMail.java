package org.freedom.testflight.mail;

import java.util.Properties;

public class IMAPFetchMail extends FetchMail {

    private static final int DEFAULT_PORT = 143;
    private final String DEFAULT_PROTOCOL = "imap";

    public IMAPFetchMail(String host, String username, String password) {
        this(host, DEFAULT_PORT, username, password);
    }

    public IMAPFetchMail(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    @Override
    protected Properties getSessionProperties() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", DEFAULT_PROTOCOL);
        props.setProperty("mail.imap.host", getHost());
        props.setProperty("mail.imap.port", String.valueOf(getPort()));
        return props;
    }

    @Override
    protected String getProtocol() {
        return DEFAULT_PROTOCOL;
    }
}
