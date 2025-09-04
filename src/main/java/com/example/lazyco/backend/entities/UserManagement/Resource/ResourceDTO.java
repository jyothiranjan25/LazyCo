package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentRBACModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class ResourceDTO extends AbstractDTO<ResourceDTO> {
    private String name;
    private String description;
    private String url;
    private String type;
}