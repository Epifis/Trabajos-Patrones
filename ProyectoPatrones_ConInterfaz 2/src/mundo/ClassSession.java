package mundo;

    public interface ClassSession {
    void scheduleClass();
    void cancelClass();
    void setClassName(String name); 
    String getClassName();
    Teacher getTeacher();
}

