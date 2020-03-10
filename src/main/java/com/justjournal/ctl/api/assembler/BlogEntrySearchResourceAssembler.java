package com.justjournal.ctl.api.assembler;

import com.justjournal.ctl.api.entry.EntryController;
import com.justjournal.model.search.BlogEntry;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Lucas Holt
 */
@Component
public class BlogEntrySearchResourceAssembler implements RepresentationModelAssembler<BlogEntry, EntityModel<BlogEntry>> {

    @Override
    public EntityModel<BlogEntry> toModel(final BlogEntry blogEntry) {
        return new EntityModel(blogEntry,
                linkTo(EntryController.class).slash(blogEntry.getId()).withSelfRel());
    }
}