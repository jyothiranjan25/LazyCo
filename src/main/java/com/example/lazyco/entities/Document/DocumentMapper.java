package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper extends AbstractMapper<DocumentDTO, Document> {}
