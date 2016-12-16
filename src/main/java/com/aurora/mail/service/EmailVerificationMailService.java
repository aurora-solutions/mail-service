package com.aurora.mail.service;

import com.aurora.mail.beans.CustomerAccessBean;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
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
 * Application Service to send mails for email verification and account activation.
 * <p/>
 * Created by Rasheed on 9/12/14.
 */
@SuppressWarnings("ALL")
@Service
public class EmailVerificationMailService {

    private final Logger log = LoggerFactory.getLogger(EmailVerificationMailService.class);

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

    /**
     * Sends email verification mail to the customer.
     *
     * @param customerId
     */
    public void sendEmailVerificationMailToCustomer(final Long customerId) {
        Assert.notNull(customerId, "CustomerID can't be Null!");
        CustomerAccessBean customerAccessBean = customerRepository.findById(customerId);
        Assert.notNull(customerAccessBean, "Customer can't be Null!");
        final String email = customerAccessBean.getCredentials().getEmail();
        final String token = customerAccessBean.getCredentials().getToken();

        // Create a locale based on languageId
        final Long languageId = customerAccessBean.getCustomer().getLanguageId();
        final String languageCode = languageRepository.findLanguageCodeById(languageId);
        final Locale locale = new Locale(languageCode);
        log.debug("Sending email verification mail to '{}' in locale '{}'", email, languageCode);

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("customerId", customerId);
        ctx.setVariable("token", token);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("activationEmail", ctx);

        // Get subject according to locale
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        mailService.sendEmail(email, subject, htmlContent, false, true);
    }
}
