package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentRBACModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("resource")
public class Resource extends AbstractDocumentRBACModel {

    private String name;

    private String description;

    private String url;

    private String type;
}