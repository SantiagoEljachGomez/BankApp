/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;


import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Account;
import core.models.User;
import core.models.storages.StorageAccounts;
import core.models.storages.StorageUsers;

/**
 *
 * @author isabe
 */
public class CreateController {

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
}
