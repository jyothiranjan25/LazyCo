package com.example.lazyco.backend.core.abstracts;

import com.example.lazyco.schema.database.AbstractBaseSchema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractBase implements Serializable,Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "hilo_generator")
    @TableGenerator(
            name = "hilo_generator",
            table = AbstractBaseSchema.HIBERNATE_SEQUENCES,
            pkColumnName = "sequence_name",
            valueColumnName = "next_hi",
            allocationSize = 1)
    @Column(name = AbstractBaseSchema.ID)
    private Long id;

    @Column(name = AbstractBaseSchema.CREATED_AT)
    private Date createAt;

    @Column(name = AbstractBaseSchema.UPDATED_AT)
    private Date updateAt;

    @Column(name = AbstractBaseSchema.CREATED_BY)
    private String createdBy;

    @Column(name = AbstractBaseSchema.UPDATED_BY)
    private String updatedBy;

    @Override
    public Object clone() {
        try {
            Object o1 = super.clone();
            // SerializationUtils.clone performs a deep clone
            return SerializationUtils.clone(this);
        }catch (Exception e){
            return null;
        }
    }
}