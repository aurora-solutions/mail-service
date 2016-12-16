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


package com.aurora.mail.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

/**
 * Added by Rasheed.
 */
@Configuration
@EnableSwagger
public class SwaggerConfiguration implements EnvironmentAware {
    public static final String DEFAULT_INCLUDE_PATTERN = "/app/rest/.*";

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    /**
     * Swagger Spring MVC configuration
     */
    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .includePatterns(DEFAULT_INCLUDE_PATTERN);
    }

    /**
     * API Info as it appears on the swagger-ui page
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                propertyResolver.getProperty("title"),
                propertyResolver.getProperty("description"),
                propertyResolver.getProperty("termsOfServiceUrl"),
                propertyResolver.getProperty("contact"),
                propertyResolver.getProperty("license"),
                propertyResolver.getProperty("licenseUrl"));
    }
}