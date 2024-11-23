/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storages;

import core.models.Transaction;
import java.util.ArrayList;

/**
 *
 * @author isabe
 */
public class StorageTransactions {

    //singleton instance
    private static StorageTransactions instance;

    //atributos de storage t
    private ArrayList<Transaction> transactions;
    //lista de transactions para mostrar en el view, con diferentes atributos sin el tipo Accoint sino String

    public StorageTransactions() {
        this.transactions = new ArrayList<>();
    }

    public static StorageTransactions getInstance() {
        if (instance == null) {
            instance = new StorageTransactions();
        }
        return instance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    
    public boolean addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
       
        
        return true;
    }
    
}
