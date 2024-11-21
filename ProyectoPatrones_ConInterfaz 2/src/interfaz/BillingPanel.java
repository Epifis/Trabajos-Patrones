package interfaz;

import controlador.UniversityController;
import mundo.Charge;
import mundo.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BillingPanel extends JPanel {
    private UniversityController controller;
    private JComboBox<String> studentComboBox;
    private DefaultListModel<Charge> chargesListModel;
    private JList<Charge> chargesList;
    private JTextField chargeDescriptionField;
    private JTextField chargeAmountField;
    private JButton addChargeButton;
    private JButton generateInvoiceButton;
    

    public BillingPanel(UniversityController controller) {
        this.controller = controller;
        initializeUI();
        System.out.println("hi");
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Panel para selección de estudiante
        JPanel studentPanel = new JPanel(new FlowLayout());
        studentPanel.add(new JLabel("Seleccione un estudiante:"));
        studentComboBox = new JComboBox<>();
        for (Student student : controller.getStudents()) {
            studentComboBox.addItem(student.getName());
        }
        studentPanel.add(studentComboBox);
        add(studentPanel, BorderLayout.NORTH);

        // Panel para agregar cargos
        JPanel chargesPanel = new JPanel(new BorderLayout());
        chargesListModel = new DefaultListModel<>();
        chargesList = new JList<>(chargesListModel);
        chargesPanel.add(new JScrollPane(chargesList), BorderLayout.CENTER);

        JPanel chargeInputPanel = new JPanel(new GridLayout(4, 2));
        chargeInputPanel.add(new JLabel("Descripción del cargo:"));
        chargeDescriptionField = new JTextField();
        chargeInputPanel.add(chargeDescriptionField);

        chargeInputPanel.add(new JLabel("Monto del cargo:"));
        chargeAmountField = new JTextField();
        chargeInputPanel.add(chargeAmountField);
        

        addChargeButton = new JButton("Agregar cargo");
        chargeInputPanel.add(addChargeButton);
        chargesPanel.add(chargeInputPanel, BorderLayout.SOUTH);
        add(chargesPanel, BorderLayout.CENTER);

        // Botón para generar factura
        generateInvoiceButton = new JButton("Generar Factura");
        add(generateInvoiceButton, BorderLayout.SOUTH);
        

        // Listeners
        addListeners();
    }

    private void addListeners() {
        // Agregar cargo
        addChargeButton.addActionListener(e -> {
            String description = chargeDescriptionField.getText().trim();
            String amountText = chargeAmountField.getText().trim();

            if (description.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
                return;
            }
            try {
                double amount = Double.parseDouble(amountText);
                chargesListModel.addElement(new Charge(description, amount));
                chargeDescriptionField.setText("");
                chargeAmountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.");
            }
        });

        // Generar factura
        generateInvoiceButton.addActionListener(e -> {
            int selectedStudentIndex = studentComboBox.getSelectedIndex();
            if (selectedStudentIndex < 0) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un estudiante.");
                return;
            }

            Student selectedStudent = controller.getStudents().get(selectedStudentIndex);
            List<Charge> charges = new ArrayList<>();
            for (int i = 0; i < chargesListModel.size(); i++) {
                charges.add(chargesListModel.getElementAt(i));
            }

            if (charges.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor agregue al menos un cargo.");
                return;
            }
            
            controller.generateInvoice(selectedStudent, charges, controller.getDateInicial(), 30);
            controller.sendPaymentNotification(controller.getInvoicesList().get(controller.getInvoicesList().size()-1).getDateFinal(), selectedStudent);
            for (int i = 0; i < chargesListModel.size(); i++) {
                JOptionPane.showMessageDialog(this, chargesListModel.getElementAt(i).toString());
                controller.getInvoicesList().get(i);
            }
            JOptionPane.showMessageDialog(this, "Factura generada exitosamente\n"+"Con fecha " + controller.getDateInicial().toString() + " y 30 días de pago");
            chargesListModel.clear();
        });
    }
}
