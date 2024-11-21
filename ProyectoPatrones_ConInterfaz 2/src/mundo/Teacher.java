/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j8318
 */
public class Teacher {
    private static List<String> authorizedId = new ArrayList<>();
    private String name;
    private String id;

    public Teacher(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    // Agregar profesor a la lista de autorizados
    public void addAuthorizedProfessor(String id) {
        authorizedId.add(id);
    }

    // Verificar si el profesor está autorizado
    public boolean isAuthorized(String id) {
        if(id =="no"){return false;}
        System.out.println("Profesor Autorizado");
        return authorizedId.contains(id);
    }
}
