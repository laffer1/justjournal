package com.justjournal.repository.search;

import com.justjournal.model.search.BlogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Lucas Holt
 */
public interface BlogEntryRepository extends ElasticsearchRepository<BlogEntry, Integer> {

    Page<BlogEntry> findBySubjectContainsOrBodyContainsAllIgnoreCase(@Param("subject") String subject,
                                        @Param("body") String body,
                                        Pageable pageable);

    Page<BlogEntry> findByAuthor(@Param("author") String author, Pageable pageable);

    Page<BlogEntry> findByAuthorAndSubjectContainsOrBodyContainsAllIgnoreCase(@Param("author") String author,
                                                 @Param("subject") String subject,
                                                 @Param("body") String body,
                                                 Pageable pageable);

}