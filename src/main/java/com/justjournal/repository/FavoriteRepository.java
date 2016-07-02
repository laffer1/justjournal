package com.justjournal.repository;

import com.justjournal.model.Entry;
import com.justjournal.model.Favorite;
import com.justjournal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser(@Param("user") User user);

    List<Favorite> findByEntry(@Param("entry") Entry entry);

    Favorite findByUserAndEntry(@Param("user") User user, @Param("entry") Entry entry);
}
