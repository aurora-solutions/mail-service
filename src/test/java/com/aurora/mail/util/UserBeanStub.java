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

import com.aurora.mail.beans.UserBean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aamir on 15/09/2014.
 */
public class UserBeanStub {

    public static final String CUSTOM_MESSAGE="Hi, Sir.";
    public static final String CUSTOM_SUBJECT="email.activation.title";


    public static final Integer USER_ID=56;
    public static final String USER_FIRST_NAME="Aamir";
    public static final boolean USER_ACTIVE=true;
    public static final String USER_A_EMAIL = "aamir100711@gmail.com";
    public static final Integer USER_POWER_LEVEL = 6;

    public static final Integer USER_ID_2=67;
    public static final String USER_FIRST_NAME_2="Aziz";
    public static final boolean USER_ACTIVE_2=true;
    public static final String USER_A_EMAIL_2 = "aamir100711@gmail.com";
    public static final Integer USER_POWER_LEVEL_2 = 6;

    public static UserBean createUserBean() {
        UserBean userBean=new UserBean();
        userBean.setId(USER_ID);
        userBean.setFirstName(USER_FIRST_NAME);
        userBean.setActive(USER_ACTIVE);
        userBean.setEmail(USER_A_EMAIL);
        userBean.setPowerlevel(USER_POWER_LEVEL);
        return userBean;
    }

    public static Set<UserBean> createUserBeans(){
        Set<UserBean> userBeans	= new HashSet<UserBean>(0);
        UserBean userBean=new UserBean();
        userBean.setId(USER_ID_2);
        userBean.setFirstName(USER_FIRST_NAME_2);
        userBean.setActive(USER_ACTIVE_2);
        userBean.setEmail(USER_A_EMAIL_2);
        userBean.setPowerlevel(USER_POWER_LEVEL);
        UserBean userBean1=new UserBean();
        userBean1.setId(USER_ID);
        userBean1.setFirstName(USER_FIRST_NAME);
        userBean1.setActive(USER_ACTIVE);
        userBean1.setEmail(USER_A_EMAIL);
        userBean1.setPowerlevel(USER_POWER_LEVEL);
        userBeans.add(userBean);
        userBeans.add(userBean1);
        return userBeans;
    }

}
