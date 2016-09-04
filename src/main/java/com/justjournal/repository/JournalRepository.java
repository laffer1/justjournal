package com.justjournal.repository;

import com.justjournal.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Journal Repository
 * @author Lucas Holt
 */
@Repository
public interface JournalRepository extends JpaRepository<Journal, Integer> {

    Journal findOneBySlug(@Param("slug") String slug);
}
