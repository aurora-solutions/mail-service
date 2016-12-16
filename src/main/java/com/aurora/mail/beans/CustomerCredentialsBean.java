package com.aurora.mail.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CustomerCredentialsBean {

    private Long id;
    private String email;
    private String token;
    private String username;
    private Boolean active;
    private Date timestamp;
    //	private GoogleAuthBean			googleAuth;
    private Set<CustomerAccessBean> access = new HashSet<CustomerAccessBean>(0);

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return active;
    }

    public boolean isActive() {
        return (active != null) ? active.booleanValue() : false;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

/*
    public GoogleAuthBean getGoogleAuth() {
		return googleAuth;
	}


	public void setGoogleAuth(GoogleAuthBean googleAuth) {
		this.googleAuth = googleAuth;
	}*/

    public Set<CustomerAccessBean> getAccess() {
        return access;
    }

    public void setAccess(Set<CustomerAccessBean> access) {
        this.access = access;
    }
}
