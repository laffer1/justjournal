package com.justjournal.repository;

import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Lucas Holt
 */
public interface EntryStatisticRepository extends JpaRepository<EntryStatistic, Integer> {

    @Query("select et from EntryStatistic et, User u where et.user = u and LOWER(u.username) = LOWER(:username)")
    List<EntryStatistic> findByUsernameOrderByYearDesc(@Param("username") String username);

    @Query("select et from EntryStatistic et, User u where et.user = u and LOWER(u.username) = LOWER(:username) and et.year=:year")
    EntryStatistic findByUsernameAndYear(@Param("username") String username, @Param("year") int year);

    EntryStatistic findByUserAndYear(@Param("user") User user, @Param("year") int year);

}
