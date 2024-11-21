/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import bank.Account;
import bank.User;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storages.StorageAccounts;
import core.models.storages.StorageUsers;

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
            if (firstname.equals("") && lastname.equals("")) {
                return new Response("Firstname and last name must be not empty", Status.BAD_REQUEST);
            }

//        quitar    if (lastname.equals("")) {
//                return new Response("Lastname must be not empty", Status.BAD_REQUEST);
//            }
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
            //id pasar de string a int 
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

            //llamar al modelo y recibir respuesta
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
