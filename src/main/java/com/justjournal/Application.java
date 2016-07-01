package com.justjournal;

import com.justjournal.ctl.Image;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.Servlet;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {
	@Value("${tomcat.ajp.port}")
	int ajpPort;

	@Value("${tomcat.ajp.remoteauthentication}")
	private boolean remoteAuthentication;

	@Value("${tomcat.ajp.enabled}")
	private boolean tomcatAjpEnabled;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {

		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		if (tomcatAjpEnabled)
		{
			Connector ajpConnector = new Connector("AJP/1.3");
			ajpConnector.setProtocol("AJP/1.3");
			ajpConnector.setPort(ajpPort);
			ajpConnector.setSecure(false);
			ajpConnector.setAllowTrace(false);
			ajpConnector.setScheme("http");
			ajpConnector.setAttribute("tomcatAuthentication",!remoteAuthentication);
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
		}

		return tomcat;
	}
}
