package com.justjournal.repository;

import com.justjournal.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
@RepositoryRestResource(exported = false)
public interface StyleRepository extends JpaRepository<Style, Integer> {

    Style findOneByTitle(@Param("title") String title);
}
