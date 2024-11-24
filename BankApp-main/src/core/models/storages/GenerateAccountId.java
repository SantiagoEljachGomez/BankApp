/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storages;

import java.util.Random;

/**
 *
 * @author JACH
 */
public class GenerateAccountId {

    public GenerateAccountId() {
    }
    
    
    
    public String generateAccountId() {
        Random random = new Random();

        int random1 = random.nextInt(1000);
        int random2 = random.nextInt(1000000);
        int random3 = random.nextInt(100);

        String accountId = String.format("%03d", random1) + "-" + String.format("%06d", random2) + "-" + String.format("%02d", random3);
        
        return accountId;
        
    }
    
    
    
}
