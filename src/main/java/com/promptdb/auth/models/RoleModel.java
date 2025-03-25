package com.promptdb.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "role")
@Table(name = "role")
public class RoleModel extends BaseModel {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;
}
