package com.aurora.mail.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.aurora.mail.Application;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
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
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.WiserMessage;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;


/**
 * Created by Rasheed on 9/13/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EmailVerificationMailServiceTests extends AbstractBaseMailTests {

    private final Logger log = LoggerFactory.getLogger(EmailVerificationMailServiceTests.class);

    @Inject
    @InjectMocks
    private EmailVerificationMailService emailVerificationMailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Inject
    private MessageSource messageSource;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendEmailVerificationMailToCustomer() {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
        emailVerificationMailService.sendEmailVerificationMailToCustomer(CustomerAccessBeanStub.CUSTOMER_A_ID);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
                final String subject = messageSource.getMessage("email.activation.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());
                assertEquals("'To' address contains more than one recipents", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());
                final String messageContent = (String) msg.getContent();
                assertEquals("'CustomerID' did not match", true, messageContent.contains(CustomerAccessBeanStub.CUSTOMER_A_ID.toString()));
                assertEquals("'Token' did not match", true, messageContent.contains(CustomerAccessBeanStub.CUSTOMER_A_TOKEN.toString()));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }
}
