/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storages;

import core.models.Account;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author isabe
 */
public class StorageAccounts {

    private static StorageAccounts instance;
    private GenerateAccountId generateId;

    private ArrayList<Account> accounts;

    public StorageAccounts() {
        this.generateId = new GenerateAccountId ();
        this.accounts = new ArrayList<> ();
    }

    

    public static StorageAccounts getInstance() {
        if (instance == null) {
            instance = new StorageAccounts();
        }
        return instance;
    }

    public boolean addAccounts(Account account) {
        for (Account a : this.accounts) {
            if (a.getId().equals(account.getId())) {
                return false;
            }
        }
        this.accounts.add(account);
        return true;
    }

    public String generateAccountId() {
        return generateId.generateAccountId();
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    
    public boolean getAccounts(String id) {
        for (Account a : this.accounts) {
            if (a.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Account getAccount(String id) {
        for (Account a : this.accounts) {
             if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

}
