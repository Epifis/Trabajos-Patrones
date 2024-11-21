package mundo;

public class ClassSessionProxy implements ClassSession {
    private RealClassSession realSession;
    private Teacher teacher;

    public ClassSessionProxy(RealClassSession realSession, Teacher teacher) {
        this.realSession = realSession;
        this.teacher = teacher;
    }
    public ClassSessionProxy( Teacher teacher) {
        this.teacher = teacher;
    }
    @Override
    public void scheduleClass() {
        if (teacher.isAuthorized(teacher.getId())) {
            this.realSession.scheduleClass();
        } else {
            System.out.println("El profesor no está autorizado para programar clases.");
        }
    }

    @Override
    public void cancelClass() {
        if (teacher.isAuthorized(teacher.getId())) {
            realSession.cancelClass();
        } else {
            System.out.println("El profesor no está autorizado para cancelar clases.");
        }
    }

    @Override
    public String getClassName() {
        return realSession.getClassName();
    }

    @Override
    public void setClassName(String name) {
        realSession.setClassName(name);
    }

    public Teacher getTeacher() {
        return teacher;
    }
    
}
