package com.betterchoice.core.crud;

import com.betterchoice.core.dto.PageResponse;
import com.betterchoice.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractCrudService<E, REQ, RES, ID> implements CrudService<REQ, RES, ID> {

    protected abstract JpaRepository<E, ID> repository();

    protected abstract RES toResponse(E entity);

    protected abstract E toEntity(REQ request);

    protected abstract void updateEntity(E entity, REQ request);

    protected abstract String entityName();

    @Override
    @Transactional
    public RES create(REQ request) {
        E saved = repository().save(toEntity(request));
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(ID id) {
        return toResponse(findEntityOrThrow(id));
    }

    @Override
    @Transactional
    public RES update(ID id, REQ request) {
        E entity = findEntityOrThrow(id);
        updateEntity(entity, request);
        return toResponse(repository().save(entity));
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!repository().existsById(id)) {
            throw notFound(id);
        }
        repository().deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RES> list(Pageable pageable) {
        Page<E> page = repository().findAll(pageable);
        return PageResponse.from(page, this::toResponse);
    }

    protected E findEntityOrThrow(ID id) {
        return repository().findById(id).orElseThrow(() -> notFound(id));
    }

    protected BusinessException notFound(ID id) {
        return new BusinessException(
                "NOT_FOUND",
                entityName() + " not found: " + id,
                HttpStatus.NOT_FOUND
        );
    }
}
