package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractClasses.MapperDocument.AbstractDocumentMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper extends AbstractDocumentMapper<ResourceDTO, Resource> {}
