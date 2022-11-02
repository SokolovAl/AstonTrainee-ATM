package main.command;

import main.exception.InterruptedOperationException;
import main.service.Operation;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final Map<Operation, Command> operations = new HashMap<>();

    static {
        operations.put(Operation.LOGIN, new LoginCommand());
        operations.put(Operation.INFO, new InfoCommand());
        operations.put(Operation.DEPOSIT, new DepositCommand());
        operations.put(Operation.WITHDRAW, new WithdrawCommand());
        operations.put(Operation.EXIT, new ExitCommand());
    }

    public static void execute(Operation operation) throws InterruptedOperationException {
        operations.get(operation).execute();
    }
}
