package main.command;

import main.ATM;
import main.exception.InterruptedOperationException;
import main.model.Account;
import main.service.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginCommand implements Command {
    private final ResourceBundle verifiedCards = ResourceBundle.getBundle("verifiedCards");

    List<Account> accounts = new ArrayList<>();

    @Override
    public void execute() throws InterruptedOperationException {
        System.out.println("Аутентификация");

        while (true) {
            System.out.println("Введите номер карты и пин код или наберите 'EXIT' для прекращения работы (действует в любом месте)");

            String cardNum = Terminal.readString();
            String pinCode = Terminal.readString();

            if (verifiedCards.containsKey(cardNum)) {
                if (verifiedCards.getString(cardNum).equals(pinCode)) {
                    Terminal.printMsg("Карта идентифицирована");
                    ATM atm = ATM.getATM();
                    accounts = atm.loadAccounts();
                    for (Account a : accounts) {
                        if (a.getCardNum() == Long.parseLong(cardNum)) {
                            ATM.setAccount(a);
                            atm.writeListAccounts(accounts);
                            break;
                        }
                    }
                    if (ATM.getAccount() == null) {
                        Terminal.printMsg("Вы впервые пользуетесь данным банкоматом");
                        ATM.setAccount(new Account(Long.parseLong(cardNum)));
                        Terminal.printMsg("Карта успешно активирована");
                        accounts.add(ATM.getAccount());
                        atm.writeListAccounts(accounts);
                    }
                } else {
                    Terminal.printMsg("Карта не идентифицирована. Пропробуйте снова.");
                    continue;
                }
            } else {
                Terminal.printMsg("Карта не идентифицирована. Пропробуйте снова.\nНомер карты - 12 цифр, пи-код - 4 цифры");
                continue;
            }
            break;
        }
    }
}
