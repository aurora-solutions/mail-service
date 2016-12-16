package com.aurora.mail.util;

import com.aurora.mail.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;

import javax.inject.Inject;

/**
 * Created by Rasheed on 9/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class AbstractBaseMailTests extends AbstractJUnit4SpringContextTests {

    private final Logger log = LoggerFactory.getLogger(AbstractBaseMailTests.class);

    @Inject
    public Environment env;

    public Wiser wiser;
    private int wiserPort = 2500;
    private String wiserHostName = "localhost";

    @Before
    public void setup() {
        wiser = new Wiser();
        wiser.setPort(wiserPort);
        wiser.setHostname(wiserHostName);
        wiser.start();

        MailTestUtils.reconfigureMailSenders(applicationContext, wiserPort, wiserHostName);
    }

    @After
    public void after() {
        try {
            wiser.stop();
        } catch (Exception exception) {
            log.error("Exception while stopping wiser!", exception);
        }
    }

}
