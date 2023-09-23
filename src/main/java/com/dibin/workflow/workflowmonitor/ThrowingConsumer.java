package com.dibin.workflow.workflowmonitor;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
