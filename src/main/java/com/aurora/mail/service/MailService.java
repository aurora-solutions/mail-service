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

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

/**
 * Infrastructure service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@SuppressWarnings("ALL")
@Service
@Configurable
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSender javaMailSender;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    /**
     * @param to
     * @param subject
     * @param content
     * @param isMultipart
     * @param isHtml
     */
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail to user '{}'!", to);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to user '{}'!", to);
        } catch (Exception e) {
            log.error("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    /**
     * @param to
     * @param subject
     * @param content
     * @param isMultipart
     * @param isHtml
     */
    @Async
    public void sendEmailWithAttachment(String to, String subject, String content, final String attachmentFileName,
                                        final byte[] attachmentBytes, final String attachmentContentType, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail to user '{}'!", to);

        final InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);

            // Add the attachment
            message.addAttachment(attachmentFileName, attachmentSource, attachmentContentType);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'!", to);
        } catch (Exception e) {
            log.error("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }
}
