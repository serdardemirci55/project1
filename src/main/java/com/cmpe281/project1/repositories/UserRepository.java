package com.cmpe281.project1.repositories;

import com.cmpe281.project1.entity.UserFileDto;
import com.cmpe281.project1.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Serdar Demirci
 */
public interface UserRepository extends CrudRepository<Users, Long> {
    Users findByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT f.id, u.first_name AS firstName, u.last_name AS lastName, f.title, f.description, f.upload_time AS uploadTime, f.updated_time AS updatedTime FROM files f INNER JOIN users u on f.username = u.username")
    Iterable<UserFileDto> fetchUserFileInnerJoin();

    @Query(nativeQuery = true, value = "SELECT f.id, u.first_name AS firstName, u.last_name AS lastName, f.title, f.description, f.upload_time AS uploadTime, f.updated_time AS updatedTime  FROM files f INNER JOIN users u on f.username = u.username WHERE u.username = :username")
    Iterable<UserFileDto> fetchUserFileInnerJoinByUsername(@Param("username") String username);
}
