package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDTO extends AbstractDTO<ResourceDTO> {
    private String name;
    private String description;
    private String url;
    private String type;
}