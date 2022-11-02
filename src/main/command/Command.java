package main.command;

import main.exception.InterruptedOperationException;

public interface Command {
    void execute() throws InterruptedOperationException;
}
