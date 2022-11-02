package main.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Account {
    private long cardNum;
    private int money = 0;

    @JsonCreator
    public Account(@JsonProperty("cardNum") long cardNum) {
        this.cardNum = cardNum;
    }

    public long getCardNum() {
        return cardNum;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
