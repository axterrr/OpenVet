package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.mapper.BaseMapper;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.BaseValidator;

import java.util.List;

@Transactional
public abstract class BaseService<ENTITY extends Identifiable<ID>, VIEW, RESPONSE, ID> {

    @Autowired
    protected BaseRepository<ENTITY, ID> repository;

    @Autowired
    protected BaseMapper<ENTITY, VIEW, RESPONSE> mapper;

    @Autowired
    protected BaseValidator<ENTITY, VIEW, ID> validator;

    public List<RESPONSE> getAll() {
        validator.validateForViewAll();
        List<ENTITY> result = repository.findAll();
        return mapper.mapToResponse(result);
    }

    public RESPONSE getById(ID id) {
        return mapper.mapToResponse(getEntity(id));
    }

    public ID create(VIEW view) {
        return createEntity(view).getId();
    }

    public void update(ID id, VIEW view) {
        updateEntity(id, view);
    }

    public void deleteById(ID id) {
        deleteEntity(id);
    }

    protected ENTITY createEntity(VIEW view) {
        validator.validateForCreate(view);
        ENTITY entity = mapper.mapToEntity(view);
        return repository.saveAndFlush(entity);
    }

    protected ENTITY updateEntity(ID id, VIEW view) {
        ENTITY entity = getEntity(id);
        validator.validateForUpdate(view, entity);
        mapper.merge(view, entity);
        repository.saveAndFlush(entity);
        return entity;
    }

    protected ENTITY deleteEntity(ID id) {
        ENTITY entity = getEntity(id);
        validator.validateForDelete(entity);
        repository.deleteById(entity.getId());
        return entity;
    }

    protected ENTITY getEntity(ID id) {
        ENTITY entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Entity with such id was not found"));
        validator.validateForView(entity);
        return entity;
    }
}
