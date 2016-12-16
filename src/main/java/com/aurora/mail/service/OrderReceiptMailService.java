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
import com.aurora.mail.beans.ReceiptBean;
import com.aurora.mail.repository.CustomerRepository;
import com.aurora.mail.repository.LanguageRepository;
import com.aurora.mail.repository.ReceiptRepository;
import com.lowagie.text.DocumentException;
import com.aurora.mail.beans.CustomerOrder;
import com.aurora.mail.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Application Service to handle sending order reciept emails.
 */
@SuppressWarnings("ALL")
@Service
@Configurable
public class OrderReceiptMailService {

    private static final ClassLoader loader = OrderReceiptMailService.class.getClassLoader();
    private final Logger log = LoggerFactory.getLogger(OrderReceiptMailService.class);

    @Inject
    private MailService mailService;

    @Inject
    private Environment env;

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

    @Inject
    private ReceiptRepository receiptRepository;

    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    /**
     * Sends order reciept to the customer.
     *
     * @param receiptId
     */
    public void sendOrderReceiptEmailWithAttachment(final Long receiptId) {
        try {
            Assert.notNull(receiptId, "ReceiptId can't be Null!");
            ReceiptBean receipt = receiptRepository.findById(receiptId);
            Assert.notNull(receipt, "Receipt can't be Null!");

            final Long orderId = receipt.getOrder().getId();
            Assert.notNull(orderId, "OrderId can't be Null!");
            CustomerOrder order = receipt.getOrder();
            Assert.notNull(order, "Order can't be Null!");

            final Long customerId = receipt.getOrder().getCustomer().getId();
            Assert.notNull(customerId, "CustomerID can't be Null!");
            CustomerAccessBean customer = customerRepository.findById(customerId);
            Assert.notNull(customer, "Customer can't be Null!");

            String attachmentHTMLContent = createHTMLContentForAttachment(receiptId);

            ByteArrayOutputStream receiptPDFFileName = createPDF(attachmentHTMLContent);

            byte[] pdfFileInBytesFormat = receiptPDFFileName.toByteArray();

            Context ctx = createContext(customer, order, receipt);
            Locale locale = ctx.getLocale();

            final String subject = messageSource.getMessage("email.order.receipt.title", null, locale);
            final String email = customer.getCredentials().getEmail();

            // Create the HTML body using Thymeleaf
            final String emailHTMLContent = this.templateEngine.process("orderReceiptEmail", ctx);

            final String attachmentName = "Receipt Number :" + receiptId.toString();

            mailService.sendEmailWithAttachment(email, subject, emailHTMLContent, attachmentName, pdfFileInBytesFormat, "attachment/pdf", true, true);
        } catch (Exception exception) {
            log.error("Exception : ", exception);
        }
    }

    /**
     * Creates HTML content for attachment for given recieptId
     *
     * @param receiptId
     * @return: HTML content for attachment
     */
    public String createHTMLContentForAttachment(final Long receiptId) {

        Assert.notNull(receiptId, "ReceiptId can't be Null!");
        ReceiptBean receipt = receiptRepository.findById(receiptId);
        Assert.notNull(receipt, "Receipt can't be Null!");

        final Long orderId = receipt.getOrder().getId();
        Assert.notNull(orderId, "OrderId can't be Null!");
        CustomerOrder order = receipt.getOrder();
        Assert.notNull(order, "Order can't be Null!");

        final Long customerId = receipt.getOrder().getCustomer().getId();
        Assert.notNull(customerId, "CustomerID can't be Null!");
        CustomerAccessBean customer = customerRepository.findById(customerId);
        Assert.notNull(customer, "Customer can't be Null!");

        Context ctx = createContext(customer, order, receipt);

        // Create the HTML using Thymeleaf for the attachment by replacing place holders with real values.
        String attachmentHTMLContent = this.templateEngine.process("orderReceiptAttachment", ctx);

        return attachmentHTMLContent;
    }

