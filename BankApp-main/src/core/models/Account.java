/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank;

import java.util.Random;

/**
 *
 * @author edangulo
 */
public class Account {

    private String id;
    private User owner;
    private double balance;

    public Account(String id, User owner) {
        this.id = id;
        this.owner = owner;
        this.balance = 0;

        this.owner.addAccount(this);
    }

    public Account(String id, User owner, double balance) {
        this.id = id;
        this.owner = owner; // crear clase owner, para SOLID
        this.balance = balance;

        this.owner.addAccount(this);
    }

   

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > this.balance) {
            return false;// no hay la cantidad disponible en el balance
        }
        this.balance -= amount;//hay disponibilidad, por eso se resta al balance
        return true;
    }

}
