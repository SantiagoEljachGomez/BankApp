/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import bank.Account;
import bank.Transaction;
import bank.TransactionType;
import bank.User;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storages.StorageAccounts;
import core.models.storages.StorageTransactions;
import core.models.storages.StorageUsers;
import java.util.ArrayList;

/**
 *
 * @author isabe
 */
public class BankController {

    public static Response register(String idUser, String firstname, String lastname, String ageUser) {

        try { //capturar errores externos con try general
            if (idUser.equals("")) {
                return new Response("ID must be not empty", Status.BAD_REQUEST);
            }
            if (ageUser.equals("")) {
                return new Response("Age must be not empty", Status.BAD_REQUEST);
            }
            int idInt, ageInt;

            try { //try de capturar errores en el ingreso de los numeros como id y age
                idInt = Integer.parseInt(idUser);
                if (idInt < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
                if (idInt > 999999999) { //id no puede ser mayor a 9 digitos
                    return new Response("Id must have maximum 9 digits", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException ex) {
                return new Response("Entrance must be numeric", Status.BAD_REQUEST);
            }
            //validar que el nombre y apellido no sean vacios
            if (firstname.equals("")) {
                return new Response("Firstname must be not empty", Status.BAD_REQUEST);
            }

            if (lastname.equals("")) {
                return new Response("Lastname must be not empty", Status.BAD_REQUEST);
            }
            try {
                ageInt = Integer.parseInt(ageUser);
                if (ageInt < 18) {
                    return new Response("Age must be over 18 years old", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Age must be numeric", Status.BAD_REQUEST);
            }

            //validar que los id no se repitan, buscar en el storage de users para validar que sea unico
            StorageUsers storageusers = StorageUsers.getInstance();
            if (!storageusers.addUsers(new User(idInt, firstname, lastname, ageInt))) {
                return new Response("A user with this id already exists", Status.BAD_REQUEST);
            }
            return new Response("User registered successfully", Status.CREATED);
        } catch (Exception ex) {

            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

    public static Response create(String userId, String initialBalance) {

        try {
            int userIdInt;
            double initialBalanceDouble;

            if (userId.equals("")) {
                return new Response("User ID must be not empty", Status.BAD_REQUEST);
            }
            if (initialBalance.equals("")) {
                return new Response("Initial Balance must not be empty", Status.BAD_REQUEST);
            }

            StorageUsers storageUsers = StorageUsers.getInstance();
            try {
                userIdInt = Integer.parseInt(userId);
                //validacion de que el ususrio que quiera crear cuenta exita y este registrado
                if (!storageUsers.getUsers(userIdInt)) {
                    return new Response("The user is not registered", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("User ID must be numeric", Status.BAD_REQUEST);
            }

            try {
                initialBalanceDouble = Double.parseDouble(initialBalance);
                //validacion que el saldo inicial no sea negativo
                if (initialBalanceDouble < 0) {
                    return new Response("Initial Balance cannot be negative", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Initial balance must be numeric", Status.BAD_REQUEST);
            }

            StorageAccounts storageAccounts = StorageAccounts.getInstance();
            User user = storageUsers.getUser(userIdInt);
            if (!storageAccounts.addAccounts(new Account(storageAccounts.generateAccountId(), user, initialBalanceDouble))) {
                return new Response("Error, Account not successfully created", Status.BAD_REQUEST);
                //hubo error al crear la cuenta debido a que el id generado coincide con otro existente
            }
            return new Response("Account created successfully", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

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

    public static Response refreshUsers() {
        try {
            StorageUsers storageUsers = StorageUsers.getInstance();
            ArrayList<User> usersOfStorage = storageUsers.getUsers();

            return new Response(" ", Status.CREATED, usersOfStorage);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response refreshAccounts() {
        try {

            StorageAccounts storageAccounts = StorageAccounts.getInstance();
            ArrayList<Account> accountsOfStorage = storageAccounts.getAccounts();

            return new Response(" ", Status.CREATED, accountsOfStorage);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response refreshTransactions() {
        try {

            StorageTransactions storageTransactions = StorageTransactions.getInstance();
            ArrayList<Transaction> transactionsOfStorage = storageTransactions.getTransactions();

            return new Response(" ", Status.CREATED, transactionsOfStorage);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
