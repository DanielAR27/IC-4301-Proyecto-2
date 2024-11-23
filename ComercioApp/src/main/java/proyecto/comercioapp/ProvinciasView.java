package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import proyecto.db.DBMediator;

public class ProvinciasView extends JFrame {
    private JList<String> provinciaList;
    private DefaultListModel<String> listModel;
    private JTextField nombreField, paisField;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver;
    private Map<Integer, List<Object>> provinciaIDs = new HashMap<>();
    private int usuarioID;

    public ProvinciasView(int usuarioID) {
        setTitle("Gestionar Provincias");
        setResizable(false);
        setSize(500, 500); // Ajuste del tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de lista para mostrar provincias
        listModel = new DefaultListModel<>();
        provinciaList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(provinciaList);
        provinciaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Cargar provincias al inicio
        cargarProvincias();

        // Campos para ingresar datos de la provincia
        nombreField = new JTextField(20); // Campo para ingresar el nombre
        paisField = new JTextField(10);  // Campo para ingresar el ID del país

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        // Panel para el campo y botón de agregar
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldsPanel = new JPanel(new FlowLayout());
        fieldsPanel.add(new JLabel("Nombre:"));
        fieldsPanel.add(nombreField);
        fieldsPanel.add(new JLabel("Código del País:"));
        fieldsPanel.add(paisField);
        inputPanel.add(fieldsPanel);
        inputPanel.add(agregarButton);

        // Panel para los botones de editar, eliminar y volver
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(btnVolver);

        // Agregar componentes al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para botones
        agregarButton.addActionListener(this::agregarProvincia);
        editarButton.addActionListener(this::editarProvincia);
        eliminarButton.addActionListener(this::eliminarProvincia);
        btnVolver.addActionListener(this::btnVolverActionPerformed);
    }

    private void cargarProvincias() {
        List<List<Object>> provincias = DBMediator.getProvinciasConID(); // Método que devuelve IDs y atributos
        listModel.clear();
        provinciaIDs.clear();
        if (provincias != null) {
            int index = 0;
            for (List<Object> provincia : provincias) {
                int id = (int) provincia.get(0); // ID de la provincia
                String nombreProvincia = (String) provincia.get(1); // Nombre de la provincia
                String nombrePais = (String) provincia.get(2);
                String codigoPais = (String) provincia.get(3);
                listModel.addElement(nombreProvincia + " - " + nombrePais + " (" +codigoPais + ")");
                provinciaIDs.put(index++, provincia); // Mapear el índice con el ID
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar provincias.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProvincia(ActionEvent e) {
        String nombre = nombreField.getText().trim();
        String paisCode = paisField.getText().trim();

        if (!nombre.isEmpty() && !paisCode.isEmpty()) {
                int resultado = DBMediator.createProvincia(nombre, paisCode); // Cambiar orden de parámetros
                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "Provincia agregada con éxito.");
                        cargarProvincias();
                        nombreField.setText("");
                        paisField.setText("");
                    } case -1 -> {
                        JOptionPane.showMessageDialog(this, "Verifique que el código del país exista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } default -> {
                        JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);                          
                    }
                }
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


    private int getProvinciaID(int index) {
        if (index < 0 || index >= provinciaIDs.size()) {
            return -1; // Índice fuera de rango, devolver -1
        }
        return (Integer) provinciaIDs.get(index).get(0); // Devuelve el ID correspondiente
    }
    
    private String getProvinciaNombre(int index){
        if (index < 0 || index >= provinciaIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) provinciaIDs.get(index).get(1); // Devuelve el nombre de la provincia (Uruguay reference)  
    }
    
    private String getPaisCodigo(int index){
        if (index < 0 || index >= provinciaIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) provinciaIDs.get(index).get(3); // Devuelve el nombre de la provincia (Uruguay reference)  
    }

    private void editarProvincia(ActionEvent e) {
        int selectedIndex = provinciaList.getSelectedIndex(); // Índice seleccionado

        if (selectedIndex != -1) {
            int provinciaID = getProvinciaID(selectedIndex); // Obtén el ID de la provincia
            String nombre = JOptionPane.showInputDialog(this, "Editar Nombre:", getProvinciaNombre(selectedIndex));
            String paisCode = JOptionPane.showInputDialog(this, "Editar Código:", getPaisCodigo(selectedIndex));

            // Validar que los campos no sean null ni estén vacíos
            if (nombre != null && paisCode != null && !nombre.trim().isEmpty() && !paisCode.trim().isEmpty()) {
                int resultado = DBMediator.updateProvincia(provinciaID, nombre.trim(), paisCode.trim()); // Actualizar provincia

                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "Provincia actualizada con éxito.");
                        cargarProvincias();
                    }
                    case -1 -> JOptionPane.showMessageDialog(this, "No se ha encontrado la provincia por su ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -2 -> JOptionPane.showMessageDialog(this, "Verifique que el código del país exista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    default -> JOptionPane.showMessageDialog(this, "Ocurrió un error. Verifique los datos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una provincia para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void eliminarProvincia(ActionEvent e) {
        int selectedIndex = provinciaList.getSelectedIndex(); // Obtener índice seleccionado
        if (selectedIndex != -1) {
            int provinciaID = getProvinciaID(selectedIndex); // Obtener ID de la provincia
            String provinciaNombre = listModel.get(selectedIndex).split(" - ")[0]; // Obtener nombre para confirmación

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la provincia \"" + provinciaNombre + "\"?\nEsta acción es irreversible.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deleteProvincia(provinciaID);
                switch(resultado) {
                    case 0 ->{
                    JOptionPane.showMessageDialog(this, "Provincia eliminada con éxito.");
                    cargarProvincias();
                    }case -1 ->{
                        JOptionPane.showMessageDialog(this, "La provincia está asociada a una dirección, intente con otra.", "Error", JOptionPane.ERROR_MESSAGE);
                    }default ->{
                        JOptionPane.showMessageDialog(this, "Error al eliminar la provincia.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una provincia para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnVolverActionPerformed(ActionEvent e) {
        this.dispose(); // Cierra la ventana actual
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }
}

