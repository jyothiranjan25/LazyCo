package com.example.lazyco.backend.core.AbstractClasses.JpaRepository;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractJpaRepository<T extends AbstractModel> extends JpaRepository<T, Long> {}