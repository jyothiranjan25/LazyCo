package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import java.util.List;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.NESTED)
public class AbstractDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    implements IAbstractDAO<D, E> {

  public List<E> get(D filter, BiConsumer<?, D> addEntityFilters) {
    return List.of();
  }

  public List<D> get(D filter, AbstractMapper<D, E> mapper, BiConsumer<?, D> addEntityFilters) {
    return List.of();
  }

  @Override
  public Long getCount(D filter, BiConsumer<?, D> addEntityFilters) {
    return 0L;
  }
}
