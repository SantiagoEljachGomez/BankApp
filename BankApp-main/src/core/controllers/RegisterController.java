/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.User;
import core.models.storages.StorageUsers;

/**
 *
 * @author isabe
 */
public class RegisterController {

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
}
