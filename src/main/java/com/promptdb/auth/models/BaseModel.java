package com.promptdb.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/*
annotate with mapped superclass for extending it.
similar to setting __abstract__ = True in SQLAlchemy
*/

@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Hibernate won't add these cols in create statement
    // value will be handled by db
    @Column(name = "created_on", nullable = false, insertable = false, updatable = false)
    private Date createdOn;

    @Column(name = "modified_on", nullable = false, insertable = false, updatable = false)
    private Date modifiedOn;
}
