package com.promptdb.auth.repository.repoInterfaces;

import com.promptdb.auth.models.CompanyModel;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<CompanyModel, Long> {

    CompanyModel findById(long id);
}
