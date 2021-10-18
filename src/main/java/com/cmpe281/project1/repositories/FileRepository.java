package com.cmpe281.project1.repositories;

import com.cmpe281.project1.entity.Files;
import org.springframework.data.repository.CrudRepository;


public interface FileRepository extends CrudRepository<Files, Long> {
    Files findById(Integer id);
    Files findByTitle(String title);
    Iterable<Files> findByUsername(String username);
}
