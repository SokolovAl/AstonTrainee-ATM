package main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import main.command.CommandExecutor;
import main.exception.InterruptedOperationException;
import main.model.Account;
import main.service.Operation;
import main.service.Terminal;

import java.io.*;
import java.util.*;

public class ATM {
    private static ATM atm;
    private static Account account = null;
    private final Map<Integer, Integer> moneyInATM = new HashMap<>();
    private File accounts = new File("./src/resources/accounts");
    private File denominationAndAmount = new File("./src/resources/denominationAndAmount");

    public static void main(String[] args) {

        try {
            CommandExecutor.execute(Operation.LOGIN);
            Operation operation;
            do {
                Terminal.printMsg("Выберите операцию или наберите 'EXIT' для завершения работы");
                Terminal.printMsg("Информация по счету - 1");
                Terminal.printMsg("Внесение наличных - 2");
                Terminal.printMsg("Снятие наличных - 3");
                Terminal.printMsg("Завешение работы - 4");

                operation = Terminal.chooseOperation();

                CommandExecutor.execute(operation);
            }
            while (operation != Operation.EXIT);
        } catch (InterruptedOperationException e) {
            try {
                CommandExecutor.execute(Operation.EXIT);
            } catch (InterruptedOperationException ioe) {
            }
            Terminal.printMsg("Завершение работы");
        }
    }

    public static ATM getATM() {
        if (atm == null) {
            atm = new ATM();
        }
        return atm;
    }

    public Map<Integer, Integer> getMoneyInATM() {
        try {
            Properties p = new Properties();
            p.load(new FileReader(denominationAndAmount));

            for (String s : p.stringPropertyNames()) {
                moneyInATM.put(Integer.parseInt(s), Integer.parseInt(p.getProperty(s)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moneyInATM;
    }

    public void setMoneyInATM(Map<Integer, Integer> map) {
        try {
            Properties p = new Properties();

            for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
                p.setProperty(pair.getKey().toString(), pair.getValue().toString());
            }
            p.store(new FileWriter(denominationAndAmount), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotalAmountInATM() {
        int result = 0;
        for (Map.Entry<Integer, Integer> pair : getMoneyInATM().entrySet()) {
            result = result + (pair.getKey() * pair.getValue());
        }
        return result;
    }

    public List<Account> loadAccounts() {
        try {
            FileReader fr = new FileReader(accounts);
            ObjectMapper om = new ObjectMapper();
            List<Account> accounts = om.readValue(fr, new TypeReference<List<Account>>() {
            });
            return accounts;
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeListAccounts(List<Account> list) {
        try {
            FileWriter fw = new FileWriter(accounts);
            ObjectMapper om = new ObjectMapper();
            om.writeValue(fw, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeAccount(Account account) {
        List<Account> list = loadAccounts();
        for (Account a : list) {
            if (a.getCardNum() == account.getCardNum()) {
                a.setMoney(account.getMoney());
            }
        }
        writeListAccounts(list);
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        ATM.account = account;
    }
}
