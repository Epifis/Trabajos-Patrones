/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Kotaro
 */
public class dueDate {
    private Date dateInicial;
    private Date dateFinal;
    private int diasParaPago;

    public dueDate(Date dateInicial, int diasParaPago) {
        this.dateInicial = dateInicial;;
        this.diasParaPago = diasParaPago;
    }

    public int getDiasParaPago() {
        return diasParaPago;
    }
    

    public Date getDateInicial() {
        return dateInicial;
    }

    public void setDateInicial(Date dateInicial) {
        this.dateInicial = dateInicial;
    }

    public Date getDateFinal() {
        return dateFinal;
    }

    public void setDateFinal(Date dateFinal) {
        this.dateFinal = dateFinal;
    }
    
    public Date addDate(Date dateInicial, int diasParaPago){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInicial);
        cal.add(Calendar.DATE, diasParaPago);
        dateFinal = cal.getTime();
        return dateFinal;
    }
}
