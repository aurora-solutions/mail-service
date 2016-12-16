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
