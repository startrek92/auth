package com.promptdb.auth.repository.repoInterfaces;

import com.promptdb.auth.models.BearerTokenModel;
import org.springframework.data.repository.CrudRepository;

public interface BearerTokenRepository extends CrudRepository<BearerTokenModel, Long> {
}
