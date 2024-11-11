package com.promptdb.auth.repository;


import com.promptdb.auth.models.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <UserModel, Long> {

    UserModel findById(long id);

    UserModel findByUsername(String username);
}
