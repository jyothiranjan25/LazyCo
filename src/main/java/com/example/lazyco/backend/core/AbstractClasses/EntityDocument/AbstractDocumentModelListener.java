package com.example.lazyco.backend.core.AbstractClasses.Entity;

import java.util.Date;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AbstractDocumentModelListener
    extends AbstractMongoEventListener<AbstractDocumentModel> {

  @Override
  public void onBeforeSave(BeforeSaveEvent<AbstractDocumentModel> event) {
    AbstractDocumentModel source = event.getSource();
    if (source instanceof AbstractDocumentRBACModel modelBase) {
      if (modelBase.getUserGroup() == null) {
        modelBase.setUserGroup("DEFAULT");
      }
    }
    source.setCreatedBy("DEFAULT");
    source.setCreatedAt(new Date());
  }

  @Override
  public void onBeforeConvert(BeforeConvertEvent<AbstractDocumentModel> event) {
    AbstractDocumentModel source = event.getSource();
    source.setUpdatedBy("DEFAULT");
    source.setUpdatedAt(new Date());
  }
}
