package mundo;

import java.util.ArrayList;
import java.util.List;

public class NotificationDecorator implements ClassSession {
    private ClassSession classSession;
    private List<Student> students;

    public NotificationDecorator(ClassSession classSession, List<Student> students) {
        this.classSession = classSession;
        this.students = students;
    }

    @Override
    public void scheduleClass() {
        classSession.scheduleClass();
        notifyStudents("Clase programada: " + classSession.getClassName());
    }

    @Override
    public void cancelClass() {
        classSession.cancelClass();
        notifyStudents("Clase cancelada: " + classSession.getClassName());
    }

    @Override
    public String getClassName() {
        return classSession.getClassName();
    }

    private void notifyStudents(String message) {
        for (Student student : students) {
            student.update(message);
        }
    }

    @Override
    public void setClassName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Teacher getTeacher() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
