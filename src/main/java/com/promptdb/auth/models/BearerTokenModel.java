package com.promptdb.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bearer_token")
public class BearerTokenModel extends BaseModel{

    @Column(name = "access_token", nullable = false, unique = true, columnDefinition = "LONGTEXT")
    private String accessToken;

    @Column(name = "refresh_token", nullable = true, unique = true, columnDefinition = "LONGTEXT")
    private String refreshToken;

    @Column(name = "access_token_expires_on", nullable = false)
    private Date accessTokenExpiresOn;

    @Column(name = "refresh_token_expires_on", nullable = true)
    private Date refreshTokenExpiresOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_bearer_token_user"))
    private UserModel user;

}
