/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

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

//getters y setters cumplen con SINGLE RESPONSABILITY DE SOLID
    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
