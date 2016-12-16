/***************************************************************************** 
* Copyright 2016 Aurora Solutions 
* 
*    http://www.aurorasolutions.io 
* 
* Aurora Solutions is an innovative services and product company at 
* the forefront of the software industry, with processes and practices 
* involving Domain Driven Design(DDD), Agile methodologies to build 
* scalable, secure, reliable and high performance products.
* 
* The Mail Service exposes a RESTful API, developed with Spring Boot,
* for sending emails. The service is configurable via a properties file
* to allow for customizing configuration including SMTP mail properties.
* The project also leverages Thymeleaf Template enigne for defining
* HTML email templates. The API is documented using Swagger, and easily
* viewable from the web browser using Swagger UI. Other frameworks used
* in the project are Flying Saucer, logback, Mockito, JUnit and Wiser.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at 
* 
*    http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
*****************************************************************************/


package com.aurora.mail.service;

import com.aurora.mail.util.UserBeanStub;
import com.aurora.mail.Application;
import com.aurora.mail.beans.UserBean;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import com.aurora.mail.repository.UserRepository;
import com.aurora.mail.util.AbstractBaseMailTests;
import com.aurora.mail.util.CustomerAccessBeanStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.WiserMessage;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Aamir on 15/09/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SystemUserCustomMailServiceTests extends AbstractBaseMailTests {

    private final Logger log = LoggerFactory.getLogger(SystemUserCustomMailServiceTests.class);

    @Inject
    @InjectMocks
    private SystemUserCustomMailService systemUserCustomMailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendCustomMailToUser() {
        when(userRepository.findById(UserBeanStub.USER_ID)).thenReturn(UserBeanStub.createUserBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
        systemUserCustomMailService.sendCustomMailToSystemUser(UserBeanStub.USER_ID, UserBeanStub.CUSTOM_SUBJECT, UserBeanStub.CUSTOM_MESSAGE);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
                final String subject = UserBeanStub.CUSTOM_SUBJECT;
                assertEquals("'Subject' did not match", subject, msg.getSubject());
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());
                assertEquals("'To' address contains more than one recipents", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", UserBeanStub.USER_A_EMAIL, msg.getAllRecipients()[0].toString());
                final String messageContent=UserBeanStub.CUSTOM_MESSAGE;
                assertEquals("'Content' did not match",true,msg.getContent().toString().contains(messageContent));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {0}", exception));
        }
    }

    @Test
    public void testSendCustomMailToMultipleUsers() {
        when(userRepository.findByPowerLevel(UserBeanStub.USER_POWER_LEVEL)).thenReturn(UserBeanStub.createUserBeans());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
        systemUserCustomMailService.sendCustomMailToMultipleSystemUsers(UserBeanStub.USER_POWER_LEVEL, UserBeanStub.CUSTOM_SUBJECT, UserBeanStub.CUSTOM_MESSAGE);

        // Verify that one message is available
        Set<UserBean> userBean = userRepository.findByPowerLevel(UserBeanStub.USER_POWER_LEVEL);
        assertTrue("No mail messages found", wiser.getMessages().size() == userBean.size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
                final String subject = CustomerAccessBeanStub.CUSTOM_SUBJECT;
                assertEquals("'Subject' did not match", subject, msg.getSubject());
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());
                assertEquals("'To' address contains more than one recipents", 1, msg.getAllRecipients().length);
                final String messageContent=UserBeanStub.CUSTOM_MESSAGE;
                assertEquals("'Content' did not match",true,msg.getContent().toString().contains(messageContent));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }
}