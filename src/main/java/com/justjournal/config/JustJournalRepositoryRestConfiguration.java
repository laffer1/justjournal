package com.justjournal.config;

import com.justjournal.model.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Expose ids for some classes
 * @author Lucas Holt
 */
@Configuration
public class JustJournalRepositoryRestConfiguration extends RepositoryRestMvcConfiguration {

    /** {@inheritDoc} */
    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);

        config.exposeIdsFor(Comment.class);
        config.exposeIdsFor(Entry.class);
        config.exposeIdsFor(Mood.class);
        config.exposeIdsFor(Location.class);
        config.exposeIdsFor(Security.class);

    }
}