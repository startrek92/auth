package com.promptdb.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_role")
@Table(name = "user_role")
public class UserRolesModel extends BaseModel {

    @JoinColumn(
            name = "fk_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id_user")
    )
    @ManyToOne(fetch = FetchType.LAZY)
    UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_id_role")
    )
    RoleModel role;
}
