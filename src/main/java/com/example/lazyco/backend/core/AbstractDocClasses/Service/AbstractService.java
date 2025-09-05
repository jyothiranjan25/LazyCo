package com.example.lazyco.backend.core.AbstractDocClasses.Service;

import com.example.lazyco.backend.core.AbstractDocClasses.DAO.IAbstractDocDAO;
import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional("mongoTransactionManager")
public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    implements IAbstractService<D, E> {

  private final AbstractMapper<D, E> abstractMapper;
  private IAbstractDocDAO<D, E> abstractDAO;
  @Getter private final Class<D> dtoClass;

  protected AbstractService(AbstractMapper<D, E> abstractMapper) {
    this.abstractMapper = abstractMapper;
    dtoClass = this.calculateDTOClass();
  }

  @Autowired
  private void injectDependencies(IAbstractDocDAO<D, E> abstractDAO) {
    this.abstractDAO = abstractDAO;
  }

  @SuppressWarnings("unchecked")
  private Class<D> calculateDTOClass() {
    Type superClass = getClass().getGenericSuperclass();
    if (superClass instanceof ParameterizedType parameterizedType) {
      Type type = parameterizedType.getActualTypeArguments()[0];
      if (type instanceof Class) {
        return (Class<D>) type;
      } else if (type instanceof ParameterizedType) {
        return (Class<D>) ((ParameterizedType) type).getRawType();
      }
    }
    throw new ExceptionWrapper("Unable to determine DTO class");
  }

  @Override
  public List<E> getEntities(D filters) {
    return List.of();
  }

  @Override
  public E getSingleEntity(D filter) {
    return null;
  }

  @Override
  public E getEntityById(Long id) {
    return null;
  }

  @Override
  public D getSingle(D filter) {
    return null;
  }

  @Override
  public D getById(Long id) {
    return null;
  }

  @Override
  public Long getCount(D filter) {
    return 0L;
  }

  @Override
  public D create(D dto) {
    E entity = abstractMapper.map(dto);
    E savedEntity = abstractDAO.save(entity);
    return abstractMapper.map(savedEntity);
  }

  @Override
  public D delete(D dto) {
    return null;
  }

  @Override
  public List<D> get(D dto) {
    return List.of();
  }

  @Override
  public D update(D dto) {
    return null;
  }
}
