package com.justjournal.repository;

import com.justjournal.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface StyleRepository extends JpaRepository<Style, Integer> {

    Style findOneByTitle(@Param("title") String title);

    List<Style> findAll();
}
