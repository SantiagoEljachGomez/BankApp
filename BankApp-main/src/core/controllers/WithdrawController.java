/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.models.Transaction;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Account;
import core.models.TransactionType;
import core.models.storages.StorageAccounts;
import core.models.storages.StorageTransactions;

/**
 *
 * @author isabe
 */
public class WithdrawController {

    public static Response withdrawTransaction(String sourceAccountId, String amount) {
        try {
            StorageAccounts storageAccounts = StorageAccounts.getInstance();

            if (sourceAccountId.equals("")) {
                return new Response("Source account should be filled", Status.BAD_REQUEST);
            }
            //validar que el account a sacar dinero si exista
            if (!storageAccounts.getAccounts(sourceAccountId)) {
                return new Response("Source account does not exist", Status.BAD_REQUEST);
            }

            double balance = storageAccounts.getAccount(sourceAccountId).getBalance();
            double amountDouble;
            try {
                amountDouble = Double.parseDouble(amount);
                if (amountDouble <= 0) {
                    return new Response("The amount to be deposited must be positive", Status.BAD_REQUEST);
                }
                if (amountDouble > balance) {
                    return new Response("The amount to be withdrawn must be available in the balance of the account ", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Amount must be numeric", Status.BAD_REQUEST);
            }
            StorageTransactions storageTransactions = StorageTransactions.getInstance();

            Account sourceAccount = storageAccounts.getAccount(sourceAccountId);
            Account destinationAccount = null;
            Transaction transaction = new Transaction(TransactionType.WITHDRAW, sourceAccount, destinationAccount, amountDouble);

            if (!storageTransactions.addTransactions(transaction)) {
                return new Response("Transaction not approved", Status.BAD_REQUEST);
            }

            sourceAccount.setBalance(balance - amountDouble);
            return new Response("Transaction approved", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
