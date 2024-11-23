package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import proyecto.db.DBMediator;

public class MarcasView extends JFrame {
    private JList<String> marcaList;
    private DefaultListModel<String> listModel;
    private JTextField marcaField;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver; // Botones
    private Map<Integer, List<Object>> marcaIDs = new HashMap<>(); // Mapea índices a IDs de marcas
    private int usuarioID;
    
    public MarcasView(int usuarioID) {
        setTitle("Gestionar Marcas");
        setResizable(false);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.usuarioID = usuarioID;

        // Modelo de lista para mostrar marcas
        listModel = new DefaultListModel<>();
        marcaList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(marcaList);
        marcaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Cargar marcas al inicio
        cargarMarcas();
        
        marcaField = new JTextField();

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(marcaField, BorderLayout.CENTER);
        inputPanel.add(agregarButton, BorderLayout.EAST);
        
        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(btnVolver);

        // Agregar componentes al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para botones
        agregarButton.addActionListener(this::agregarMarca);
        editarButton.addActionListener(this::editarMarca);
        eliminarButton.addActionListener(this::eliminarMarca);
        btnVolver.addActionListener(this::btnVolverActionPerformed);
    }

    private void cargarMarcas() {
        List<List<Object>> marcas = DBMediator.getMarcasConID(); // Método que devuelve IDs y nombres
        listModel.clear();
        marcaIDs.clear();
        if (marcas != null) {
            int index = 0;
            for (List<Object> marca : marcas) {
                listModel.addElement((String)  marca.get(1)); // Agregar el nombre al modelo de la lista
                marcaIDs.put(index++, marca); // Mapear el índice con el ID
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar marcas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarMarca(ActionEvent e) {
        String nuevaMarca = marcaField.getText().trim();
        if (!nuevaMarca.isEmpty()) {
            int resultado = DBMediator.createMarca(nuevaMarca);
            switch(resultado) {
                case 0 ->{
                    JOptionPane.showMessageDialog(this, "Marca agregada con éxito.");
                    cargarMarcas();
                    marcaField.setText("");
                } case -1 -> {
                    JOptionPane.showMessageDialog(this, "Verifique que el nombre no se encuentre asociado a otra marca.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } default -> {
                    JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);                          
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de la marca no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int getMarcaID(int index) {
        if (index < 0 || index >= marcaIDs.size()) {
            return -1; // Índice fuera de rango, devolver -1
        }
        return (Integer) marcaIDs.get(index).get(0); // Devuelve el ID correspondiente
    }
    
    private String getMarcaNombre(int index){
        if (index < 0 || index >= marcaIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) marcaIDs.get(index).get(1); // Devuelve el nombre de la marca      
    }

    private void editarMarca(ActionEvent e) {
        int selectedIndex = marcaList.getSelectedIndex(); // Índice seleccionado
        if (selectedIndex != -1) {
            int marcaID = getMarcaID(selectedIndex); // Obtén el ID de la marca
            String nombre = JOptionPane.showInputDialog(this, "Editar Nombre:", getMarcaNombre(selectedIndex));

            // Validar que los campos no sean null ni estén vacíos
            if (nombre != null  && !nombre.trim().isEmpty()) {
                int resultado = DBMediator.updateMarca(marcaID, nombre.trim()); // Actualizar provincia

                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "Marca actualizada con éxito.");
                        cargarMarcas();
                    }
                    case -1 -> JOptionPane.showMessageDialog(this, "No se ha encontrado la marca por su ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -2 -> JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -3 -> JOptionPane.showMessageDialog(this, "Verifique que el nombre no se encuentre asociado a otra marca.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    default -> JOptionPane.showMessageDialog(this, "Ocurrió un error. Verifique los datos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una marca para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } 
    }

    private void eliminarMarca(ActionEvent e) {
        int selectedIndex = marcaList.getSelectedIndex(); // Obtener índice seleccionado
        if (selectedIndex != -1) {
            int marcaID = getMarcaID(selectedIndex); // Obtener ID del país
            String nombre = getMarcaNombre(selectedIndex); // Obtener el nombre de la marca

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la marca \"" + nombre + "\"?\nEsta acción es irreversible.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deleteMarca(marcaID);
                switch(resultado) {
                    case 0 ->{
                        JOptionPane.showMessageDialog(this, "Marca eliminada con éxito.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
                        cargarMarcas();
                    }case -1 ->{
                        JOptionPane.showMessageDialog(this, "La marca está asociada a un producto, intente con otra.", "Error", JOptionPane.ERROR_MESSAGE);
                    }default ->{
                        JOptionPane.showMessageDialog(this, "Error al eliminar la marca.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una marca para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnVolverActionPerformed(ActionEvent e) {
        this.dispose(); // Cierra la ventana actual
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }

}
