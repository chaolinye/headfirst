package org.freedom.testflight;

import org.apache.commons.lang3.StringUtils;
import org.freedom.mail.FetchMail;
import org.freedom.mail.imap.IMAPFetch163Mail;
import org.freedom.mail.utils.MessageParseUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

public class TestFlightExtractor {
    private static final String DEFAULT_EMAIL_SUFFIX = "@163.com";

    public String extractLastTestFlightLink(String mailAccount, String mailPassword) {
        if (!mailAccount.endsWith(DEFAULT_EMAIL_SUFFIX)) {
            throw new RuntimeException("testflight not support exception");
        }
        FetchMail fetchMail = new IMAPFetch163Mail(mailAccount, mailPassword);
        fetchMail.connect();
        String testFlightLink = extractLastTestFlightLink(fetchMail);
        fetchMail.disconnect();
        return testFlightLink;
    }

    private String extractLastTestFlightLink(FetchMail fetchMail) {
        List<Message> messages = getMessageFromApple(fetchMail);
        for (int i = messages.size() - 1; i >= 0; i--) {
            String testFlightLink = extractTestFlightLink(messages.get(i));
            if (StringUtils.isNotBlank(testFlightLink)) {
                setMessageSeen(messages.get(i));
                return testFlightLink;
            }
        }
        return null;
    }

    private List<Message> getMessageFromApple(FetchMail fetchMail) {
        return fetchMail.getMessages(message -> {
            MimeMessage msg = (MimeMessage) message;
            try {
                if (!msg.getFlags().contains(Flags.Flag.SEEN) && MessageParseUtils.getFromAddress(msg).equals("no_reply@email.apple.com")) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private String extractTestFlightLink(Message message) {
        Document doc = Jsoup.parse(getMessageContent(message));
        Elements element = doc.getElementsByAttributeValueStarting("alt", "Start Testing");
        if (element.size() != 1) {
            throw new RuntimeException("getTestFlightLink exception");
        }
        return element.get(0).attr("href");
    }

    private String getMessageContent(Message message) {
        MimeMessage msg = (MimeMessage) message;
        StringBuffer content = new StringBuffer(30);
        try {
            MessageParseUtils.getMailTextContent(msg, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void setMessageSeen(Message message) {
        try {
            message.setFlag(Flags.Flag.SEEN, true);
        } catch (MessagingException e) {
            throw new RuntimeException("message set seen flag exception", e);
        }
    }
}
