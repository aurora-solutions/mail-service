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


package com.aurora.mail.api;

import com.aurora.mail.beans.UserBean;
import com.aurora.mail.repository.*;
import com.aurora.mail.service.*;
import com.aurora.mail.util.CustomerOrderBeanStub;
import com.aurora.mail.util.MailTestUtils;
import com.aurora.mail.util.UserBeanStub;
import com.aurora.mail.Application;
import com.aurora.mail.util.CustomerAccessBeanStub;
import org.junit.After;
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
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MailApiTests extends AbstractJUnit4SpringContextTests {

    private final Logger log = LoggerFactory.getLogger(MailApiTests.class);

    private Wiser wiser;
    private int port = 2500;
    private String hostName = "localhost";

    // Inject and Mock the repositories in MailApi Controller and all the Services required
    @Inject
    @InjectMocks
    private MailApi mailApi;

    @Inject
    @InjectMocks
    private CustomerCustomMailService customerCustomMailService;

    @Inject
    @InjectMocks
    private EmailVerificationMailService emailVerificationMailService;

    @Inject
    @InjectMocks
    private OrderConfirmationMailService orderConfirmationMailService;

    @Inject
    @InjectMocks
    private OrderReceiptMailService orderReceiptMailService;

    @Inject
    @InjectMocks
    private SystemUserCustomMailService systemUserCustomMailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private UserRepository userRepository;

    @Inject
    private MessageSource messageSource;

    @Inject
    public Environment env;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        wiser = new Wiser();
        wiser.setPort(port);
        wiser.setHostname(hostName);
        wiser.start();
        MailTestUtils.reconfigureMailSenders(applicationContext, port, hostName);

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(mailApi).build();
    }

    @Test
    public void testSendCustomMailToCustomer() throws Exception {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendCustomMailToCustomer")
                .param("customerId", CustomerAccessBeanStub.CUSTOMER_A_ID.toString())
                .param("subject", CustomerAccessBeanStub.CUSTOM_SUBJECT)
                .param("message", CustomerAccessBeanStub.CUSTOM_MESSAGE))
                .andExpect(status().isOk());

        // Verify that we have one interaction with customerRepository
        verify(customerRepository, times(1)).findById(CustomerAccessBeanStub.CUSTOMER_A_ID);
        verifyNoMoreInteractions(customerRepository);

        // Verify that we have one interaction with languageRepository
        verify(languageRepository, times(1)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();
                assertNotNull("Message was null", msg);

                // Verify that subject of mail is same
                final String subject = CustomerAccessBeanStub.CUSTOM_SUBJECT;
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());

                // Verify message content
                final String messageContent = CustomerAccessBeanStub.CUSTOM_MESSAGE;
                assertEquals("'Content' did not match", true, msg.getContent().toString().contains(messageContent));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }

    @Test
    public void testSendEmailVerificationMailToCustomer() throws Exception {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendEmailVerificationMailToCustomer")
                .param("customerId", CustomerAccessBeanStub.CUSTOMER_A_ID.toString()))
                .andExpect(status().isOk());

        // Verify that we have one interaction with customerRepository
        verify(customerRepository, times(1)).findById(CustomerAccessBeanStub.CUSTOMER_A_ID);
        verifyNoMoreInteractions(customerRepository);

        // Verify that we have one interaction with languageRepository
        verify(languageRepository, times(1)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

                // Verify that subject of mail is same
                final String subject = messageSource.getMessage("email.activation.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());

                // Verify message content
                final String messageContent = (String) msg.getContent();
                assertEquals("'CustomerID' did not match", true, messageContent.contains(CustomerAccessBeanStub.CUSTOMER_A_ID.toString()));
                assertEquals("'Token' did not match", true, messageContent.contains(CustomerAccessBeanStub.CUSTOMER_A_TOKEN.toString()));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }

    @Test
    public void testSendOrderConfirmationEmailToCustomer() throws Exception {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(orderRepository.findById(CustomerOrderBeanStub.ORDER_ID)).thenReturn(CustomerOrderBeanStub.createCustomerOrder());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendOrderConfirmationEmailToCustomer")
                .param("orderId", CustomerOrderBeanStub.ORDER_ID.toString()))
                .andExpect(status().isOk());

        // Verify that we have one interaction with customerRepository
        verify(customerRepository, times(1)).findById(CustomerAccessBeanStub.CUSTOMER_A_ID);
        verifyNoMoreInteractions(customerRepository);

        // Verify that we have one interaction with orderRepository
        verify(orderRepository, times(1)).findById(CustomerOrderBeanStub.ORDER_ID);
        verifyNoMoreInteractions(orderRepository);

        // Verify that we have one interaction with languageRepository
        verify(languageRepository, times(1)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();
                assertNotNull("Message was null", msg);

                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

                // Verify that subject of mail is same
                final String subject = messageSource.getMessage("email.order.confirmation.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());

                // Verify message content
                final String messageContent = (String) msg.getContent();
                assertEquals("'firstName' did not match", true, messageContent.contains(CustomerAccessBeanStub.FIRST_NAME));
                assertEquals("'currencyCode' did not match", true, messageContent.contains(CustomerOrderBeanStub.CURRENCY_CODE));
                assertEquals("'referenceNumber' did not match", true, messageContent.contains(CustomerOrderBeanStub.REFERENCE_NUMBER));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }

    @Test
    public void testSendOrderReceiptEmailWithAttachment() throws Exception {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(receiptRepository.findById(CustomerOrderBeanStub.RECEIPT_ID)).thenReturn(CustomerOrderBeanStub.createReceiptBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendOrderReceiptEmailWithAttachment")
                .param("receiptId", CustomerOrderBeanStub.RECEIPT_ID.toString()))
                .andExpect(status().isOk());

        // Verify that we have two interaction with customerRepository
        verify(customerRepository, times(2)).findById(CustomerAccessBeanStub.CUSTOMER_A_ID);
        verifyNoMoreInteractions(customerRepository);

        // Verify that we have two interaction with receiptRepository
        verify(receiptRepository, times(2)).findById(CustomerOrderBeanStub.RECEIPT_ID);
        verifyNoMoreInteractions(receiptRepository);

        // Verify that we have two interaction with languageRepository
        verify(languageRepository, times(2)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();
                assertNotNull("Message was null", msg);

                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

                // Verify that subject of mail is same
                final String subject = messageSource.getMessage("email.order.receipt.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", CustomerAccessBeanStub.CUSTOMER_A_EMAIL, msg.getAllRecipients()[0].toString());

                // attachment data confirmations
                final String attachmentHTMLContent = orderReceiptMailService.createHTMLContentForAttachment(CustomerOrderBeanStub.RECEIPT_ID);
                assertEquals("'customer_address' did not match", true, attachmentHTMLContent.contains(CustomerOrderBeanStub.CUSTOMER_ADDRESS));
                assertEquals("'customer_zip' did not match", true, attachmentHTMLContent.contains(CustomerOrderBeanStub.CUSTOMER_ZIP));

                assertEquals("'customer_city' did not match", true, attachmentHTMLContent.contains(CustomerOrderBeanStub.CUSTOMER_CITY));
                assertEquals("'country_name' did not match", true, attachmentHTMLContent.contains(CustomerOrderBeanStub.COUNTRY_NAME));

                String dateFromTimestamp = new SimpleDateFormat("yyyy-MM-dd").format(CustomerOrderBeanStub.TIMESTAMP);
                assertEquals("'timestamp' did not match", true, attachmentHTMLContent.contains(dateFromTimestamp));

                assertEquals("'btc_amount' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.BTC_AMOUNT)));
                assertEquals("'currency_code' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.CURRENCY_CODE)));

                assertEquals("'vat' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.VAT)));
                assertEquals("'btc_address' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.BTC_ADDRESS)));

                assertEquals("'company_name' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.COMPANY_NAME)));
                assertEquals("'company number' did not match", true, attachmentHTMLContent.contains(String.valueOf(CustomerOrderBeanStub.COMPANY_NUMBER)));
                assertEquals("'company email' did not match", true, attachmentHTMLContent.contains(String.valueOf(from)));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }

    @Test
    public void testSendCustomMailToSystemUser() throws Exception {
        when(userRepository.findById(UserBeanStub.USER_ID)).thenReturn(UserBeanStub.createUserBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendCustomMailToSystemUser")
                .param("userId", UserBeanStub.USER_ID.toString())
                .param("subject", UserBeanStub.CUSTOM_SUBJECT)
                .param("message", UserBeanStub.CUSTOM_MESSAGE))
                .andExpect(status().isOk());

        // Verify that we have one interaction with userRepository
        verify(userRepository, times(1)).findById(UserBeanStub.USER_ID);
        verifyNoMoreInteractions(userRepository);

        // Verify that we have no interaction with languageRepository
        verify(languageRepository, times(0)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();
                assertNotNull("Message was null", msg);

                // Verify that subject of mail is same
                final String subject = UserBeanStub.CUSTOM_SUBJECT;
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);
                assertEquals("'To' address did not match", UserBeanStub.USER_A_EMAIL, msg.getAllRecipients()[0].toString());

                // Verify message content
                final String messageContent = UserBeanStub.CUSTOM_MESSAGE;
                assertEquals("'Content' did not match", true, msg.getContent().toString().contains(messageContent));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {0}", exception));
        }
    }

    @Test
    public void testSendCustomMailToMultipleSystemUsers() throws Exception {
        when(userRepository.findByPowerLevel(UserBeanStub.USER_POWER_LEVEL)).thenReturn(UserBeanStub.createUserBeans());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);

        this.mockMvc.perform(post("/app/rest/sendCustomMailToMultipleSystemUsers")
                .param("powerLevel", UserBeanStub.USER_POWER_LEVEL.toString())
                .param("subject", UserBeanStub.CUSTOM_SUBJECT)
                .param("message", UserBeanStub.CUSTOM_MESSAGE))
                .andExpect(status().isOk());

        // Verify that we have one interaction with userRepository
        verify(userRepository, times(1)).findByPowerLevel(UserBeanStub.USER_POWER_LEVEL);
        verifyNoMoreInteractions(userRepository);

        // Verify that we have no interaction with languageRepository
        verify(languageRepository, times(0)).findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID);
        verifyNoMoreInteractions(languageRepository);

        // Verify that one message is available
        Set<UserBean> userBean = userRepository.findByPowerLevel(UserBeanStub.USER_POWER_LEVEL);
        assertTrue("No mail messages found", wiser.getMessages().size() == userBean.size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();
                assertNotNull("Message was null", msg);

                // Verify that subject of mail is same
                final String subject = CustomerAccessBeanStub.CUSTOM_SUBJECT;
                assertEquals("'Subject' did not match", subject, msg.getSubject());

                // Verify that from address is same
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());

                // Verify that to address is same
                assertEquals("'To' address contains more than one recipients", 1, msg.getAllRecipients().length);

                // Verify message content
                final String messageContent = UserBeanStub.CUSTOM_MESSAGE;
                assertEquals("'Content' did not match", true, msg.getContent().toString().contains(messageContent));
            }
        } catch (Exception exception) {
            fail(String.format("Exception occurred during test execution {}", exception));
        }
    }

    @After
    public void after() {
        wiser.stop();
    }
}