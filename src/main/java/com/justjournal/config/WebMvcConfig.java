/*
Copyright (c) 2003-2021, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package com.justjournal.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties({ResourceProperties.class})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Autowired private ResourceProperties resourceProperties = new ResourceProperties();

  private ObjectMapper mapper;

  @Autowired
  public WebMvcConfig(ObjectMapper mapper) {
    super();

    this.mapper = mapper;
    mapper.registerModule(new Hibernate5Module());

    JaxbAnnotationModule module = new JaxbAnnotationModule();
    mapper.registerModule(module);
  }

  /**
   * Override default handlers. see
   * http://stackoverflow.com/questions/24837715/spring-boot-with-angularjs-html5mode
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    super.addResourceHandlers(registry);
    int cachePeriod = 300;

    registry
        .addResourceHandler("/robots.txt")
        .addResourceLocations("classpath:/static/robots.txt")
        .setCachePeriod(cachePeriod);

    registry
        .addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/")
        .setCachePeriod(cachePeriod);

    registry
        .addResourceHandler("/static/styles/**", "/styles/*.css", "/styles/*.map")
        .addResourceLocations("classpath:/static/styles/")
        .setCachePeriod(cachePeriod);

    // todo: move these files to an external source
    registry
        .addResourceHandler(
            "/software/*.zip", "/software/*.bz2", "/software/*.gz", "/software/unix/**")
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

  /** {@inheritDoc} */
  @Override
  public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    converters.add(new StringHttpMessageConverter());
    converters.add(new ByteArrayHttpMessageConverter());
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    return builder.build();
  }
}
