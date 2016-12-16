# Project Setup #

###Setting up the Environment###
In order to run the project you need:

- _JAVA 1.7_
- _MAVEN 3.0+_

###Running through MAVEN Command Line###
To run the project use command

- _mvn spring-boot:run_

Explore [Mail API](http://localhost:8090/docs/index.html) and then click on "List Operations". And it will give you all details & save your
time from looking into code to find details of API! (Please note you can't Try Out as there is no real backend available)

To run the test cases use command

- _mvn clean test_
All tests use mocked Repositories. Both unit & integration tests have been written.

###Sender Email Settings###

Please ensure to provide valid settings for the sender email address in the file application.properties

###Project Structure###

1. `src/main/java/com/aurora/mail/api` : REST controller and the entry point
2. `src/main/java/com/aurora/mail/beans` : Beans
3. `src/main/java/com/aurora/mail/config` : Java based configurations
4. `src/main/java/com/aurora/mail/repository` : Dummy repositories which will be replaced with real ones.
5. `src/main/java/com/aurora/mail/service` : Granular infrastructure services to send emails
6. `src/main/resource/mails` : Email templates
7. `src/main/resource/i18n` : I18N files
8. `src/main/resource/application.properties` : Externalized properties for mail settings, logging,  etc.
9. `src/main/resource/logback.xml` : Specify loggers.

###Log Files!###

Log files can be accessed here: `logs/mail.log`

###Running in IDE (Optional)###

- Setup IntelliJ
- Open IntelliJ
- Go to File->Open and select the pom file
- Further actions (like running the test cases) can be performed using the IDE controls

###Salient Features###

- All Java based configurations
- Async Emails
- i18n: Internationalization to support different languages.
- Spring-Boot
- Embedded Tomcat
- SFL4J with LogBack for logging

###API / Libraries###

[Flying Saucer](https://code.google.com/p/flying-saucer/) is a pure java library that takes XML or XHTML and applies CSS 2.1-compliant stylesheets to it, in 
order to render to PDF (via iText), images, and on-screen using Swing or SWT. The library implements (basically) the 
entirety of CSS 2.1 and aims to be fully compliant with the W3C specification; it includes a small handful 
of CSS 3 features.

[Thymeleaf](http://www.thymeleaf.org/) Thymeleaf is a Java library. It is an XML / XHTML / HTML5 template engine (extensible to other formats) 
that can work both in web and non-web environments. It is better suited for serving XHTML/HTML5 at the view layer of 
web applications, but it can process any XML file even in offline environments.

[logback](http://logback.qos.ch/) Logging API.

[Mockito](https://code.google.com/p/mockito/) Mockito is a mocking framework that tastes really good. It lets you write beautiful tests with clean & simple API. 

[JUnit](http://junit.org/) JUnit is a simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.

[Wiser](https://code.google.com/p/subethasmtp/wiki/Wiser) Wiser is a simple SMTP server that you can use for unit testing applications that send mail.

[Swagger](http://swagger.io/) The goal of Swagger is to define a standard, language-agnostic interface to REST APIs which allows both humans 
and computers to discover and understand the capabilities of the service without access to source code, documentation, 
or through network traffic inspection.

[Swagger-UI](https://github.com/wordnik/swagger-ui) is a good-looking JavaScript client for Swagger's JSON.