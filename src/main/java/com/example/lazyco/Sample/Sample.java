package com.example.lazyco.Sample;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sample")
@EntityListeners(SampleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sample extends AbstractModel {}
