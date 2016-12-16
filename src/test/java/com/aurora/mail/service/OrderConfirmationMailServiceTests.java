package com.aurora.mail.service;

import com.aurora.mail.Application;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import com.aurora.mail.repository.OrderRepository;
import com.aurora.mail.util.AbstractBaseMailTests;
import com.aurora.mail.util.CustomerAccessBeanStub;
import com.aurora.mail.util.CustomerOrderBeanStub;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Aamir on 15/09/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class OrderConfirmationMailServiceTests extends AbstractBaseMailTests {

    private final Logger log = LoggerFactory.getLogger(OrderConfirmationMailServiceTests.class);

    @Inject
    @InjectMocks
    private OrderConfirmationMailService orderConfirmationMailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private OrderRepository orderRepository;

    @Inject
    private MessageSource messageSource;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendOrderConfirmationEmailToCustomer() {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(orderRepository.findById(CustomerOrderBeanStub.ORDER_ID)).thenReturn(CustomerOrderBeanStub.createCustomerOrder());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
        orderConfirmationMailService.sendOrderConfirmationEmailToCustomer(CustomerOrderBeanStub.ORDER_ID);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
                final String subject = messageSource.getMessage("email.order.confirmation.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());
                assertEquals("'To' address contains more than one recipents", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());
                final String messageContent = (String) msg.getContent();
                assertEquals("'firstName' did not match", true, messageContent.contains(CustomerAccessBeanStub.FIRST_NAME));
                assertEquals("'currencyCode' did not match", true, messageContent.contains(CustomerOrderBeanStub.CURRENCY_CODE));
                assertEquals("'referenceNumber' did not match", true, messageContent.contains(CustomerOrderBeanStub.REFERENCE_NUMBER));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }
}