package interfaz;

import controlador.UniversityController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mundo.ClassSession;
import mundo.Invoice;
import mundo.PaymentNotificationSystem;
import mundo.Student;
import mundo.Teacher;

public class FramePrincipal extends javax.swing.JFrame {

    public UniversityController ctrl = new UniversityController();

    public FramePrincipal() {
        initComponents();
        configurarOpciones();
        cargarDatosIniciales();
    }

    private void configurarOpciones() {
        // Agregamos las opciones al choice1
        choice1.add("Seleccionar");
        choice1.add("Sistema de Facturación");
        choice1.add("Sistema de Notificaciones");
        choice1.add("Gestión de Clases");
        choice1.add("Gestión de Usuarios");

        // Listener para detectar la opción seleccionada
        choice1.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                String opcionSeleccionada = evt.getItem().toString();
                abrirNuevoFrame(opcionSeleccionada);
            }
        });
    }

    private void abrirNuevoFrame(String opcion) {
        // Crear nuevo frame
        JFrame nuevoFrame = new JFrame(opcion);
        nuevoFrame.setSize(400, 300);
        nuevoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        nuevoFrame.setLocationRelativeTo(null);

        // Panel y choice
        java.awt.Panel panel = new java.awt.Panel();
        java.awt.Choice nuevoChoice = new java.awt.Choice();
        panel.add(nuevoChoice);

        // Llenar opciones según la selección
        agregarOpciones(nuevoChoice, opcion);

        // Listener para las opciones del nuevo frame
        nuevoChoice.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                String seleccion = evt.getItem().toString();
                System.out.println("Seleccionaste: " + seleccion);
            }
        });

        // Agregar panel al nuevo frame y hacerlo visible
        nuevoFrame.add(panel);
        nuevoFrame.setVisible(true);
    }

    private void agregarOpciones(java.awt.Choice choice, String opcion) {
        choice.add("Seleccionar");
        switch (opcion) {
            case "Sistema de Facturación":
                choice.add("Crear factura");
                choice.add("Listar facturas");
                choice.add("Eliminar factura");
                break;
            case "Sistema de Notificaciones":
                String professorId = JOptionPane.showInputDialog("Ingrese su id");
                if (ctrl.getTeacherById(professorId).isAuthorized(professorId)) {
                    choice.add("Enviar notificación de pago");
                    choice.add("Revisar notificaciones");
                    choice.add("Eliminar notificación");
                    break;
                }

                JOptionPane.showMessageDialog(this, "Su Id no está en el sistema");
                break;
            case "Gestión de Clases":
                choice.add("Crear clase");
                choice.add("Listar clases");
                choice.add("Eliminar clase");
                break;
            case "Gestión de Usuarios":
                choice.add("Agregar usuario");
                choice.add("Listar usuarios");
                choice.add("Eliminar usuario");
                choice.add("Actualizar usuario");
                break;
        }

        //
        choice.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                String seleccion = evt.getItem().toString();
                switch (seleccion) {
                    case "Crear factura":
                        mostrarBillingPanel();
                        break;
                    case "Listar facturas":
                        if (ctrl.getInvoicesList().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay facturas");
                            break;
                        } else {
                            StringBuilder facturaDetails = new StringBuilder();

                            // Crear un mapa de facturas por estudiante
                            Map<String, List<Invoice>> facturasPorEstudiante = new HashMap<>();
                            for (Invoice invoice : ctrl.getInvoicesList()) {
                                facturasPorEstudiante
                                        .computeIfAbsent(invoice.getStudent().getName(), k -> new ArrayList<>())
                                        .add(invoice);
                            }

                            // Recorrer estudiantes y sus facturas
                            for (int i = 0; i < ctrl.getStudents().size(); i++) {
                                String studentName = ctrl.getStudentNames().get(i);
                                facturaDetails.append("Estudiante: ").append(studentName).append("\n");

                                List<Invoice> facturas = facturasPorEstudiante.get(studentName);
                                if (facturas != null && !facturas.isEmpty()) {
                                    double totalCargos = 0.0;
                                    for (int j = 0; j < facturas.size(); j++) {
                                        Invoice invoice = facturas.get(j);
                                        facturaDetails.append("Factura #").append(j + 1)
                                                .append("\n");
                                        invoice.generateDocument(); // Llamar al método sin agregar su valor a la cadena.
                                        facturaDetails.append("\nTotal: ").append(invoice.calculateTotal())
                                                .append("\n\n");
                                        totalCargos += invoice.calculateTotal();
                                        facturaDetails.append("\n con fecha inicial ").append(ctrl.getDateInicial()).append(" y fecha máxima de pago").append(invoice.getDateFinal());
                                    }
                                    facturaDetails.append("Total cargos del estudiante: ").append(totalCargos).append("\n\n");
                                } else {
                                    facturaDetails.append("No tiene facturas.\n\n");
                                }
                            }

                            // Mostrar todo el contenido en un solo mensaje
                            JOptionPane.showMessageDialog(this, facturaDetails.toString());
                        }
                        break;
                    case "Eliminar factura":
                        if (ctrl.getInvoicesList().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay facturas para eliminar.");
                            break;
                        }

                        // Mostrar un diálogo para que el usuario seleccione la factura a eliminar
                        String[] invoiceIds = new String[ctrl.getInvoicesList().size()];
                        for (int i = 0; i < ctrl.getInvoicesList().size(); i++) {
                            invoiceIds[i] = "Factura #" + (i + 1);
                        }

                        String selectedInvoice = (String) JOptionPane.showInputDialog(
                                this,
                                "Seleccione la factura a eliminar:",
                                "Eliminar Factura",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                invoiceIds,
                                invoiceIds[0]
                        );

                        if (selectedInvoice != null) {
                            // Obtener el índice de la factura seleccionada
                            int invoiceIndex = Integer.parseInt(selectedInvoice.split("#")[1].trim()) - 1;

                            // Eliminar la factura de la lista
                            ctrl.getInvoicesList().remove(invoiceIndex);
                            JOptionPane.showMessageDialog(this, "Factura eliminada exitosamente.");

                            // Actualizar la lista de facturas (si es necesario)
                            StringBuilder facturaDetails = new StringBuilder();
                            for (int i = 0; i < ctrl.getStudents().size(); i++) {
                                // Generar el encabezado de cada estudiante
                                facturaDetails.append("Estudiante: ").append(ctrl.getStudentNames().get(i)).append("\n");

                                // Variables para calcular el total de los cargos
                                double totalCargos = 0.0;

                                // Iterar sobre las facturas y agregar los detalles
                                for (int j = 0; j < ctrl.getInvoicesList().size(); j++) {
                                    Invoice invoice = ctrl.getInvoicesList().get(j);

                                    if (invoice.getStudent().getName().equals(ctrl.getStudents().get(i).getName())) {
                                        facturaDetails.append("Factura #").append(j + 1)
                                                .append("\n");

                                        // Llamamos al método generateDocument() sin concatenar su valor
                                        invoice.generateDocument();  // Este método genera el documento pero no devuelve nada
                                        facturaDetails.append("Factura generada.\n");  // Indicamos que la factura fue generada

                                        facturaDetails.append("\nTotal: ").append(invoice.calculateTotal())
                                                .append("\n\n");
                                        totalCargos += invoice.calculateTotal();  // Sumar el total de cargos de este estudiante
                                    }
                                }

                                // Mostrar el total de cargos por estudiante
                                facturaDetails.append("Total cargos del estudiante: ").append(totalCargos).append("\n\n");
                            }

                            // Mostrar la lista actualizada de facturas
                            JOptionPane.showMessageDialog(this, facturaDetails.toString());
                        }
                        break;
                    case "Enviar notificación de pago":
                        String dateString = JOptionPane.showInputDialog("Ingrese la fecha de pago (DD/MM/YYYY): ");
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        Date date;
                    try {
                        date = formato.parse(dateString);
                    } catch (ParseException ex) {
                        date= ctrl.getCal().getTime();
                    }
                        String student = JOptionPane.showInputDialog("Ingrese el nombre del estudiante al que se le enviará la notificación: ");
                        if(ctrl.getStudentByName(student).getName().equals(student)){
                            Student studentForNotification = ctrl.getStudentByName(student);
                        ctrl.sendPaymentNotification(date, studentForNotification);
                        JOptionPane.showMessageDialog(this, "Se envió la notificación de pago con fecha de pago " + date.toString() + " para el estudiante: " + studentForNotification.getName());
                        break;
                        }
                        JOptionPane.showMessageDialog(this, "No se envió la notificación de pago con fecha de pago ");
                        break;
                    case "Revisar notificaciones":
                        if (ctrl.getNotificaciones().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay notificaciones.");
                        } else {
                            StringBuilder notificacionesList = new StringBuilder();
                            for (String notificacion : ctrl.getNotificaciones()) {
                                notificacionesList.append(notificacion).append("\n");
                            }
                            JOptionPane.showMessageDialog(this, notificacionesList.toString());
                        }
                        break;
                    case "Eliminar notificación":
                        String eliminarNotificacion = JOptionPane.showInputDialog("Ingrese el número de la notificación a eliminar:");
                        if (eliminarNotificacion != null && !eliminarNotificacion.isEmpty()) {
                            try {
                                int indice = Integer.parseInt(eliminarNotificacion) - 1;  // Convertir a índice (restamos 1)
                                if (indice >= 0 && indice < ctrl.getNotificaciones().size()) {
                                    ctrl.removeNotification(indice);
                                    JOptionPane.showMessageDialog(this, "Notificación eliminada.");
                                } else {
                                    JOptionPane.showMessageDialog(this, "Número de notificación no válido.");
                                }
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.");
                            }
                        }
                        break;
                    case "Crear clase":
                        String nombreClase = JOptionPane.showInputDialog("Ingrese el nombre de la clase:");
                        List<String> nombresProfesores = new ArrayList<>();

                        // Obtener los nombres de los profesores disponibles
                        for (Teacher teacher : ctrl.getTeachers()) {
                            nombresProfesores.add(teacher.getName());
                        }

                        String profesorSeleccionado = (String) JOptionPane.showInputDialog(
                                this,
                                "Seleccione un profesor:",
                                "Asignar Profesor",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                nombresProfesores.toArray(),
                                nombresProfesores.get(0)
                        );

                        if (profesorSeleccionado != null && nombreClase != null && !nombreClase.isEmpty()) {
                            Teacher teacher = ctrl.getTeacherByName(profesorSeleccionado);
                            ctrl.crearClase(nombreClase, teacher);
                            JOptionPane.showMessageDialog(this, "Clase creada exitosamente.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Datos incompletos. No se creó la clase.");
                        }
                        break;

                    case "Listar clases":
                        if (ctrl.getClases().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay clases disponibles.");
                        } else {
                            StringBuilder claseDetails = new StringBuilder();
                            for (ClassSession clase : ctrl.getClases()) {
                                claseDetails.append("Clase: ").append(clase.getClassName()).append("\n")
                                        .append("Profesor: ").append(clase.getTeacher().getName()).append("\n\n");
                            }
                            JOptionPane.showMessageDialog(this, claseDetails.toString());
                        }
                        break;

                    case "Eliminar clase":
                        if (ctrl.getClases().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay clases para eliminar.");
                        } else {
                            List<String> nombresClases = new ArrayList<>();
                            for (ClassSession clase : ctrl.getClases()) {
                                nombresClases.add(clase.getClassName());
                            }
                            
                            String claseSeleccionada = (String) JOptionPane.showInputDialog(
                                    this,
                                    "Seleccione la clase a eliminar:",
                                    "Eliminar Clase",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    nombresClases.toArray(),
                                    nombresClases.get(0)
                            );

                            if (claseSeleccionada != null) {
                                ctrl.eliminarClase(claseSeleccionada);
                                JOptionPane.showMessageDialog(this, "Clase eliminada exitosamente.");
                            }
                        }
                        break;

                    case "Agregar usuario":
                        String nombreUsuario = JOptionPane.showInputDialog("Ingrese el nombre del usuario:");
                        String emailUsuario = JOptionPane.showInputDialog("Ingrese el correo del usuario:");
                        String estOprof = JOptionPane.showInputDialog("Ingrese 1 para estudiante o 2 para profesor");

                        if (nombreUsuario != null && !nombreUsuario.isEmpty() && emailUsuario != null && !emailUsuario.isEmpty()) {
                            if (estOprof.equals("1")) {
                                ctrl.crearEstudiante(nombreUsuario, emailUsuario);
                                JOptionPane.showMessageDialog(this, "Usuario estudiante creado exitosamente.");
                            } else if (estOprof.equals("2")) {
                                String autorizacion = JOptionPane.showInputDialog("Ingrese 1 si el maestro tiene autorizacion o 2 sino");
                                switch (autorizacion) {
                                    case "1":
                                        String idautorizacion = JOptionPane.showInputDialog("Ingrese el id de autorización");
                                        ctrl.crearProfesor(nombreUsuario, idautorizacion);
                                        ctrl.getTeacherByName(nombreUsuario).addAuthorizedProfessor(idautorizacion);
                                        JOptionPane.showMessageDialog(this, "Usuario profesor con autorizacion creado exitosamente.");
                                        break;
                                    case "2":
                                        ctrl.crearProfesor(nombreUsuario, null);
                                        JOptionPane.showMessageDialog(this, "Usuario profesor sin autorizacion creado exitosamente.");
                                        break;
                                }
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Datos incompletos. No se creó el usuario.");
                        }
                        break;
                    case "Listar usuarios":
                        if (ctrl.getUsuarios().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay usuarios registrados.");
                        } else {
                            StringBuilder usuarioDetails = new StringBuilder();
                            for (Student usuario : ctrl.getStudents()) {
                                usuarioDetails.append("Nombre: ").append(usuario.getName()).append("\n")
                                        .append("Email: ").append(usuario.getEmail()).append("\n\n");
                            }
                            JOptionPane.showMessageDialog(this, usuarioDetails.toString());
                        }
                        break;

                    case "Eliminar usuario":
                        if (ctrl.getUsuarios().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No hay usuarios para eliminar.");
                        } else {
                            List<String> nombresUsuarios = new ArrayList<>();
                            for (Student usuario : ctrl.getStudents()) {
                                nombresUsuarios.add(usuario.getName());
                            }

                            String usuarioSeleccionado = (String) JOptionPane.showInputDialog(
                                    this,
                                    "Seleccione el usuario a eliminar:",
                                    "Eliminar Usuario",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    nombresUsuarios.toArray(),
                                    nombresUsuarios.get(0)
                            );

                            if (usuarioSeleccionado != null) {
                                ctrl.eliminarUsuario(usuarioSeleccionado);
                                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
                            }
                        }
                        break;

                    case "Actualizar usuario":
                        String actualizarUsuario = JOptionPane.showInputDialog("Ingrese el número del usuario a actualizar:");
                        if (actualizarUsuario != null && !actualizarUsuario.isEmpty()) {
                            try {
                                int indice = Integer.parseInt(actualizarUsuario) - 1;  // Convertir a índice (restamos 1)
                                if (indice >= 0 && indice < ctrl.getUsuarios().size()) {
                                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del usuario:");
                                    String nuevoEmail = JOptionPane.showInputDialog("Ingrese el nuevo correo del usuario:");
                                    if (nuevoNombre != null && !nuevoNombre.isEmpty() && nuevoEmail != null && !nuevoEmail.isEmpty()) {
                                        ctrl.actualizarUsuario(indice, nuevoNombre, nuevoEmail);  // Suponiendo que tienes un método para actualizar el usuario
                                        JOptionPane.showMessageDialog(this, "Usuario actualizado.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "Número de usuario no válido.");
                                }
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.");
                            }
                        }
                        break;

                    default:
                        System.out.println("Opción no implementada: " + seleccion);
                        break;
                }
            }
        });

        // tabbedPane.addTab("Gestión de Clases", new ClassManagementPanel(controller));
        //
    }

    private void mostrarBillingPanel() {
        BillingPanel panelFacturas = new BillingPanel(ctrl);
        JFrame frameFacturas = new JFrame("Sistema de Facturación - Crear Factura");
        frameFacturas.add(panelFacturas); // Añadimos el BillingPanel al frame
        frameFacturas.setSize(600, 400); // Ajusta el tamaño del frame
        frameFacturas.setBackground(Color.pink);
        frameFacturas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameFacturas.setVisible(true);
    }

    private void cargarDatosIniciales() {
        // Muestra los datos iniciales en consola (puedes conectarlo con la interfaz)
        System.out.println("Estudiantes cargados:");
        ctrl.getStudents().forEach(student
                -> System.out.println("- " + student.getName() + " (" + student.getEmail() + ")"));

        System.out.println("Profesores cargados:");
        ctrl.getTeachers().forEach(teacher
                -> System.out.println("- " + teacher.getName()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new java.awt.Panel();
        SistemaUniversidadPiloto = new java.awt.Label();
        panel2 = new java.awt.Panel();
        choice1 = new java.awt.Choice();
        SeleccioneUnaOpcion = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel1.setFont(new java.awt.Font("Apple LiSung", 3, 36)); // NOI18N

        SistemaUniversidadPiloto.setFont(new java.awt.Font("Apple LiSung", 3, 36)); // NOI18N
        SistemaUniversidadPiloto.setText("Sistema Universidad Piloto");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SistemaUniversidadPiloto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(SistemaUniversidadPiloto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        choice1.setFont(new java.awt.Font("Sinhala MN", 2, 13)); // NOI18N
        choice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                choice1KeyPressed(evt);
            }
        });

        SeleccioneUnaOpcion.setFont(new java.awt.Font("Yuppy TC", 2, 14)); // NOI18N
        SeleccioneUnaOpcion.setText("Seleccione una opción");

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(choice1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(SeleccioneUnaOpcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(SeleccioneUnaOpcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        choice1.getAccessibleContext().setAccessibleName("");
        SeleccioneUnaOpcion.getAccessibleContext().setAccessibleName("Seleccione Una Opción");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void choice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_choice1KeyPressed

    }//GEN-LAST:event_choice1KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label SeleccioneUnaOpcion;
    private java.awt.Label SistemaUniversidadPiloto;
    private java.awt.Choice choice1;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    // End of variables declaration//GEN-END:variables
}
