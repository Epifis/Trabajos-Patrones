package mundo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sistema de notificaciones de pagos de matrícula y fechas de corte académico.
 */
public class PaymentNotificationSystem {

    private List<PaymentObserver> observers;
    private List<String> notifications = new ArrayList<>(); // Lista para almacenar notificaciones

    public PaymentNotificationSystem() {
        this.observers = new ArrayList<>();
    }

    public void attach(PaymentObserver observer) {
        observers.add(observer);
    }

    public void detach(PaymentObserver observer) {
        observers.remove(observer);
    }

    public void notifyPaymentDue(Date dueDate) {
        String message = "Pago pendiente para la fecha: " + dueDate;
        for (PaymentObserver observer : observers) {
            observer.update(message);
        }
    }

    public void notifyPaymentDue(Date dueDate, Student student) {
        String notification = "Estudiante " + student.getName() + " ( con Fecha de pago: " + dueDate + ")";
        notifications.add(notification); // Guarda la notificación
        for (PaymentObserver observer : observers) {
            observer.update(notification);
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

