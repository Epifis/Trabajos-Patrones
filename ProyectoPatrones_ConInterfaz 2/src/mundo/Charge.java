/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

/**
 *
 * @author j8318
 */
public class Charge {
    private String description;
    private double amount;
    
    public Charge(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }
    
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String ImprimirCargos(){ 
        System.out.println("Descripcion: " + description);
        System.out.println("Monto: " + amount);
    return "Descripcion: " + description+"Monto: " + amount;
    }
    @Override
    public String toString() {
        return "Cargos con descripción " + description +" y de monto: " + amount;
    }

}
