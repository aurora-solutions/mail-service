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
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Created by Rasheed on 9/12/14.
 */
@SuppressWarnings("ALL")
@Service
public class CustomerCustomMailService {
    private final Logger log = LoggerFactory.getLogger(CustomerCustomMailService.class);

    @Inject
    private MailService mailService;

    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private LanguageRepository languageRepository;

    /**
     * Sends custom mail to customer.
     *
     * @param customerId - Customer Id
     * @param subject - Subject
     * @param message - Message
     */
    public void sendCustomMailToCustomer(final Long customerId, final String subject, final String message) {
        Assert.notNull(customerId, "CustomerID can't be Null!");
        CustomerAccessBean customerAccessBean = customerRepository.findById(customerId);
        Assert.notNull(customerAccessBean, "Customer can't be Null!");
        final String email = customerAccessBean.getCredentials().getEmail();

        // Create a locale based on languageId
        final Long languageId = customerAccessBean.getCustomer().getLanguageId();
        Assert.notNull(languageId);
        final String languageCode = languageRepository.findLanguageCodeById(languageId);
        Assert.notNull(languageCode);
        final Locale locale = new Locale(languageCode);
        Assert.notNull(locale);
        log.debug("Sending email verification mail to '{}' in locale '{}'", email, languageCode);

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("customerId", customerId);
        ctx.setVariable("subject", subject);
        ctx.setVariable("message", message);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("customEmail", ctx);
        mailService.sendEmail(email, subject, htmlContent, false, true);
    }
}