/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Kotaro
 */
public class CancelClassNotificationSystem {
    
    private List<PaymentObserver> observers;
    private List<String> notifications = new ArrayList<>(); // Lista para almacenar notificaciones

    public CancelClassNotificationSystem(){
        this.observers = new ArrayList<>();
    }

    public void attach(PaymentObserver observer) {
        observers.add(observer);
    }

    public void detach(PaymentObserver observer) {
        observers.remove(observer);
    }

    public void notifyCancelClass(String clase) {
        String message = "Se cancela la clase: " + clase ;
        for (PaymentObserver observer : observers) {
            observer.update(message);
        }
    }


    // Devuelve la lista de todas las notificaciones
    public List<String> getAllNotifications() {
        return notifications;
    }

    // Elimina una notificación por su índice
    public void removeNotification(int index) {
        if (index >= 0 && index < notifications.size()) {
            notifications.remove(index);
        }
    }

}
