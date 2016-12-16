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


package com.aurora.mail.api;

import com.aurora.mail.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/app")
@Api(value = "Mail", description = "Mail API")
public class MailApi {

    private final Logger log = LoggerFactory.getLogger(MailApi.class);

    @Inject
    private CustomerCustomMailService customerCustomMailService;

    @Inject
    private EmailVerificationMailService emailVerificationMailService;

    @Inject
    private OrderConfirmationMailService orderConfirmationMailService;

    @Inject
    private OrderReceiptMailService orderReceiptMailService;

    @Inject
    private SystemUserCustomMailService systemUserCustomMailService;

    /**
     * Sends custom mail to a customer.
     * <p/>
     * POST  /app/rest/sendCustomMailToCustomer -> sends custom mail to a customer.
     *
     * @param customerId - Unique customer identifier
     * @param subject    - Subject of the message
     * @param message    - Body or details of the message
     */
    @ApiOperation(value = "Send custom mail.", notes = "Sends custom mail to a customer.")
    @RequestMapping(value = "/rest/sendCustomMailToCustomer", method = RequestMethod.POST)
    public void sendCustomMailToCustomer(final Long customerId, final String subject, final String message) {
        // Call the required method of customerCustomMailService with appropriate parameters
        this.customerCustomMailService.sendCustomMailToCustomer(customerId, subject, message);
    }

    /**
     * Sends verification mail to a customer.
     * <p/>
     * POST  /app/rest/sendEmailVerificationMailToCustomer -> sends verification mail to a customer.
     *
     * @param customerId - Unique customer identifier
     */
    @ApiOperation(value = "Send verification mail.", notes = "Sends verification mail to a customer.")
    @RequestMapping(value = "/rest/sendEmailVerificationMailToCustomer", method = RequestMethod.POST)
    public void sendEmailVerificationMailToCustomer(final Long customerId) {
        // Call the required method of emailVerificationMailService with appropriate parameters
        this.emailVerificationMailService.sendEmailVerificationMailToCustomer(customerId);
    }

    /**
     * Sends order confirmation mail to customer.
     * <p/>
     * POST  /app/rest/sendOrderConfirmationEmailToCustomer -> sends order confirmation mail to customer.
     *
     * @param orderId
     */
    @ApiOperation(value = "Send order confirmation mail.", notes = "Sends order confirmation mail to customer.")
    @RequestMapping(value = "/rest/sendOrderConfirmationEmailToCustomer", method = RequestMethod.POST)
    public void sendOrderConfirmationEmailToCustomer(final long orderId) {
        // Call the required method of orderConfirmationMailService with appropriate parameters
        this.orderConfirmationMailService.sendOrderConfirmationEmailToCustomer(orderId);
    }

    /**
     * Sends order receipt mail with attachment to customer.
     * <p/>
     * POST  /app/rest/sendOrderReceiptEmailWithAttachment -> sends order receipt mail with attachment to customer.
     *
     * @param receiptId
     */
    @ApiOperation(value = "Send order receipt mail with attachment.", notes = "Sends order receipt mail with attachment to customer.")
    @RequestMapping(value = "/rest/sendOrderReceiptEmailWithAttachment", method = RequestMethod.POST)
    public void sendOrderReceiptEmailWithAttachment(final Long receiptId) {
        // Call the required method of orderReceiptMailService with appropriate parameters
        this.orderReceiptMailService.sendOrderReceiptEmailWithAttachment(receiptId);
    }

    /**
     * Sends custom mail to system user.
     * <p/>
     * POST  /app/rest/sendCustomMailToSystemUser -> sends custom mail to system user.
     *
     * @param userId
     * @param subject
     * @param message
     */
    @ApiOperation(value = "Send custom mail to system user.", notes = "Sends custom mail to system user.")
    @RequestMapping(value = "/rest/sendCustomMailToSystemUser", method = RequestMethod.POST)
    public void sendCustomMailToSystemUser(final Integer userId, final String subject, final String message) {
        // Call the required method of systemUserCustomMailService with appropriate parameters for single user
        this.systemUserCustomMailService.sendCustomMailToSystemUser(userId, subject, message);
    }

    /**
     * Sends custom mail to multiple system users.
     * <p/>
     * POST  /app/rest/sendCustomMailToMultipleSystemUsers -> sends custom mail to multiple system users.
     *
     * @param powerLevel
     * @param subject
     * @param message
     */
    @ApiOperation(value = "Send custom mail to multiple system users.", notes = "Sends custom mail to multiple system users.")
    @RequestMapping(value = "/rest/sendCustomMailToMultipleSystemUsers", method = RequestMethod.POST)
    public void sendCustomMailToMultipleSystemUsers(final Integer powerLevel, final String subject, final String message) {
        // Call the required method of systemUserCustomMailService with appropriate parameters for multiple users
        this.systemUserCustomMailService.sendCustomMailToMultipleSystemUsers(powerLevel, subject, message);
    }

    @ExceptionHandler(Exception.class)
    public String error() {
        return "error.html";
    }
}
