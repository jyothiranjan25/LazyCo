package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Module.class)
public class ModuleDTO extends AbstractDTO<ModuleDTO> {}
