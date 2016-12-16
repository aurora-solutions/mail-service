package com.aurora.mail.beans;

public class CustomerPrivateBean {

    private Long id;
    private CustomerHibernateBean customer;
    private String firstName;
    private String lastName;
    private String personalNumber;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

}
