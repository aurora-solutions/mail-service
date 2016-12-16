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

import com.aurora.mail.beans.UserBean;
import com.aurora.mail.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Rasheed on 9/12/14.
 */
@SuppressWarnings("ALL")
@Service
public class SystemUserCustomMailService {
    private final Logger log = LoggerFactory.getLogger(SystemUserCustomMailService.class);

    @Inject
    private MailService mailService;

    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private UserRepository userRepository;

    /**
     * Sends email to the provided user.
     *
     * @param userId
     * @param subject
     * @param message
     */
    public void sendCustomMailToSystemUser(final Integer userId, final String subject, final String message) {
        Assert.notNull(userId, "UserId can't be Null!");
        UserBean userBean = userRepository.findById(userId);
        final String email = userBean.getEmail();
        Locale locale = new Locale("en");

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("userId", userId);
        ctx.setVariable("subject", subject);
        ctx.setVariable("message", message);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("customEmail", ctx);
        mailService.sendEmail(email, subject, htmlContent, false, true);
    }

    /**
     * Finds all users with given powerLevel and sends custom email to them.
     *
     * @param powerLevel
     * @param subject
     * @param message
     */
    public void sendCustomMailToMultipleSystemUsers(final Integer powerLevel, final String subject, final String message) {
        Assert.notNull(powerLevel, "UserId can't be Null!");
        Set<UserBean> userBean = userRepository.findByPowerLevel(powerLevel);
        UserBean userBeanTemp = new UserBean();
        String[] emails = new String[userBean.size()];
        int i = 0;
        Iterator iteratorTemp = userBean.iterator();
        while (iteratorTemp.hasNext()) {
            userBeanTemp = (UserBean) iteratorTemp.next();
            emails[i++] = userBeanTemp.getEmail();
        }
        Locale locale = new Locale("en");

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("subject", subject);
        ctx.setVariable("message", message);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("customEmail", ctx);
        for (i = 0; i < emails.length; i++) {
            mailService.sendEmail(emails[i], subject, htmlContent, false, true);
        }
    }
}
