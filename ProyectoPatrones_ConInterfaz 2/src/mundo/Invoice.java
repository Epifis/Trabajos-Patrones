/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.Date;
import java.util.List;

public class Invoice {
    private Student student;
    private List<Charge> charges;
    private Date dateInicial;
    private int diasParaPago;
    private Date dateFinal;
    private dueDate date;

    public Invoice(Student student, List<Charge> charges, Date dateInicial, int diasParaPago) {
        this.student = student;
        this.charges = charges;
        this.dateInicial = dateInicial;
        this.diasParaPago = diasParaPago;
        date = new dueDate(dateInicial, diasParaPago);
        this.dateFinal = date.addDate(dateInicial, diasParaPago);
    }

    public void generateDocument() {
        // Aquí puedes generar un documento de la factura en formato String o cualquier otro formato
        StringBuilder document = new StringBuilder();
        document.append("Generando el documento PDF...\n");
        document.append("Estudiante: ").append(student.getName()).append("\n");
        document.append("Cargos del estudiante:\n");
        
        // Mostrar todos los cargos
        for (Charge charge : charges) {
            
            document.append(charge.toString()).append("\n");
            document.append("Día de generación Factura: ").append(dateInicial.toString());
            document.append("Día máximo para pago: ").append(dateFinal.toString());
        }

        document.append("Total calculado: ").append(calculateTotal()).append("\n");
        
        // Puedes devolver el documento como String o lo que consideres necesario
        System.out.println(document.toString());  // Para ver el documento en consola
    }

    public double calculateTotal() {
        double total = 0;
        for (Charge charge : charges) {
            total += charge.getAmount();  
        }
        return total;
    }
     public Student getStudent() {
        return student;
    }

    public Date getDateInicial() {
        return dateInicial;
    }

    public int getDiasParaPago() {
        return diasParaPago;
    }

    public Date getDateFinal() {
        return dateFinal;
    }
     
}
