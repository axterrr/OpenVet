package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;

import java.util.List;

@Transactional
public abstract class BaseService<ENTITY extends Identifiable<ID>, REQUEST, ID> {

    @Autowired
    protected BaseRepository<ENTITY, ID> repository;
    @Autowired
    protected BaseMerger<ENTITY, REQUEST> merger;
    @Autowired
    protected BaseValidator<ENTITY, REQUEST> validator;
    @Autowired
    protected BasePermissionValidator<ENTITY, REQUEST> permissionValidator;

    protected abstract ENTITY newEntity();

    public List<ENTITY> getAll() {
        permissionValidator.validateForGetAll();
        return repository.findAll();
    }

    public ENTITY getById(ID id) {
        ENTITY entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Entity with such id was not found"));
        permissionValidator.validateForGet(entity);
        return entity;
    }

    public ENTITY create(REQUEST request) {
        permissionValidator.validateForCreate(request);
        validator.validateForCreate(request);
        ENTITY entity = newEntity();
        merger.mergeCreate(entity, request);
        return repository.saveAndFlush(entity);
    }

    public ENTITY update(ID id, REQUEST request) {
        ENTITY entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        validator.validateForUpdate(request, entity);
        merger.mergeUpdate(entity, request);
        return repository.saveAndFlush(entity);
    }

    public ENTITY deleteById(ID id) {
        ENTITY entity = getById(id);
        permissionValidator.validateForDelete(entity);
        validator.validateForDelete(entity);
        repository.deleteById(entity.getId());
        return entity;
    }
}
