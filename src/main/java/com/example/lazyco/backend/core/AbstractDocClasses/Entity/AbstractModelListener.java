package com.example.lazyco.backend.core.AbstractDocClasses.Entity;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AbstractModelListener extends AbstractMongoEventListener<AbstractModel> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<AbstractModel> event) {
        AbstractModel source = event.getSource();
        if (source instanceof AbstractRBACModel modelBase) {
            if (modelBase.getUserGroup() == null) {
                modelBase.setUserGroup("DEFAULT");
            }
        }
        source.setCreatedBy("DEFAULT");
        source.setCreatedAt(new Date());
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AbstractModel> event) {
        AbstractModel source = event.getSource();
        source.setUpdatedBy("DEFAULT");
        source.setUpdatedAt(new Date());
    }
}
