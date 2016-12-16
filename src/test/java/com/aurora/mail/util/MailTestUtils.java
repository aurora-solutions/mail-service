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


package com.aurora.mail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

/**
 * Created by Rasheed on 9/13/14.
 *
 * A util class where we call a method which finds all the Spring-managed classes which are JavaMailSenderImpl and
 * reconfigures them to use the same host and port that we set in the junit test setup method.
 */
public class MailTestUtils {

    private static final Logger log = LoggerFactory.getLogger(MailTestUtils.class);

    public static void reconfigureMailSenders(ApplicationContext applicationContext, int port, String hostName) {
        Map<String, JavaMailSenderImpl> ofType =
                applicationContext.getBeansOfType(org.springframework.mail.javamail.JavaMailSenderImpl.class);

        for (Map.Entry<String, JavaMailSenderImpl> bean : ofType.entrySet()) {
            log.info(String.format("configuring mailsender %s to use local Wiser SMTP", bean.getKey()));
            JavaMailSenderImpl mailSender = bean.getValue();
            mailSender.setHost(hostName);
            mailSender.setPort(port);
        }
    }
}
