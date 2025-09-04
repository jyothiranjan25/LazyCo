package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("resource")
public class Resource extends AbstractModel {

    private String name;

    private String description;

    private String url;

    private String type;
}