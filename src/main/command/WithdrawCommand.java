package main.command;

import main.ATM;
import main.exception.InterruptedOperationException;
import main.exception.NotEnoughMoneyExeption;
import main.model.Account;
import main.service.Terminal;

import java.util.*;

public class WithdrawCommand implements Command {
    private ATM atm = ATM.getATM();
    private Account account = ATM.getAccount();


    @Override
    public void execute() throws InterruptedOperationException {
        int sum;

        while (true) {
            Terminal.printMsg("Снятие средств");
            String str = Terminal.readString();
            try {
                sum = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                Terminal.printMsg("Введите корректные данные");
                continue;
            }
            if (sum <= 0) {
                Terminal.printMsg("Сумма для снятия не может быть меньше либо равна нулю");
                continue;
            }
            if (!(isAmountAvailable(sum))) {
                Terminal.printMsg("В банкомате недостаточно средств");
                continue;
            }
            try {
                withdrawAmount(sum);
            } catch (NotEnoughMoneyExeption e) {
                Terminal.printMsg("Не достаточно средств");
                continue;
            }

            Terminal.printMsg("Вы успешно сняли " + sum);
            break;
        }
    }

    private void withdrawAmount(int expectedAmount) throws NotEnoughMoneyExeption {
        int sum = expectedAmount;
        Map<Integer, Integer> moneyInATM = ATM.getATM().getMoneyInATM();
        List<Integer> list = new ArrayList<>();

        for (Map.Entry<Integer, Integer> pair : moneyInATM.entrySet()) {
            list.add(pair.getKey());
        }

        Collections.sort(list);
        Collections.reverse(list);

        TreeMap<Integer, Integer> result = new TreeMap<>();

        for (Integer i : list) {
            int money = i;
            int count = moneyInATM.get(money);
            while (true) {
                if (sum < money || count <= 0) {
                    moneyInATM.put(money, count);
                    break;
                }
                sum -= money;
                count--;

                if (result.containsKey(money)) {
                    result.put(money, result.get(money) + 1);
                } else {
                    result.put(money, 1);
                }
            }
        }
        if (sum > 0) {
                throw new NotEnoughMoneyExeption();
        } else {
            for (Map.Entry<Integer, Integer> pair : result.entrySet()) {
                Terminal.printMsg(pair.getKey() + " РУБ - " + pair.getValue() + " банкнот(ы)");
            }

            atm.setMoneyInATM(moneyInATM);
            account.setMoney(account.getMoney() - expectedAmount);
            atm.writeAccount(account);
            Terminal.printMsg("Успешная операция");
        }
    }

    private boolean isAmountAvailable(int sum) {
        return (sum < ATM.getATM().getTotalAmountInATM());
    }
}