package com.justjournal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableConfigurationProperties({ResourceProperties.class})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ResourceProperties resourceProperties = new ResourceProperties();

    private ObjectMapper mapper;

    @Autowired
    public WebMvcConfig(ObjectMapper mapper) {
        super();

        this.mapper = mapper;
        mapper.registerModule(new Hibernate5Module());
    }

    /**
     * Override default handlers. see http://stackoverflow.com/questions/24837715/spring-boot-with-angularjs-html5mode
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        int cachePeriod = 300;

        registry.addResourceHandler("/robots.txt")
                .addResourceLocations("classpath:/static/robots.txt")
                .setCachePeriod(cachePeriod);

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(cachePeriod);

        registry.addResourceHandler("/static/styles/**", "/styles/*.css", "/styles/*.map")
                .addResourceLocations("classpath:/static/styles/")
                .setCachePeriod(cachePeriod);

        // todo: move these files to an external source
        registry.addResourceHandler("/software/*.zip", "/software/*.bz2", "/software/*.gz", "/software/unix/**")
                .addResourceLocations("classpath:/static/software/")
                .setCachePeriod(cachePeriod);

        /* one possible html5 mode solution.
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/index.html")
                .setCachePeriod(cachePeriod).resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath,
                            Resource location) throws IOException {
                        return location.exists() && location.isReadable() ? location
                                : null;
                    }
                });
                */
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new StringHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());
    }

}