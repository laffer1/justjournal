package com.justjournal.repository;

import com.justjournal.model.UserImage;
import com.justjournal.model.UserPic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface UserPicRepository extends CrudRepository<UserPic, Integer> {

}
