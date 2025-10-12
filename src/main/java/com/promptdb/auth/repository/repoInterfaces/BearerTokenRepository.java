package com.promptdb.auth.repository.repoInterfaces;

import com.promptdb.auth.models.BearerTokenModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for accessing bearer token records stored in the database.
 * This interface provides methods to retrieve token-related data for authorization logic.
 */
public interface BearerTokenRepository extends CrudRepository<BearerTokenModel, Long> {

    /**
     * Retrieves a bearer token by its access token string, only if:
     * <ul>
     *     <li>The token has not expired (i.e., current time is before its expiry)</li>
     *     <li>The associated user is active (user.isActive = true)</li>
     *     <li>The user is not locked (user.isLocked = false)</li>
     * </ul>
     *
     * <p>
     * This query performs a {@code JOIN FETCH} to eagerly load the associated {@code UserModel}
     * for authorization checks, even though the entity association is configured as {@code LAZY}.
     * </p>
     *
     * @param accessToken the access token string
     * @return an Optional containing the BearerTokenModel and its UserModel if the token is valid and user meets conditions
     */
    @Query("""
            SELECT bt FROM BearerTokenModel bt
            JOIN FETCH bt.user u
            WHERE bt.accessToken = :access_token
              AND bt.accessTokenExpiresOn >= CURRENT_TIMESTAMP
              AND u.isActive = true
              AND u.isLocked = false
           """)
    BearerTokenModel getValidBearerToken(@Param("access_token") String accessToken);
}