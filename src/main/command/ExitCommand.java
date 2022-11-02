package main.command;

import main.service.Terminal;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        Terminal.printExitMsg();
    }
}
