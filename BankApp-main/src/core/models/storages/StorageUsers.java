/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storages;

import bank.User;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author isabe
 */
public class StorageUsers {

    //Instancia Singleton
    private static StorageUsers instance;

    private ArrayList<User> users;

    public StorageUsers() {
        this.users = users;
    }

    public static StorageUsers getInstance() {
        if (instance == null) {
            instance = new StorageUsers();
        }
        return instance;
    }

    public boolean addUsers(User user) {
        for (User u : this.users) {
            if (u.getId() == user.getId()) {
                return false;
            }
        }
        this.users.add(user);
        return true;
    }

    public ArrayList<User> getUsers() { //usar despues para mostrar en el view de ListUsers
        return users;
    }

}
