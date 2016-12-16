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

import com.aurora.mail.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;

import javax.inject.Inject;

/**
 * Created by Rasheed on 9/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class AbstractBaseMailTests extends AbstractJUnit4SpringContextTests {

    private final Logger log = LoggerFactory.getLogger(AbstractBaseMailTests.class);

    @Inject
    public Environment env;

    public Wiser wiser;
    private int wiserPort = 2500;
    private String wiserHostName = "localhost";

    @Before
    public void setup() {
        wiser = new Wiser();
        wiser.setPort(wiserPort);
        wiser.setHostname(wiserHostName);
        wiser.start();

        MailTestUtils.reconfigureMailSenders(applicationContext, wiserPort, wiserHostName);
    }

    @After
    public void after() {
        try {
            wiser.stop();
        } catch (Exception exception) {
            log.error("Exception while stopping wiser!", exception);
        }
    }

}
