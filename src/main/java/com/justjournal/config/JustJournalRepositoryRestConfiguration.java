package com.justjournal.config;

import com.justjournal.model.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Expose ids for some classes
 *
 * @author Lucas Holt
 */
@Configuration
public class JustJournalRepositoryRestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config) {

        config.exposeIdsFor(Comment.class, Entry.class, Mood.class, Location.class, Security.class, Style.class);
    }
}