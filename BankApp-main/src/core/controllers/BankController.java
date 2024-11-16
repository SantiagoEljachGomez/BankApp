/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import bank.User;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storages.StorageUsers;

/**
 *
 * @author isabe
 */
public class BankController {

    public static Response register(String idUser, String firstname, String lastname, String ageUser) {

        try { //capturar errores externos con try general
            int id, age;

            try { //try de capturar errores en el ingreso de los numeros como id y age
                id = Integer.parseInt(idUser);
                if (id < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
                if (id > 999999999) { //id no puede ser mayor a 9 digitos
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
                age = Integer.parseInt(ageUser);
                if (age < 18) {
                    return new Response("Age must be over 18 years old", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Age must be numeric", Status.BAD_REQUEST);
            }

            //validar que los id no se repitan, buscar en el storage de users para validar que sea unico
            StorageUsers storageusers = StorageUsers.getInstance();
            if (!storageusers.addUsers(new User(id, firstname, lastname, age))) {
                return new Response("A user with this id already exists", Status.BAD_REQUEST);
            }
            return new Response("User registered successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        } 

    }
}
