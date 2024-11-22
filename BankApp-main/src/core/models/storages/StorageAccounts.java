/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storages;

import bank.Account;
import bank.User;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author isabe
 */
public class StorageAccounts {

    private static StorageAccounts instance;

    private ArrayList<Account> accounts;

    public StorageAccounts() {
        this.accounts = accounts;
        
    }

    public static StorageAccounts getInstance() {
        if (instance == null) {
            instance = new StorageAccounts();
        }
        return instance;
    }

    public boolean addAccounts(Account account) {
        for (Account a : this.accounts) {
            if (a.getId() == account.getId()) {
                return false;
            }
        }
        this.accounts.add(account);
        return true;
    }

    public String generateAccountId() {
        Random random = new Random();

        int random1 = random.nextInt(1000);
        int random2 = random.nextInt(1000000);
        int random3 = random.nextInt(100);

        String accountId = String.format("%03d", random1) + "-" + String.format("%06d", random2) + "-" + String.format("%02d", random3);
        return accountId;
    }

    public boolean getAccounts(String id) {
        for (Account a : this.accounts) {
            if (a.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Account getAccount(String id) {
        for (Account a : this.accounts) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

}
