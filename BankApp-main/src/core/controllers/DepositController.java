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
public class DepositController {

    public static Response depositTransaction(String destinationAccountId, String amount) {   //validar o no el source acc en depost
        try {
            StorageAccounts storageAccounts = StorageAccounts.getInstance();

            if (destinationAccountId.equals("")) {
                return new Response("Destination account should be filled", Status.BAD_REQUEST);
            }
            //validar que el account a despositar dinero si exista
            if (!storageAccounts.getAccounts(destinationAccountId)) {
                return new Response("Destination account does not exist", Status.BAD_REQUEST);
            }
            double amountDouble, balance;
            try {
                amountDouble = Double.parseDouble(amount);

                if (amountDouble <= 0) {
                    return new Response("The amount to be deposited must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Amount must be numeric", Status.BAD_REQUEST);
            }
            StorageTransactions storageTransactions = StorageTransactions.getInstance();

            Account sourceAccount = null;
            Account destinationAccount = storageAccounts.getAccount(destinationAccountId);

            Transaction transaction = new Transaction(TransactionType.DEPOSIT, sourceAccount, destinationAccount, amountDouble);
            if (!storageTransactions.addTransactions(transaction)) {
                return new Response("Transaction not approved", Status.BAD_REQUEST);
            }

            balance = destinationAccount.getBalance();
            destinationAccount.setBalance(balance + amountDouble);

            return new Response("Transaction approved", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
