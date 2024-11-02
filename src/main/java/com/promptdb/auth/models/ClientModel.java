package com.promptdb.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class ClientModel extends BaseModel {

    private enum GrantTypeEnum {
        IMPLICIT("implicit"),
        CLIENT_CREDENTIALS("client_credentials"),
        AUTHORIZATION_CODE("authorization_code");
        private final String value;
        GrantTypeEnum(String value) {
            this.value = value;
        }
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "grant_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GrantTypeEnum grantType;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault(value = "1")
    private Boolean isActive;
}
