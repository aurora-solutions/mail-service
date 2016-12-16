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

import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import com.aurora.mail.repository.ReceiptRepository;
import com.aurora.mail.util.CustomerOrderBeanStub;
import com.aurora.mail.Application;
import com.aurora.mail.repository.OrderRepository;
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
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Aamir on 15/09/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class OrderReceiptMailServiceTests extends AbstractBaseMailTests {

    private final Logger log = LoggerFactory.getLogger(OrderReceiptMailServiceTests.class);

    @Inject
    @InjectMocks
    private OrderReceiptMailService orderReceiptMailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @Inject
    private MessageSource messageSource;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendOrderReceiptEmailWithAttachment() {
        when(customerRepository.findById(CustomerAccessBeanStub.CUSTOMER_A_ID)).thenReturn(CustomerAccessBeanStub.createCustomerAccessBean());
        when(receiptRepository.findById(CustomerOrderBeanStub.RECEIPT_ID)).thenReturn(CustomerOrderBeanStub.createReceiptBean());
        when(languageRepository.findLanguageCodeById(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_ID)).thenReturn(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
        orderReceiptMailService.sendOrderReceiptEmailWithAttachment(CustomerOrderBeanStub.RECEIPT_ID);

        // Verify that one message is available
        assertEquals("No mail messages found", 1, wiser.getMessages().size());
        try {
            if (wiser.getMessages().size() > 0) {
                WiserMessage wMsg = wiser.getMessages().get(0);
                MimeMessage msg = wMsg.getMimeMessage();

                assertNotNull("Message was null", msg);
                final Locale locale = new Locale(CustomerAccessBeanStub.CUSTOMER_A_LANGUAGE_CODE);
                final String subject = messageSource.getMessage("email.order.receipt.title", null, locale);
                assertEquals("'Subject' did not match", subject, msg.getSubject());
                final String from = env.getProperty("spring.mail.from");
                assertEquals("'From' address did not match", from, msg.getFrom()[0].toString());
                assertEquals("'To' address contains more than one recipents", 1, msg.getAllRecipients().length);
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
}