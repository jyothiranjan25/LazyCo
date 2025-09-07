package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractDocDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends PersistenceDocDAO<E> implements IAbstractDocDAO<D, E> {}
