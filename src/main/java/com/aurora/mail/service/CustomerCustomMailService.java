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