package com.aurora.mail.util;

import com.aurora.mail.beans.*;

import java.util.Date;

/**
 * Created by Aamir on 15/09/2014.
 */
public class CustomerOrderBeanStub {

    public static final String CUSTOMER_A_EMAIL = "aamir100711@gmail.com";
    public static final String CUSTOMER_A_TOKEN = "1234567890";
    public static final Long CUSTOMER_A_ID = 999L;
    public static final Long CUSTOMER_A_LANGUAGE_ID = 1L;
    public static final String CUSTOMER_A_LANGUAGE_CODE = "en";
    public static final String CUSTOMER_ADDRESS = "F8-ISLAMABAD";
    public static final String CUSTOMER_ZIP = "44000";
    public static final String CUSTOMER_CITY = "ISLAMABAD";
    public static final String COMPANY_NAME = "AURORA";
    public static final String COMPANY_NUMBER = "123123";
    public static final String COUNTRY_NAME = "PAKISTAN";
    public static final String FIRST_NAME="Aamir";
    public static final String REFERENCE_NUMBER="1212";
    public static final Long ORDER_ID=98L;
    public static final String BTC_ADDRESS="BLUE AREA, ISLAMABAD";
    public static final int BTC_AMOUNT=1000;
    public static final int VAT=10;
    public static final int TOTAL_AMOUNT=10000;
    public static final int BTC_PRICE=1500;
    public static Date TIMESTAMP=new Date();
    public static final Long RECEIPT_ID=13L;
    public static final String CURRENCY_CODE="PKR";

    public static CustomerOrder createCustomerOrder() {
        CountryBean countryBean=new CountryBean();
        countryBean.setCountryCode(COUNTRY_NAME);
        CustomerHibernateBean customerHibernateBean = new CustomerHibernateBean();
        customerHibernateBean.setId(CUSTOMER_A_ID);
        customerHibernateBean.setLanguageId(CUSTOMER_A_LANGUAGE_ID);
        customerHibernateBean.setAddress(CUSTOMER_ADDRESS);
        customerHibernateBean.setZip(CUSTOMER_ZIP);
        customerHibernateBean.setCity(CUSTOMER_CITY);
        customerHibernateBean.setCountry(countryBean);
        CustomerCorporateBean customerCorporateBean=new CustomerCorporateBean();
        customerCorporateBean.setCompanyName(COMPANY_NAME);
        customerCorporateBean.setOrganizationNumber(COMPANY_NUMBER);
        CustomerOrder customerOrder=new CustomerOrder();
        customerOrder.setId(ORDER_ID);
        customerOrder.setEmail(CUSTOMER_A_EMAIL);
        customerOrder.setAmount(TOTAL_AMOUNT);
        customerOrder.setCurrencyCode(CURRENCY_CODE);
        customerOrder.setReference(REFERENCE_NUMBER);
        ReceiptBean receiptBean = new ReceiptBean();
        receiptBean.setId(RECEIPT_ID);
        receiptBean.setBtcAddress(BTC_ADDRESS);
        receiptBean.setBtcAmount(BTC_AMOUNT);
        receiptBean.setVat(VAT);
        receiptBean.setTotalAmount(TOTAL_AMOUNT);
        receiptBean.setBtcPrice(BTC_PRICE);
        receiptBean.setTimestamp(TIMESTAMP);
        CustomerPrivateBean customerPrivateBean=new CustomerPrivateBean();
        customerPrivateBean.setFirstName(FIRST_NAME);
        customerOrder.setReceipt(receiptBean);
        customerOrder.setCustomer(customerHibernateBean);
        customerOrder.getCustomer().setPriv(customerPrivateBean);
        customerOrder.getCustomer().setCorporate(customerCorporateBean);
        return customerOrder;
    }

    public static ReceiptBean createReceiptBean() {
        CountryBean countryBean=new CountryBean();
        countryBean.setCountryCode(COUNTRY_NAME);
        CustomerHibernateBean customerHibernateBean = new CustomerHibernateBean();
        customerHibernateBean.setId(CUSTOMER_A_ID);
        customerHibernateBean.setLanguageId(CUSTOMER_A_LANGUAGE_ID);
        customerHibernateBean.setAddress(CUSTOMER_ADDRESS);
        customerHibernateBean.setZip(CUSTOMER_ZIP);
        customerHibernateBean.setCity(CUSTOMER_CITY);
        customerHibernateBean.setCountry(countryBean);
        CustomerCorporateBean customerCorporateBean=new CustomerCorporateBean();
        customerCorporateBean.setCompanyName(COMPANY_NAME);
        customerCorporateBean.setOrganizationNumber(COMPANY_NUMBER);
        CustomerOrder customerOrder=new CustomerOrder();
        customerOrder.setId(ORDER_ID);
        customerOrder.setEmail(CUSTOMER_A_EMAIL);
        customerOrder.setAmount(TOTAL_AMOUNT);
        customerOrder.setCurrencyCode(CURRENCY_CODE);
        customerOrder.setReference(REFERENCE_NUMBER);
        ReceiptBean receiptBean = new ReceiptBean();
        receiptBean.setId(RECEIPT_ID);
        receiptBean.setBtcAddress(BTC_ADDRESS);
        receiptBean.setBtcAmount(BTC_AMOUNT);
        receiptBean.setVat(VAT);
        receiptBean.setTotalAmount(TOTAL_AMOUNT);
        receiptBean.setBtcPrice(BTC_PRICE);
        receiptBean.setTimestamp(TIMESTAMP);
        CustomerPrivateBean customerPrivateBean=new CustomerPrivateBean();
        customerPrivateBean.setFirstName(FIRST_NAME);
        customerOrder.setReceipt(receiptBean);
        customerOrder.setCustomer(customerHibernateBean);
        customerOrder.getCustomer().setPriv(customerPrivateBean);
        customerOrder.getCustomer().setCorporate(customerCorporateBean);
        receiptBean.setOrder(customerOrder);
        return receiptBean;
    }

}
