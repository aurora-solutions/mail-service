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

import com.aurora.mail.beans.CustomerAccessBean;
import com.aurora.mail.beans.CustomerHibernateBean;
import com.aurora.mail.beans.CustomerPrivateBean;
import com.aurora.mail.beans.CustomerCredentialsBean;

/**
 * Created by Rasheed on 9/14/14.
 */
public final class CustomerAccessBeanStub {

    public static final String CUSTOMER_A_EMAIL = "aamir100711@gmail.com";
    public static final String CUSTOMER_A_TOKEN = "1234567890";
    public static final Long CUSTOMER_A_ID = 999L;
    public static final Long CUSTOMER_A_LANGUAGE_ID = 1L;
    public static final String CUSTOMER_A_LANGUAGE_CODE = "en";

    public static final String FIRST_NAME="Aamir";
    public static final Long ORDER_NUMBER=1122L;

    public static final String CUSTOM_MESSAGE="Hi, Sir.";
    public static final String CUSTOM_SUBJECT="email.activation.title";

    public static CustomerAccessBean createCustomerAccessBean() {
        CustomerAccessBean customerAccessBean = new CustomerAccessBean();
        customerAccessBean.setId(CUSTOMER_A_ID);
        CustomerHibernateBean customerHibernateBean = new CustomerHibernateBean();
        customerHibernateBean.setId(CUSTOMER_A_ID);
        customerHibernateBean.setLanguageId(CUSTOMER_A_LANGUAGE_ID);
        customerAccessBean.setCustomer(customerHibernateBean);
        CustomerCredentialsBean customerCredentialsBean = new CustomerCredentialsBean();
        customerCredentialsBean.setEmail(CUSTOMER_A_EMAIL);
        customerCredentialsBean.setToken(CUSTOMER_A_TOKEN);
        customerAccessBean.setCredentials(customerCredentialsBean);

        CustomerPrivateBean customerPrivateBean=new CustomerPrivateBean();
        customerPrivateBean.setFirstName(FIRST_NAME);
        customerAccessBean.getCustomer().setPriv(customerPrivateBean);

        return customerAccessBean;
    }

}
