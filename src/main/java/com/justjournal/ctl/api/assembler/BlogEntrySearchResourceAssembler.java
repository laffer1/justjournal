package com.justjournal.ctl.api.assembler;

import com.justjournal.ctl.api.entry.EntryController;
import com.justjournal.ctl.api.SearchController;
import com.justjournal.model.search.BlogEntry;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Lucas Holt
 */
@Component
public class BlogEntrySearchResourceAssembler extends ResourceAssemblerSupport<BlogEntry, Resource> {

    public BlogEntrySearchResourceAssembler() {
        super(SearchController.class, Resource.class);
    }

    @Override
    public List<Resource> toResources(final Iterable<? extends BlogEntry> blogEntries) {
        final List<Resource> resources = new ArrayList<Resource>();
        for (final BlogEntry f : blogEntries) {
            resources.add(new Resource(f,
                    linkTo(EntryController.class).slash(f.getId()).withSelfRel()));
        }
        return resources;
    }

    @Override
    public Resource toResource(final BlogEntry blogEntry) {
        return new Resource(blogEntry,
                linkTo(EntryController.class).slash(blogEntry.getId()).withSelfRel());
    }
}