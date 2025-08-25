package com.example.lazyco.backend.core.AbstractClasses.Entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class AbstractModelListener {

    @PrePersist
    public void prePersist(AbstractModel source) {
        if(source instanceof AbstractModelBase modelBase){
            if(modelBase.getUserGroup() == null){
                modelBase.setUserGroup("DEFAULT");
            }
        }
        source.setCreatedBy("DEFAULT");
        source.setCreatedAt(new Date());
    }

    @PreUpdate
    public void preUpdate(AbstractModel source) {
        source.setUpdatedBy("DEFAULT");
        source.setUpdatedAt(new Date());
    }
}
