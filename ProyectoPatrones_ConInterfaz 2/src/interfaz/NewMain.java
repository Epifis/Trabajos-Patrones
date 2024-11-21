package interfaz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mundo.BillingSystem;
import mundo.Charge;
import mundo.ClassSession;
import mundo.ClassSessionProxy;
import mundo.Invoice;
import mundo.NotificationDecorator;
import mundo.PaymentNotificationSystem;
import mundo.Student;
import mundo.Teacher;

/**
 *
 * @author j8318
 */
public class NewMain {
    
    private static Scanner scanner = new Scanner(System.in);
    private static BillingSystem billingSystem = BillingSystem.getInstance();
    private static PaymentNotificationSystem notificationSystem = new PaymentNotificationSystem();
    private static List<Student> students = new ArrayList<>();
    private static List<Teacher> teachers = new ArrayList<>();
    private static List<ClassSession> activeSessions = new ArrayList<>();
    private static Invoice invoice;
    private static List<Charge> charges ;
    private static List<Invoice> invoicesList = new ArrayList<>();
    public static void main(String[] args) {
        
        while (true) {
            showMainMenu();
            int option = getValidOption(1, 5);
            
            switch (option) {
                case 1:
                    billingMenu();
                    break;
                case 2:
                    notificationMenu();
                    break;
                case 3:
                    classManagementMenu();
                    break;
                case 4:
                    managementMenu();
                    break;
                case 5:
                    System.out.println("¡Gracias por usar el sistema!");
                    return;
            }
        }
    }

    public static void showMainMenu() {
        System.out.println("\n=== SISTEMA UNIVERSIDAD PILOTO ===");
        System.out.println("1. Sistema de Facturación");
        System.out.println("2. Sistema de Notificaciones");
        System.out.println("3. Gestión de Clases");
        System.out.println("4. Gestión de Usuarios");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void billingMenu() {
        while (true) {
            System.out.println("\n=== SISTEMA DE FACTURACIÓN ===");
            System.out.println("1. Generar Nueva Factura");
            System.out.println("2. Ver Facturas Generadas");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int option = getValidOption(1, 3);
            switch (option) {
                case 1:
                    generateNewInvoice();
                    break;
                case 2:
                    viewInvoices();
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void notificationMenu() {
        while (true) {
            System.out.println("\n=== SISTEMA DE NOTIFICACIONES ===");
            System.out.println("1. Enviar Recordatorio de Pago");
            System.out.println("2. Registrar Estudiante para Notificaciones");
            System.out.println("3. Eliminar Estudiante de Notificaciones");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int option = getValidOption(1, 4);
            switch (option) {
                case 1:
                    sendPaymentReminder();
                    break;
                case 2:
                    registerStudentForNotifications();
                    break;
                case 3:
                    removeStudentFromNotifications();
                    break;
                case 4:
                    return;
            }
        }
    }

    private static void classManagementMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE CLASES ===");
            System.out.println("1. Programar Nueva Clase");
            System.out.println("2. Cancelar Clase");
            System.out.println("3. Ver Clases Activas");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int option = getValidOption(1, 4);
            switch (option) {
                case 1:
                    scheduleNewClass();
                    break;
                case 2:
                    cancelClass();
                    break;
                case 3:
                    viewActiveSessions();
                    break;
                case 4:
                    return;
            }
        }
    }

    private static void managementMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE USUARIOS ===");
            System.out.println("1. Registrar Nuevo Estudiante");
            System.out.println("2. Registrar Nuevo Profesor");
            System.out.println("3. Ver Estudiantes");
            System.out.println("4. Ver Profesores");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int option = getValidOption(1, 5);
            switch (option) {
                case 1:
                    registerNewStudent();
                    break;
                case 2:
                    registerNewTeacher();
                    break;
                case 3:
                    viewStudents();
                    break;
                case 4:
                    viewTeachers();
                    break;
                case 5:
                    return;
            }
        }
    }

    // Métodos de implementación

