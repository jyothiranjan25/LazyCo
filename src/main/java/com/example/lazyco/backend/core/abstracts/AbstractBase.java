package com.example.lazyco.backend.core.abstracts;

import com.example.lazyco.backend.core.databaseconf.schema.AbstractBaseSchema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractBase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "hilo_generator")
    @TableGenerator(
            name = "hilo_generator",
            table = "hibernate_sequences",
            pkColumnName = "sequence_name",
            valueColumnName = "next_hi",
            allocationSize = 1)
    @Column(name = AbstractBaseSchema.ID)
    private Long id;

    @CreationTimestamp
    @Column(name = AbstractBaseSchema.CREATED_AT)
    private Date createAt;

    @UpdateTimestamp
    @Column(name = AbstractBaseSchema.UPDATED_AT)
    private Date updateAt;

    @Column(name = AbstractBaseSchema.CREATED_BY)
    private String createdBy;

    @Column(name = AbstractBaseSchema.UPDATED_BY)
    private String updatedBy;
}