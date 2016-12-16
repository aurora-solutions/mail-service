package com.aurora.mail.beans;

public class CustomerOrder {

    private Long id;
    private String email;
    private String reference;
    private int amount;
    private String currencyCode;
    private Integer channel;
    private CustomerHibernateBean customer;
    private ReceiptBean receipt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public CustomerHibernateBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerHibernateBean customer) {
        this.customer = customer;
    }

    public ReceiptBean getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptBean receipt) {
        this.receipt = receipt;
    }
}
