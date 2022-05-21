package com.gh90.example.dblock.account;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;

    public boolean canPay(int price){
        return this.balance >= price;
    }

    public void minusBalance(int price){
        this.balance = this.balance - price;
    }

    public void plusBalance(int price){
        this.balance = this.balance + price;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
