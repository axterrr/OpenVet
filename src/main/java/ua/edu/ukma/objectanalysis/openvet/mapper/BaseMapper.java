package ua.edu.ukma.objectanalysis.openvet.mapper;

import java.util.List;

public interface BaseMapper<ENTITY, VIEW, RESPONSE> {

    ENTITY mapToEntity(VIEW view);

    void merge(VIEW view, ENTITY entity);

    RESPONSE mapToResponse(ENTITY entity);

    default List<RESPONSE> mapToResponse(List<ENTITY> list) {
        if (list == null) return List.of();
        return list.stream()
            .map(this::mapToResponse)
            .toList();
    }
}