    /**
     * Creates a context.
     *
     * @param customerAccessBean
     * @param customerOrder
     * @param receiptBean
     * @return
     */
    private Context createContext(final CustomerAccessBean customerAccessBean, final CustomerOrder customerOrder, final ReceiptBean receiptBean) {
        final String email = customerAccessBean.getCredentials().getEmail();
        final String firstName = customerAccessBean.getCustomer().getPriv().getFirstName();
        final String receiptName = customerAccessBean.getCustomer().getPriv().getFirstName();
        final String receiptAddress = receiptBean.getOrder().getCustomer().getAddress();
        final String receiptZip = receiptBean.getOrder().getCustomer().getZip();
        final String receiptCity = receiptBean.getOrder().getCustomer().getCity();
        final String receiptCountry = receiptBean.getOrder().getCustomer().getCountry().getCountryCode();
        final long orderNumber = receiptBean.getOrder().getId();
        final java.util.Date receiptTimestamp = receiptBean.getOrder().getReceipt().getTimestamp();
        final int receiptBtcAmount = receiptBean.getOrder().getReceipt().getBtcAmount();
        final int receiptBtcPrice = receiptBean.getOrder().getReceipt().getBtcPrice();
        final String receiptCurrency = receiptBean.getOrder().getCurrencyCode();
        final int receiptVat = receiptBean.getOrder().getReceipt().getVat();
        final String receiptBtcAddress = receiptBean.getOrder().getReceipt().getBtcAddress();
        final int receiptTotalAmount = receiptBean.getOrder().getReceipt().getTotalAmount();
        final String receiptOrganizationName = receiptBean.getOrder().getCustomer().getCorporate().getCompanyName();
        final String receiptOrganizationNumber = receiptBean.getOrder().getCustomer().getCorporate().getOrganizationNumber();
        final String receiptOrganizationEmail = from;

        // Create a locale based on languageId
        final Long languageId = customerAccessBean.getCustomer().getLanguageId();
        final String languageCode = languageRepository.findLanguageCodeById(languageId);
        final Locale locale = new Locale(languageCode);
        log.debug("Sending order reciept e-mail with attachments to '{}'", email);


        String dateFromTimestamp = new SimpleDateFormat("yyyy-MM-dd").format(receiptTimestamp);
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("firstName", firstName);
        ctx.setVariable("orderNumber", orderNumber);
        ctx.setVariable("receiptName", receiptName);
        ctx.setVariable("receiptAddress", receiptAddress);
        ctx.setVariable("receiptZip", receiptZip);
        ctx.setVariable("receiptCity", receiptCity);
        ctx.setVariable("receiptCountry", receiptCountry);
        ctx.setVariable("receiptTimestamp", dateFromTimestamp);
        ctx.setVariable("receiptBtcAmount", receiptBtcAmount);
        ctx.setVariable("receiptBtcPrice", receiptBtcPrice);
        ctx.setVariable("receiptCurrency", receiptCurrency);
        ctx.setVariable("receiptVat", receiptVat);
        ctx.setVariable("receiptBtcAddress", receiptBtcAddress);
        ctx.setVariable("receiptTotalAmount", receiptTotalAmount);
        ctx.setVariable("receiptOrganizationName", receiptOrganizationName);
        ctx.setVariable("receiptOrganizationNumber", receiptOrganizationNumber);
        ctx.setVariable("receiptOrganizationEmail", receiptOrganizationEmail);
        ctx.setVariable("email", email);
        return ctx;
    }

    /**
     * Create a pdf.
     *
     * @param attachmentHTMLContent html content of the attachment that needs to be converted into pdf.
     * @return pdf contents as byte array.
     * @throws DocumentException
     */
    private ByteArrayOutputStream createPDF(final String attachmentHTMLContent) throws DocumentException {
        ByteArrayOutputStream pdfFileInBytesFormat = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(attachmentHTMLContent);
        renderer.layout();
        renderer.createPDF(pdfFileInBytesFormat);
        return pdfFileInBytesFormat;
    }
}
