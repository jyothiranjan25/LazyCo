package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import java.util.Set;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
        name = "resource",
        comment = "Table storing application resources")
@EntityListeners(ResourceListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends AbstractModel {

    @Column(name = "resource_name", comment = "Name of the resource")
    private String resourceName;

    @Column(name = "description", comment = "Description of the resource")
    private String description;

    @Column(name = "resource_order", comment = "Order of the resource for sorting purposes")
    private Integer resourceOrder;

    @ManyToOne
    @JoinColumn(name = "parent_resource_id", comment = "Reference to the parent resource")
    private Resource parentResource;

    @OneToMany(mappedBy = "parentResource")
    private Set<Resource> childResources;
}