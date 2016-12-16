package com.aurora.mail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

/**
 * Created by Rasheed on 9/13/14.
 *
 * A util class where we call a method which finds all the Spring-managed classes which are JavaMailSenderImpl and
 * reconfigures them to use the same host and port that we set in the junit test setup method.
 */
public class MailTestUtils {

    private static final Logger log = LoggerFactory.getLogger(MailTestUtils.class);

    public static void reconfigureMailSenders(ApplicationContext applicationContext, int port, String hostName) {
        Map<String, JavaMailSenderImpl> ofType =
                applicationContext.getBeansOfType(org.springframework.mail.javamail.JavaMailSenderImpl.class);

        for (Map.Entry<String, JavaMailSenderImpl> bean : ofType.entrySet()) {
            log.info(String.format("configuring mailsender %s to use local Wiser SMTP", bean.getKey()));
            JavaMailSenderImpl mailSender = bean.getValue();
            mailSender.setHost(hostName);
            mailSender.setPort(port);
        }
    }
}
