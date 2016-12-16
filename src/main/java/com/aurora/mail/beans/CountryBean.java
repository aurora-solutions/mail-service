package com.aurora.mail.beans;

/*import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;*/

public class CountryBean implements Comparable<CountryBean> {
    private Long id;
    private String countryCode;
    private String countrySwe;
    private String countryEng;
    private transient String countryLocal;
    private String dialingCode;
    private String currencyCode;
    private String primaryLanguage;
    private String languageCode;
    private boolean languageActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountrySwe() {
        return countrySwe;
    }

    public void setCountrySwe(String countrySwe) {
        this.countrySwe = countrySwe;
    }

    public String getCountryEng() {
        return countryEng;
    }

    public void setCountryEng(String countryEng) {
        this.countryEng = countryEng;
    }

    //	@Transient
    public String getCountryLocal() {
        return countryLocal;
    }

    public void setCountryLocal(String countryLocal) {
        this.countryLocal = countryLocal;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isLanguageActive() {
        return languageActive;
    }

    public void setLanguageActive(boolean languageActive) {
        this.languageActive = languageActive;
    }

    @Override
    public int compareTo(CountryBean o) {
        return countryEng.compareTo(o.countryEng);
    }
}
