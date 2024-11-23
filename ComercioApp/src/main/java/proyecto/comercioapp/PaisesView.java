package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import proyecto.db.DBMediator;

public class PaisesView extends JFrame {
    private JList<String> paisList;
    private DefaultListModel<String> listModel;
    private JTextField codigoField, nombreField, costoEnvioField;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver;
    private Map<Integer, List<Object>> paisIDs = new HashMap<>();
    private int usuarioID;

    public PaisesView(int usuarioID) {
        setTitle("Gestionar Países");
        setResizable(false);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.usuarioID = usuarioID;

        // Modelo de lista para mostrar países
        listModel = new DefaultListModel<>();
        paisList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(paisList);
        paisList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Cargar países al inicio
        cargarPaises();

        // Campos para ingresar datos del país
        codigoField = new JTextField(5);
        nombreField = new JTextField(16);
        costoEnvioField = new JTextField(9);

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        // Panel para el campo y botón de agregar
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldsPanel = new JPanel(new FlowLayout());
        fieldsPanel.add(new JLabel("Código:"));
        fieldsPanel.add(codigoField);
        fieldsPanel.add(new JLabel("Nombre:"));
        fieldsPanel.add(nombreField);
        fieldsPanel.add(new JLabel("Costo de Envío:"));
        fieldsPanel.add(costoEnvioField);
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
        agregarButton.addActionListener(this::agregarPais);
        editarButton.addActionListener(this::editarPais);
        eliminarButton.addActionListener(this::eliminarPais);
        btnVolver.addActionListener(this::btnVolverActionPerformed);
    }

    private void cargarPaises() {
        List<List<Object>> paises = DBMediator.getPaisesConID(); // Método que devuelve IDs y atributos
        listModel.clear();
        paisIDs.clear();
        if (paises != null) {
            int index = 0;
            for (List<Object> pais : paises) {
                int id = (int) pais.get(0); // ID del país
                String codigo = (String) pais.get(1);
                String nombre = (String) pais.get(2);
                double costoEnvio = (double) pais.get(3);
                listModel.addElement(codigo + " - " + nombre + " (₡" + costoEnvio + ")");
                paisIDs.put(index++, pais); // Mapear el índice con el ID
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar países.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarPais(ActionEvent e) {
        String codigo = codigoField.getText().trim();
        String nombre = nombreField.getText().trim();
        String costoEnvioStr = costoEnvioField.getText().trim();

        if (!codigo.isEmpty() && !nombre.isEmpty() && !costoEnvioStr.isEmpty()) {
            try{
                double costoEnvio = Double.parseDouble(costoEnvioStr);
                int resultado = DBMediator.createPais(codigo, nombre, costoEnvio);
                switch(resultado) {
                        case 0 ->{
                        JOptionPane.showMessageDialog(this, "País agregado con éxito.");
                        cargarPaises();
                        codigoField.setText("");
                        nombreField.setText("");
                        costoEnvioField.setText("");
                    } case -1 -> {
                        JOptionPane.showMessageDialog(this, "Verifique que el código del país no se encuentre asociado a otro país.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }default ->{
                         JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);                   
                    }
                }
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El costo de envío debe ser un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int getPaisID(int index) {
        if (index < 0 || index >= paisIDs.size()) {
            return -1; // Índice fuera de rango, devolver -1
        }
        return (Integer) paisIDs.get(index).get(0); // Devuelve el ID correspondiente
    }

    private String getCodigo(int index){
        if (index < 0 || index >= paisIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) paisIDs.get(index).get(1); // Devuelve el código del país  
    }
  
    private String getPaisNombre(int index){
        if (index < 0 || index >= paisIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) paisIDs.get(index).get(2); // Devuelve el código del país  
    }
    
    private double getCostoEnvio(int index) {
        if (index < 0 || index >= paisIDs.size()) {
            return -1.0; // Índice fuera de rango, devolver -1.0
        }

        // Asegurarse de manejar el valor como Double y limitar a 2 decimales
        double costoEnvio = (Double) paisIDs.get(index).get(3);
        return Math.round(costoEnvio * 100) / 100.0;
    }


    
    private void editarPais(ActionEvent e) {
        int selectedIndex = paisList.getSelectedIndex(); // Índice seleccionado

        if (selectedIndex != -1) {
            int paisID = getPaisID(selectedIndex); // Obtén el ID del país
            String codigo = JOptionPane.showInputDialog(this, "Editar Código:", getCodigo(selectedIndex));
            String nombre = JOptionPane.showInputDialog(this, "Editar Nombre:", getPaisNombre(selectedIndex));
            String costoEnvioStr = JOptionPane.showInputDialog(this, "Editar Costo de Envío:", Double.toString(getCostoEnvio(selectedIndex)));

            if (codigo == null || nombre == null || costoEnvioStr == null || 
                codigo.trim().isEmpty() || nombre.trim().isEmpty() || costoEnvioStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double costoEnvio = Double.parseDouble(costoEnvioStr.trim());
                int resultado = DBMediator.updatePais(paisID, codigo.trim(), nombre.trim(), costoEnvio);

                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "País actualizado con éxito.");
                        cargarPaises();
                    }
                    case -1 -> JOptionPane.showMessageDialog(this, "No se ha encontrado el país por su ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -2 -> JOptionPane.showMessageDialog(this, "El código del país ya está asociado a otro país.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    default -> JOptionPane.showMessageDialog(this, "Ocurrió un error. Verifique los datos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El costo de envío debe ser un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Debe seleccionar un país para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarPais(ActionEvent e) {
        int selectedIndex = paisList.getSelectedIndex(); // Obtener índice seleccionado
        if (selectedIndex != -1) {
            int paisID = getPaisID(selectedIndex); // Obtener ID del país
            String paisNombre = getPaisNombre(selectedIndex);

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el país \"" + paisNombre + "\"?\nEsta acción es irreversible.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deletePais(paisID);
                switch(resultado) {
                    case 0 ->{
                        JOptionPane.showMessageDialog(this, "País eliminado con éxito.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
                        cargarPaises();
                    }case -1 ->{
                        JOptionPane.showMessageDialog(this, "El país está asociado a una dirección, intente con otro.", "Error", JOptionPane.ERROR_MESSAGE);
                    }default ->{
                        JOptionPane.showMessageDialog(this, "Error al eliminar el país.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un país para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnVolverActionPerformed(ActionEvent e) {
        this.dispose(); // Cierra la ventana actual
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }
}

