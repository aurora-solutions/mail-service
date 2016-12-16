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
