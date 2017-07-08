package com.justjournal.repository;

import com.justjournal.model.UserPic;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface UserPicRepository extends PagingAndSortingRepository<UserPic, Integer> {

}
