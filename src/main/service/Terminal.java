package main.service;

import main.exception.InterruptedOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Terminal {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void printMsg(String msg) {
        System.out.println(msg);
    }

    public static String readString() throws InterruptedOperationException {
        String str = null;
        try {
            str = br.readLine();
            if (str.equalsIgnoreCase("EXIT")) {
                throw new InterruptedOperationException();
            }
        } catch (IOException e) {
        }
        return str;
    }

    public static void printExitMsg() {
        System.out.println("Выход");
    }

    public static String[] getBanknotes() throws InterruptedOperationException {
        String[] banknotes;
        System.out.println("Введите номинал и количество банкнот. Например 100 3 означает 300 РУБ\nКупюры, номиналом меньше 100 РУБ, не принимаются");

        while (true) {
            String str = readString();
            banknotes = str.split(" ");
            int denomination;
            int amount;
            try {
                denomination = Integer.parseInt(banknotes[0]);
                amount = Integer.parseInt(banknotes[1]);
            } catch (Exception e) {
                printMsg("Введите корректные данные");
                continue;
            }
            if (denomination <= 0 || amount <= 0 || banknotes.length > 2) {
                printMsg("Введите корректные данные");
                continue;
            }
            if (!(denomination == 100 || denomination == 200 || denomination == 500 || denomination == 1000 || denomination == 2000 || denomination == 5000)) {
                printMsg("Банкноты данного номинала не поддерживаются");
                continue;
            }
            break;
        }
        return banknotes;
    }

    public static Operation chooseOperation() throws InterruptedOperationException {
        while (true) {
            String str = readString();
            if (checkWithRegExp(str)) {
                return Operation.getAvailableOperation(Integer.parseInt(str));
            } else {
                printMsg("Данной операции нет в меню");
            }
        }
    }

    private static boolean checkWithRegExp(String str) {
        Pattern p = Pattern.compile("^[1-4]$");
        Matcher m = p.matcher(str);

        return m.matches();
    }
}
