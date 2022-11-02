package main.command;

import main.ATM;
import main.model.Account;
import main.service.Terminal;

public class InfoCommand implements Command {
    @Override
    public void execute() {
        Terminal.printMsg("Информация по счету");
        Account account = ATM.getAccount();
        int money = account.getMoney();

        if (money > 0) {
            Terminal.printMsg("На вашем счету " + money + " РУБ");
        } else if (money <= 0) {
            Terminal.printMsg("На вашем счету нет средств");
        }
    }
}
