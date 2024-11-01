package com.promptdb.auth.repository;


import com.promptdb.auth.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, Long> {

    User findById(long id);
}
