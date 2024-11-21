/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

/**
 *
 * @author j8318
 */

public class BillingSystem {
    private static BillingSystem instance;
    
    private BillingSystem() {}
    
    public static BillingSystem getInstance() {
        if (instance == null) {
            instance = new BillingSystem();
        }
        return instance;
    }
}
