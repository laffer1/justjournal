package com.justjournal.repository;

import com.justjournal.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lucas Holt
 */
public interface StyleRepository extends JpaRepository<Style, Integer> {
}