    private static void generateNewInvoice() {
        if (students.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.println("\nSeleccione un estudiante:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }
        int studentIndex = getValidOption(1, students.size()) - 1;
        Student student = students.get(studentIndex);

        charges = new ArrayList<>();
        boolean addingCharges = true;
        while (addingCharges) {
            System.out.print("Descripción del cargo: ");
            String description = scanner.nextLine();
            System.out.print("Monto del cargo: ");
            double amount = Double.parseDouble(scanner.nextLine());
            charges.add(new Charge(description, amount));
            
            System.out.print("¿Desea agregar otro cargo? (S/N): ");
            addingCharges = scanner.nextLine().toUpperCase().equals("S");
        }

        invoice = new Invoice(student, charges);
        invoicesList.add(invoice);
        invoice.generateDocument();
    }

    private static void viewInvoices() {
         if (invoicesList.isEmpty()) {
            System.out.println("No hay facturas generadas.");
            return;
        }

        System.out.println("\nFacturas generadas:");
        for (int i = 0; i < invoicesList.size(); i++) {
            System.out.println("\nFactura #" + (i + 1));
            invoicesList.get(i).generateDocument();
        }
    }

    private static void sendPaymentReminder() {
        System.out.print("Ingrese los días hasta la fecha límite: ");
        int days = Integer.parseInt(scanner.nextLine());
        notificationSystem.notifyPaymentDue(LocalDate.now().plusDays(days));
    }

    private static void registerStudentForNotifications() {
        if (students.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.println("\nSeleccione un estudiante para registrar en notificaciones:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }
        int studentIndex = getValidOption(1, students.size()) - 1;
        Student student = students.get(studentIndex);
        notificationSystem.attach(student);
        System.out.println("Estudiante registrado para notificaciones exitosamente.");
    }

    private static void removeStudentFromNotifications() {
        if (students.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.println("\nSeleccione un estudiante para eliminar de notificaciones:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }
        int studentIndex = getValidOption(1, students.size()) - 1;
        Student student = students.get(studentIndex);
        notificationSystem.detach(student);
        System.out.println("Estudiante eliminado de notificaciones exitosamente.");
    }

    private static void scheduleNewClass() {
        if (teachers.isEmpty()) {
            System.out.println("No hay profesores registrados.");
            return;
        }

        System.out.println("\nSeleccione un profesor:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i).getName());
        }
        int teacherIndex = getValidOption(1, teachers.size()) - 1;
        Teacher teacher = teachers.get(teacherIndex);

        ClassSession session = new NotificationDecorator(
            new ClassSessionProxy(teacher),
            new ArrayList<>(students)
        );
        session.scheduleClass();
        activeSessions.add(session);
    }

    private static void cancelClass() {
        if (activeSessions.isEmpty()) {
            System.out.println("No hay clases activas para cancelar.");
            return;
        }

        System.out.println("\nSeleccione una clase para cancelar:");
        for (int i = 0; i < activeSessions.size(); i++) {
            System.out.println((i + 1) + ". Clase " + (i + 1));
        }
        int sessionIndex = getValidOption(1, activeSessions.size()) - 1;
        ClassSession session = activeSessions.get(sessionIndex);
        session.cancelClass();
        activeSessions.remove(sessionIndex);
    }

    private static void viewActiveSessions() {
        if (activeSessions.isEmpty()) {
            System.out.println("No hay clases activas.");
            return;
        }

        System.out.println("\nClases Activas:");
        for (int i = 0; i < activeSessions.size(); i++) {
            System.out.println((i + 1) + ". Clase " + (i + 1));
        }
    }

    private static void registerNewStudent() {
        System.out.print("Nombre del estudiante: ");
        String name = scanner.nextLine();
        System.out.print("Email del estudiante: ");
        String email = scanner.nextLine();
        
        Student student = new Student(name, email);
        students.add(student);
        System.out.println("Estudiante registrado exitosamente.");
    }

    private static void registerNewTeacher() {
        System.out.print("Nombre del profesor: ");
        String name = scanner.nextLine();
        System.out.print("¿Está autorizado? (S/N): ");
        boolean authorized = scanner.nextLine().toUpperCase().equals("S");
        
        Teacher teacher = new Teacher(name, authorized);
        teachers.add(teacher);
        System.out.println("Profesor registrado exitosamente.");
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.println("\nEstudiantes Registrados:");
        for (Student student : students) {
            System.out.println("- " + student.getName());
        }
    }

    private static void viewTeachers() {
        if (teachers.isEmpty()) {
            System.out.println("No hay profesores registrados.");
            return;
        }

        System.out.println("\nProfesores Registrados:");
        for (Teacher teacher : teachers) {
            System.out.println("- " + teacher.getName());
        }
    }

    private static void initializeDemoData() {
        // Agregar algunos datos de ejemplo
        Student student1 = new Student("Juan Pablo", "juan@email.com");
        Student student2 = new Student("Alexandra", "alexandra@email.com");
        students.add(student1);
        students.add(student2);
        
        Teacher teacher1 = new Teacher("Prof. García", true);
        Teacher teacher2 = new Teacher("Prof. López", true);
        teachers.add(teacher1);
        teachers.add(teacher2);
        
        // Registrar estudiantes para notificaciones
        notificationSystem.attach(student1);
        notificationSystem.attach(student2);
    }

    static int getValidOption(int min, int max) {
        while (true) {
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option >= min && option <= max) {
                    return option;
                }
                System.out.print("Opción inválida. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un número válido: ");
            }
        }
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        NewMain.scanner = scanner;
    }

    public static BillingSystem getBillingSystem() {
        return billingSystem;
    }

    public static void setBillingSystem(BillingSystem billingSystem) {
        NewMain.billingSystem = billingSystem;
    }

    public static PaymentNotificationSystem getNotificationSystem() {
        return notificationSystem;
    }

    public static void setNotificationSystem(PaymentNotificationSystem notificationSystem) {
        NewMain.notificationSystem = notificationSystem;
    }

    public static List<Student> getStudents() {
        return students;
    }

    public static void setStudents(List<Student> students) {
        NewMain.students = students;
    }

    public static List<Teacher> getTeachers() {
        return teachers;
    }

    public static void setTeachers(List<Teacher> teachers) {
        NewMain.teachers = teachers;
    }

    public static List<ClassSession> getActiveSessions() {
        return activeSessions;
    }

    public static void setActiveSessions(List<ClassSession> activeSessions) {
        NewMain.activeSessions = activeSessions;
    }

    public static Invoice getInvoice() {
        return invoice;
    }

    public static void setInvoice(Invoice invoice) {
        NewMain.invoice = invoice;
    }

    public static List<Charge> getCharges() {
        return charges;
    }

    public static void setCharges(List<Charge> charges) {
        NewMain.charges = charges;
    }

    public static List<Invoice> getInvoicesList() {
        return invoicesList;
    }

    public static void setInvoicesList(List<Invoice> invoicesList) {
        NewMain.invoicesList = invoicesList;
    }
    
}
