package com.example.lazyco.backend.core.AbstractClasses.ServiceDocument;

import com.example.lazyco.backend.core.AbstractClasses.DAODocument.IAbstractDocumentDAO;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractClasses.MapperDocument.AbstractDocumentMapper;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractDocumentModel>
    implements IAbstractService<D, E> {

    private final AbstractDocumentMapper<D, E> abstractMapper;
    private IAbstractDocumentDAO<D, E> abstractDAO;
    @Getter
    private final Class<D> dtoClass;

    public AbstractService(AbstractDocumentMapper<D, E> abstractMapper) {
        this.abstractMapper = abstractMapper;
        this.dtoClass = calculateDTOClass();
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

    @Autowired
    private void injectDependencies(IAbstractDocumentDAO<D, E> abstractDAO) {
        this.abstractDAO = abstractDAO;
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
        return null;
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
