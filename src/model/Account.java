package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Account {
    private Long number;
    private Integer password; // 4 digits
    private Double balance;
    private Boolean isActive;
    private String creationDate;

    public Account(Long number, Integer password) {
        this.number = number;
        this.password = password;
        this.balance = 100_000.0;
        this.isActive = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        creationDate = format.format(new Date());
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public void addBalance(Double balanceToAdd){
        this.balance += balanceToAdd;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
