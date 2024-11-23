/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;


import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Account;
import core.models.storages.StorageAccounts;
import java.util.ArrayList;

/**
 *
 * @author isabe
 */
public class RefreshAccountsController {

    public static Response refreshAccounts() {
        try {

            StorageAccounts storageAccounts = StorageAccounts.getInstance();
            ArrayList<Account> accountsOfStorage = storageAccounts.getAccounts();

            return new Response(" ", Status.CREATED, accountsOfStorage);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
