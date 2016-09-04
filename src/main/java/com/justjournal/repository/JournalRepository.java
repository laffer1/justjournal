package com.justjournal.repository;

import com.justjournal.model.Journal;
import com.justjournal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Journal Repository
 *
 * @author Lucas Holt
 */
@Repository
public interface JournalRepository extends JpaRepository<Journal, Integer> {

    Journal findOneBySlug(@Param("slug") String slug);

    List<Journal> findByUser(@Param("user") User user);

    @Query("select ul from Journal ul, User u where ul.user = u and LOWER(u.username) = LOWER(:username)")
    List<Journal> findByUsername(@Param("username") String username);
}
