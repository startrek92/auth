package com.promptdb.auth.repository.repoInterfaces;


import com.promptdb.auth.models.UserModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository <UserModel, Long> {

    UserModel findById(long id);

    UserModel findByUsername(String username);

    @Query(value = "SELECT u FROM user u WHERE u.companyModel.id = :company_id")
    List<UserModel> getByCompanyId( @Param("company_id") Integer companyId);

    @Query(
            value = "SELECT * FROM user WHERE fk_company_id = :company_id AND (username LIKE %:search_filter% OR email LIKE %:search_filter%)",
            nativeQuery = true)
    List<UserModel> getByCompanyId(@Param("company_id") Integer companyId,
                                   @Param("search_filter") String searchFilter);
}
