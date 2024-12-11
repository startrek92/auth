package com.promptdb.auth.repository;


import com.promptdb.auth.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <UserModel, Long> {

    UserModel findById(long id);

    UserModel findByUsername(String username);
}
