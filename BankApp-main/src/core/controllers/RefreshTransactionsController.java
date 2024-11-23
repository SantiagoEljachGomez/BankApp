/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.models.Transaction;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storages.StorageTransactions;
import java.util.ArrayList;

/**
 *
 * @author isabe
 */
public class RefreshTransactionsController {

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
