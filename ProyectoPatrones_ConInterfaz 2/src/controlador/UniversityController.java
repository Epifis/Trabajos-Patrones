package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import mundo.BillingSystem;
import mundo.CancelClassNotificationSystem;
import mundo.Charge;
import mundo.ClassSession;
import mundo.ClassSessionProxy;
import mundo.Invoice;
import mundo.NotificationDecorator;
import mundo.PaymentNotificationSystem;
import mundo.RealClassSession;
import mundo.Student;
import mundo.Teacher;

/**
 *
 * @author j8318
 */
public class UniversityController {

    private BillingSystem billingSystem;
    private static List<Student> students;
    private static List<Teacher> teachers;
    private static List<Invoice> invoices;
    private static List<ClassSession> activeSessions;
    private static PaymentNotificationSystem notificationSystem;
    private static CancelClassNotificationSystem notificationSystemClasses;
    private static Date dateInicial;
    private static Calendar cal = Calendar.getInstance();
    

    public UniversityController() {
        this.billingSystem = BillingSystem.getInstance();
        this.notificationSystem = new PaymentNotificationSystem();
        this.notificationSystemClasses = new CancelClassNotificationSystem();
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.activeSessions = new ArrayList<>();
        this.invoices = new ArrayList<>();
        this.dateInicial = cal.getTime();
        initializeDemoData();
    }

    public void generateInvoice(Student student, List<Charge> charges, Date dateInicial, int diasParaPago) {
        Invoice invoice = new Invoice(student, charges, dateInicial, diasParaPago);
        invoices.add(invoice);
        invoice.generateDocument();
    }

    public Calendar getCal() {
        return cal;
    }

    public List<Invoice> getInvoicesList() {

        return invoices;
    }

    public void scheduleClass(Teacher teacher) {
        
        ClassSession session = new NotificationDecorator(
                new ClassSessionProxy(teacher),
                new ArrayList<>(students)
        );
        session.scheduleClass();
        
        activeSessions.add(session);
    }

    private static void initializeDemoData() {
        // Agregar algunos datos de ejemplo
        Student student1 = new Student("Juan Pablo", "juan@email.com");
        Student student2 = new Student("Alexandra", "alexandra@email.com");
        students.add(student1);
        students.add(student2);

        Teacher teacher1 = new Teacher("Prof. García", "01");
        Teacher teacher2 = new Teacher("Prof. López", "02");
        teachers.add(teacher1);
        teachers.add(teacher2);
        teacher1.addAuthorizedProfessor(teacher1.getId());
        teacher2.addAuthorizedProfessor(teacher2.getId());

        // Registrar estudiantes para notificaciones
        notificationSystem.attach(student1);
        notificationSystem.attach(student2);
    }

    public Date getDateInicial() {
        return dateInicial;
    }
    

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }


    public List<String> getStudentNames() {
        List<String> names = new ArrayList<>();
        for (Student student : students) {
            names.add(student.getName());
        }
        return names;
    }

    public List<String> getTeacherNames() {
        List<String> names = new ArrayList<>();
        for (Teacher teacher : teachers) {
            names.add(teacher.getName());
        }
        return names;
    }

    public List<String> getNotificaciones() {
        return notificationSystem.getAllNotifications(); // Devuelve las notificaciones almacenadas
    }

    public List<String> listarClases() {
        List<String> clases = new ArrayList<>();
        for (ClassSession session : activeSessions) {
            clases.add(session.getClassName()); // Asegúrate de que la clase tenga un método getClassName
        }
        return clases;
    }

    public void eliminarClase(String className) {
        notificationSystemClasses.notifyCancelClass(className);
        activeSessions.removeIf(session -> session.getClassName().equals(className));
        
        
    }

    public void actualizarUsuario(int index, String nuevoNombre, String nuevoEmail) {
        if (index >= 0 && index < students.size()) {
            Student student = students.get(index);
            student.setName(nuevoNombre);
            student.setEmail(nuevoEmail);
        }
    }

    public List<Object> getUsuarios() {
        List<Object> usuarios = new ArrayList<>();
        usuarios.addAll(students);
        usuarios.addAll(teachers);
        return usuarios;
    }

    public List<ClassSession> getClases() {
        return activeSessions;
    }

    public void crearClase(String nombreClase, Teacher teacher) {
        RealClassSession session = new RealClassSession(nombreClase, teacher);
        session.setClassName(nombreClase);
        session.setTeacher(teacher);
        activeSessions.add(session);
    }

    public void crearClase(String nombreClase) {
        Teacher defaultTeacher = teachers.get(0); // Predeterminado
        crearClase(nombreClase, defaultTeacher);
    }
public ClassSession getClassByName(String name) {
        return activeSessions.stream()
                .filter(teacher -> teacher.getTeacher().getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public Teacher getTeacherByName(String name) {
        return teachers.stream()
                .filter(teacher -> teacher.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public Student getStudentByName(String name) {
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Teacher getTeacherById(String id) {
        return teachers.stream()
                .filter(teacher -> teacher.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void crearEstudiante(String name, String email) {
        Student usuario = new Student(name, email);
        students.add(usuario);
    }

    public void crearProfesor(String name, String idautorizacion) {
        Teacher usuario = new Teacher(name, idautorizacion);
        teachers.add(usuario);
        usuario.addAuthorizedProfessor(idautorizacion);
    }

    public void eliminarUsuario(String name) {
        students.removeIf(usuario -> usuario.getName().equals(name));
    }

    // Método para enviar una notificación de pago
    public void sendPaymentNotification( Date dueDate, Student student) {
            notificationSystem.notifyPaymentDue(dueDate, student);
            System.out.println("Notificación de pago enviada.");
            
    }

    // Método para revisar todas las notificaciones enviadas
    public void reviewNotifications() {
        List<String> notificationsReview = notificationSystem.getAllNotifications();
        if (notificationsReview.isEmpty()) {
            System.out.println("No hay notificaciones.");
        } else {
            System.out.println("Notificaciones:");
            for (int i = 0; i < notificationsReview.size(); i++) {
                System.out.println((i + 1) + ". " + notificationsReview.get(i));
            }
        }
    }

    // Método para eliminar una notificación
    public void removeNotification(int index) {
        notificationSystem.removeNotification(index);
        System.out.println("Notificación eliminada.");
    }


}
