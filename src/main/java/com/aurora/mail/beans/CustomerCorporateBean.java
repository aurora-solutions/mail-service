package com.aurora.mail.beans;

public class CustomerCorporateBean {
    private Long id;
    private CustomerHibernateBean customer;
    private String companyName;
    private String organizationNumber;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

}
