package com.aurora.mail.beans;

import java.util.Date;

public class CustomerAccessBean {

    private Long id;
    private CustomerHibernateBean customer;
    private CustomerCredentialsBean credentials;
    private Date credentialsTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerHibernateBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerHibernateBean customer) {
        this.customer = customer;
    }

    public CustomerCredentialsBean getCredentials() {
        return credentials;
    }

    public void setCredentials(CustomerCredentialsBean credentials) {
        this.credentials = credentials;
    }

    public Date getCredentialsTimestamp() {
        return credentialsTimestamp;
    }

    public void setCredentialsTimestamp(Date credentialsTimestamp) {
        this.credentialsTimestamp = credentialsTimestamp;
    }

/*
    public void removeCircularReferences() {
        credentials.getGoogleAuth().setCredentials(null);
    }
*/

}
