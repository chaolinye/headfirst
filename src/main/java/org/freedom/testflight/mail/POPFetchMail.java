package org.freedom.testflight.mail;

import java.util.Properties;

public class POPFetchMail extends FetchMail {
    private static final int DEFAULT_PORT = 110;
    private final String DEFAULT_PROTOCOL = "pop3";

    public POPFetchMail(String host, String username, String password) {
        this(host, DEFAULT_PORT, username, password);
    }

    public POPFetchMail(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    @Override
    protected Properties getSessionProperties() {
        Properties props = new Properties();
        props.setProperty("testflight.store.protocol", "pop3");
        props.setProperty("testflight.pop3.host", getHost());
        props.setProperty("testflight.pop3.port", String.valueOf(getPort()));
        return props;
    }

    @Override
    protected String getProtocol() {
        return DEFAULT_PROTOCOL;
    }
}
