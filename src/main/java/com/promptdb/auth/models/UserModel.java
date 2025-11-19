package com.promptdb.auth.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class UserModel extends BaseModel implements UserDetails {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_company_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_user_company_id"))
    private CompanyModel companyModel;

    @Column(name = "failed_login_count", nullable = false)
    @ColumnDefault(value = "0")
    private Integer failedLoginCount;

    @Column(name = "is_locked", nullable = false)
    @ColumnDefault(value = "0")
    private Boolean isLocked;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault(value = "1")
    private Boolean isActive;

    public UserModel(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @JsonIgnore
    public HashMap<String, Object> getLoginJwtClaims() {
        HashMap<String, Object> claims = new HashMap<>();
        CompanyModel companyModel1 = this.getCompanyModel();
        claims.put("company_id", companyModel1.getId());
        claims.put("company_name", companyModel1.getName());
        claims.put("age", this.getAge());
        claims.put("modified_on", this.getModifiedOn());
        claims.put("created_on", this.getCreatedOn());
        return claims;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
