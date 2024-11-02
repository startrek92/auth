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
@Table(name = "user")
public class UserModel extends BaseModel {

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

    @ManyToOne(fetch = FetchType.LAZY)
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
}
