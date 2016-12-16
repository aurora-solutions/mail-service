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

import com.aurora.mail.beans.CustomerAccessBean;
import com.aurora.mail.beans.CustomerOrder;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import com.aurora.mail.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Created by Aamir on 15/09/2014.
 */
@SuppressWarnings("ALL")
@Service
public class OrderConfirmationMailService {
    private final Logger log = LoggerFactory.getLogger(OrderConfirmationMailService.class);

    @Inject
    private MailService mailService;

    @Inject
    private MessageSource messageSource;

    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private OrderRepository orderRepository;

    /**
     * Sends order confirmation email to the customer.
     *
     * @param customerId
     * @param orderId
     */
    public void sendOrderConfirmationEmailToCustomer(final long orderId) {
        Assert.notNull(orderId, "OrderId can't be Null!");
        CustomerOrder customerOrder = orderRepository.findById(orderId);
        final Long customerId=customerOrder.getCustomer().getId();
        CustomerAccessBean customerAccessBean = customerRepository.findById(customerId);
        final String email = customerAccessBean.getCredentials().getEmail();

        final String firstName = customerOrder.getCustomer().getPriv().getFirstName();
        final long orderNumber = customerOrder.getReceipt().getId();
        final String referenceNumber = customerOrder.getReference();
        final int buyOrderAmount = customerOrder.getAmount();
        final String currency = customerOrder.getCurrencyCode();

        // Create a locale based on languageId
        final Long languageId = customerAccessBean.getCustomer().getLanguageId();
        final String languageCode = languageRepository.findLanguageCodeById(languageId);
        final Locale locale = new Locale(languageCode);
        log.debug("Sending email verification mail to '{}' in locale '{}'", email, languageCode);

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("firstName", firstName);
        ctx.setVariable("orderNumber", orderNumber);
        ctx.setVariable("referenceNumber", referenceNumber);
        ctx.setVariable("buyOrderAmount", buyOrderAmount);
        ctx.setVariable("currency", currency);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("orderConfirmationEmail", ctx);

        // Get subject according to locale
        String subject = messageSource.getMessage("email.order.confirmation.title", null, locale);
        mailService.sendEmail(email, subject, htmlContent, false, true);
    }
}