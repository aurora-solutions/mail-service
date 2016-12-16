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
