package main.command;

import main.ATM;
import main.exception.InterruptedOperationException;
import main.model.Account;
import main.service.Terminal;

import java.util.HashMap;
import java.util.Map;

public class DepositCommand implements Command {
    private Map<Integer, Integer> moneyInATM = new HashMap<>();

    @Override
    public void execute() throws InterruptedOperationException {
        Account account = ATM.getAccount();
        Terminal.printMsg("Внесение средств");
        String yes = "y";

        while (yes.equalsIgnoreCase("y")) {
            String[] denominationAndAmount = Terminal.getBanknotes();
            try {
                int denomination = Integer.parseInt(denominationAndAmount[0]);
                int amount = Integer.parseInt(denominationAndAmount[1]);

                account.setMoney(account.getMoney() + (denomination * amount));
                moneyInATM = ATM.getATM().getMoneyInATM();

                int count = moneyInATM.get(denomination) + amount;

                moneyInATM.put(denomination, count);
                ATM.getATM().setMoneyInATM(moneyInATM);
                Terminal.printMsg("Вы успешно внесли " + (denomination * amount) + " РУБ");
                ATM.getATM().writeAccount(account);
            } catch (NumberFormatException e) {
                Terminal.printMsg("Введите корректные данные");
            }
            Terminal.printMsg("Для продолжения внесения наличных наберите 'Y'. Для завершения операции нажмите любую кнопку");
            yes = Terminal.readString();
        }

    }
}
