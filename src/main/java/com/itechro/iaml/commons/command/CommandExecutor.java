package com.itechro.iaml.commons.command;

import com.itechro.iaml.exception.impl.AppsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Command implementation to execute multiple commands
 *
 * @author : chamara
 */
public abstract class CommandExecutor<T extends ExecutionContext> implements Command<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CommandExecutor.class);

    private LinkedList<Command<T>> commands = new LinkedList<>();

    @Override
    public T execute(T context) throws AppsException {
        T cnx = context;

        if (LOG.isDebugEnabled()) {
            LOG.debug(context.toString());
        }

        for (Command<T> command : commands) {
            LOG.debug("executing command {} stated", command.getClass());
            cnx = command.execute(cnx);
            LOG.debug("executing command {} ended", command.getClass());
        }
        return cnx;
    }

    public void addCommand(Command<T> command) {
        commands.add(command);
    }


}

