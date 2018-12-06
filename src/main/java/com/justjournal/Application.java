package com.justjournal;

import com.justjournal.ctl.LoginAccount;
import com.justjournal.ctl.UpdateJournal;
import com.justjournal.ctl.XmlRpc;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@Configuration
@EnableScheduling
@EnableAsync
@EnableJpaRepositories
@SpringBootApplication(exclude = GsonAutoConfiguration.class)
public class Application {
    
    @Value("${tomcat.ajp.port}")
    int ajpPort;

    @Value("${tomcat.ajp.remoteauthentication}")
    private boolean remoteAuthentication;

    @Value("${tomcat.ajp.enabled}")
    private boolean tomcatAjpEnabled;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {

        final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        if (tomcatAjpEnabled) {
            final Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setProtocol("AJP/1.3");
            ajpConnector.setPort(ajpPort);
            ajpConnector.setSecure(false);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setScheme("http");
            ajpConnector.setAttribute("tomcatAuthentication", !remoteAuthentication);
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }

        return tomcat;
    }

    @Bean
    public ServletRegistrationBean updateJournal() {
        return new ServletRegistrationBean(new UpdateJournal(), "/updateJournal");
    }

    @Bean
    public ServletRegistrationBean loginAccount() {
        return new ServletRegistrationBean(new LoginAccount(), "/loginAccount");
    }

    @Bean
    public ServletRegistrationBean xmlrpc() {
        return new ServletRegistrationBean(new XmlRpc(), "/xml-rpc/*");
    }

}
