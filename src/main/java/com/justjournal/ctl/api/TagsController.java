/*
 * Copyright (c) 2013 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.justjournal.ctl.api;

import com.justjournal.model.Tag;
import com.justjournal.repository.EntryTagsRepository;
import com.justjournal.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tags
 *
 * @author Lucas Holt
 */
@RestController
@RequestMapping("/api/tags")
public class TagsController {

    private final TagRepository tagDao;

    private final EntryTagsRepository entryTagsRepository;

    @Autowired
    public TagsController(final TagRepository tagDao, final EntryTagsRepository entryTagsRepository) {
        this.tagDao = tagDao;
        this.entryTagsRepository = entryTagsRepository;
    }

    /**
     * Get the tag list for the whole site
     *
     * @return tag list
     */
    @Cacheable("tags")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Collection<Tag>> getTags() {

        final Map<String, Tag> tags = new HashMap<String, Tag>();

        final Iterable<Tag> tagList = tagDao.findAll();
        for (final Tag t : tagList) {
            if (!tags.containsKey(t.getName())) {
                final long count = entryTagsRepository.countByTag(t);
                t.setCount(count);
                tags.put(t.getName(), t);
            }
        }

        return ResponseEntity.ok()
                .eTag(Integer.toString(tags.values().hashCode()))
                .body(tags.values());
    }

    /**
     * Get an individual tag
     *
     * @param id tag id
     * @return tag list
     */
    @Cacheable(value = "tags", key = "#id")
    @RequestMapping(value = "/api/tags/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Tag> getById(@PathVariable("id") final Integer id) {

        final Tag tag = tagDao.findOne(id);

        if (tag == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok()
                .eTag(Integer.toString(tag.hashCode()))
                .body(tag);
    }
}
