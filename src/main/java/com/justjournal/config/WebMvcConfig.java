package com.justjournal.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableConfigurationProperties({ ResourceProperties.class })
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ResourceProperties resourceProperties = new ResourceProperties();

    public WebMvcConfig() {
        super();
    }

    /**
     * Override default handlers.
     * see http://stackoverflow.com/questions/24837715/spring-boot-with-angularjs-html5mode
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        Integer cachePeriod = resourceProperties.getCachePeriod();

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
       * <p>mappingJackson2HttpMessageConverter.</p>
       *
       * @return a {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter} object.
       */
      @Bean
      public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
          final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
          mappingJackson2HttpMessageConverter.setPrefixJson(false);
          mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());
          mappingJackson2HttpMessageConverter.setPrettyPrint(false);
          return mappingJackson2HttpMessageConverter;
      }

      /**
       * <p>objectMapper.</p>
       *
       * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} object.
       */
      @Bean
      public ObjectMapper objectMapper() {
          final ObjectMapper objMapper = new ObjectMapper();
          objMapper.enable(SerializationFeature.INDENT_OUTPUT);
          objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
          objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // attempt to write dates as a string!
          return objMapper;
      }

      /** {@inheritDoc} */
      @Override
      public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
          super.configureMessageConverters(converters);
          converters.add(new StringHttpMessageConverter());
          converters.add(new ByteArrayHttpMessageConverter());
          converters.add(mappingJackson2HttpMessageConverter());
      }

}