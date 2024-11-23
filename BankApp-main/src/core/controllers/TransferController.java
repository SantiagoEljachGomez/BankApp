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
public class TransferController {

    public static Response transferTransaction(String sourceAccountId, String destinationAccountId, String amount) {
        try {
            if (sourceAccountId.equals("")) {
                return new Response("Source account should be filled", Status.BAD_REQUEST);
            }
            if (destinationAccountId.equals("")) {
                return new Response("Destination account should be filled", Status.BAD_REQUEST);
            }
            if (amount.equals("")) {
                return new Response("Amount should be filled", Status.BAD_REQUEST);
            }

            StorageAccounts storageAccounts = StorageAccounts.getInstance();
            StorageTransactions storageTransactions = StorageTransactions.getInstance();

//source account validations
            if (!storageAccounts.getAccounts(sourceAccountId)) {
                return new Response("Source account does not exist", Status.BAD_REQUEST);
            }

            double sourcebalance = storageAccounts.getAccount(sourceAccountId).getBalance();
            double amountDouble;
            try {
                amountDouble = Double.parseDouble(amount);
                if (amountDouble <= 0) {
                    return new Response("The amount to be deposited must be positive", Status.BAD_REQUEST);
                }
                if (amountDouble > sourcebalance) {
                    return new Response("The amount to be transfered must be available in the balance of the account ", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Amount must be numeric", Status.BAD_REQUEST);
            }

            //destination account validations
            if (!storageAccounts.getAccounts(destinationAccountId)) {
                return new Response("Destination account does not exist", Status.BAD_REQUEST);
            }

            Account sourceAccount = storageAccounts.getAccount(sourceAccountId);

            Account destinationAccount = storageAccounts.getAccount(destinationAccountId);
            double destinationbalance = storageAccounts.getAccount(destinationAccountId).getBalance();

            Transaction transaction = new Transaction(TransactionType.TRANSFER, sourceAccount, destinationAccount, amountDouble);
            if (!storageTransactions.addTransactions(transaction)) {
                return new Response("Transaction not approved", Status.BAD_REQUEST);
            }
            destinationAccount.setBalance(destinationbalance + amountDouble);
            sourceAccount.setBalance(sourcebalance - amountDouble);

            return new Response("Transaction approved", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
