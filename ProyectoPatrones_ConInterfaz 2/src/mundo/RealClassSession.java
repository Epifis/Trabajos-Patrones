/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

public class RealClassSession implements ClassSession {
    private String className;
    private Teacher teacher;

    public RealClassSession(String className, Teacher teacher) {
        this.className = className;
        this.teacher = teacher;
    }

    @Override
    public void scheduleClass() {
        System.out.println("Clase '" + className + "' programada.");
    }

    @Override
    public void cancelClass() {
        System.out.println("Clase '" + className + "' cancelada.");
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }
    
}
