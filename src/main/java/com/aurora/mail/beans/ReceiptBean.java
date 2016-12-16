package com.aurora.mail.beans;

import java.util.Date;

public class ReceiptBean {

    private Long id;
    private String btcAddress;
    private int btcAmount;
    private int vat;
    private int totalAmount;
    private int btcPrice;
    private CustomerOrder order;
    private Date timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public int getBtcAmount() {
        return btcAmount;
    }

    public void setBtcAmount(int btcAmount) {
        this.btcAmount = btcAmount;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(int btcPrice) {
        this.btcPrice = btcPrice;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
