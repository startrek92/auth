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
@Table(name = "company")
public class CompanyModel extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("1")
    private Boolean isActive;

}
