package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractClasses.MapperDocument.AbstractDocumentMapper;
import com.example.lazyco.backend.core.AbstractClasses.ServiceDocument.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<ResourceDTO, Resource> {
    public ResourceService(ResourceMapper resourceMapper) {
        super(resourceMapper);
    }
}