package com.justjournal;

import com.justjournal.ctl.LoginAccount;
import com.justjournal.ctl.UpdateJournal;
import com.justjournal.ctl.XmlRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@Configuration
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories
@SpringBootApplication(exclude = {GsonAutoConfiguration.class, XADataSourceAutoConfiguration.class, SolrAutoConfiguration.class,
        SolrRepositoriesAutoConfiguration.class, Neo4jRepositoriesAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean updateJournalServlet() {
        return new ServletRegistrationBean(new UpdateJournal(), "/updateJournal");
    }

    @Bean
    public ServletRegistrationBean loginAccountServlet() {
        return new ServletRegistrationBean(new LoginAccount(), "/loginAccount");
    }

    @Bean
    public ServletRegistrationBean xmlrpc() {
        return new ServletRegistrationBean(new XmlRpc(), "/xml-rpc/*");
    }

}
