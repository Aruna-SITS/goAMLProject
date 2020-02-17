package com.itechro.iaml.commons.command;

import com.itechro.iaml.exception.impl.AppsException;

/**
 * Command Interface
 *
 * @author : chamara
 */
public interface Command<T extends ExecutionContext> {

    public T execute(T context) throws AppsException;
}
