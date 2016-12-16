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