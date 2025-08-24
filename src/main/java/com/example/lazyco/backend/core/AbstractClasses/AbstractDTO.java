package com.example.lazyco.backend.core.AbstractClasses;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public abstract class AbstractDTO<D> implements Serializable,Cloneable {

    protected Long id;
    private List<D> objectsList;
    private Integer pageSize;
    private Integer pageOffset;
    private Long totalRecords;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
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
