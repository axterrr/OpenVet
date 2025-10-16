package ua.edu.ukma.objectanalysis.openvet.merger;

public interface BaseMerger<ENTITY, REQUEST> {
    void mergeCreate(ENTITY entity, REQUEST request);
    void mergeUpdate(ENTITY entity, REQUEST request);
}
