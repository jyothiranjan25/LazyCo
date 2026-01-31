package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import java.util.List;

@Getter
@Setter
public class ResourceDTO extends AbstractModel {

    private String resourceName;
    private String description;
    private Integer resourceOrder;
    private List<ResourceDTO> childrenResources;
}