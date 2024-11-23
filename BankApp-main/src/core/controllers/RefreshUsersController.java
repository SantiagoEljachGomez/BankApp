/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;


import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.User;
import core.models.storages.StorageUsers;
import java.util.ArrayList;

/**
 *
 * @author isabe
 */
public class RefreshUsersController {

    public static Response refreshUsers() {
        try {
            StorageUsers storageUsers = StorageUsers.getInstance();
            ArrayList<User> usersOfStorage = storageUsers.getUsers();

            return new Response(" ", Status.CREATED, usersOfStorage);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
