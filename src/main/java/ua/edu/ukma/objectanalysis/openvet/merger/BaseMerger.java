package ua.edu.ukma.objectanalysis.openvet.merger;

public interface BaseMerger<ENTITY, REQUEST> {
    void merge(ENTITY entity, REQUEST request);
}
