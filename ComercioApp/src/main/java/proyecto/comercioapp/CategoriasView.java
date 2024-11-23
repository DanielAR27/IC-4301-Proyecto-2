package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import proyecto.db.DBMediator;
import java.util.Map;
import java.util.HashMap;

public class CategoriasView extends JFrame {
    private JList<String> categoriaList;
    private DefaultListModel<String> listModel;
    private JTextField categoriaField;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver; // Botón Volver
    private Map<Integer, List<Object>> categoriaIDs = new HashMap<>();
    private int usuarioID;
    
    public CategoriasView(int usuarioID) {
        setTitle("Gestionar Categorías");
        setResizable(false);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.usuarioID = usuarioID;
        
        // Modelo de lista para mostrar categorías
        listModel = new DefaultListModel<>();
        categoriaList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(categoriaList);
        categoriaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // Cargar categorías al inicio
        cargarCategorias();

        // Campo para ingresar una nueva categoría
        categoriaField = new JTextField();

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver"); // Inicializar botón Volver

        // Panel para el campo y botón de agregar
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(categoriaField, BorderLayout.CENTER);
        inputPanel.add(agregarButton, BorderLayout.EAST);

        // Panel para los botones de editar, eliminar y volver
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(btnVolver); // Agregar botón Volver al panel

        // Agregar componentes al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para botones
        agregarButton.addActionListener(this::agregarCategoria);
        editarButton.addActionListener(this::editarCategoria);
        eliminarButton.addActionListener(this::eliminarCategoria);
        btnVolver.addActionListener(this::btnVolverActionPerformed); // Listener para Volver
    }

    private void cargarCategorias() {
        List<List<Object>> categorias = DBMediator.getCategoriasConID(); // Método que devuelve IDs y nombres
        listModel.clear();
        categoriaIDs.clear();
        if (categorias != null) {
            int index = 0;
            for (List<Object> categoria : categorias) {
                int id = (int) categoria.get(0); // ID de la categoría
                String nombre = (String) categoria.get(1); // Nombre de la categoría
                listModel.addElement(nombre); // Agregar el nombre al modelo de la lista
                categoriaIDs.put(index++, categoria); // Mapear el índice con el ID
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCategoria(ActionEvent e) {
        String nombre = categoriaField.getText().trim();
        if (!nombre.isEmpty()) {
            int resultado = DBMediator.createCategoria(nombre);
            switch(resultado) {
                case 0 -> {
                    JOptionPane.showMessageDialog(this, "Categoría agregada con éxito.");
                    cargarCategorias();
                    categoriaField.setText("");
                } case -1 -> {
                    JOptionPane.showMessageDialog(this, "Verifique que el nombre no se encuentre asociado a otra categoría.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } default -> {
                    JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);                          
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de la categoría no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int getCategoriaID(int index) {
        if (index < 0 || index >= categoriaIDs.size()) {
            return -1; // Índice fuera de rango, devolver -1
        }
        return (Integer) categoriaIDs.get(index).get(0); // Devuelve el ID correspondiente
    }

    private String getCategoriaNombre(int index){
        if (index < 0 || index >= categoriaIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) categoriaIDs.get(index).get(1); // Devuelve el nombre de la categoría     
    }
    
    private void editarCategoria(ActionEvent e) {
        int selectedIndex = categoriaList.getSelectedIndex(); // Índice seleccionado
        if (selectedIndex != -1) {
            int categoriaID = getCategoriaID(selectedIndex); // Obtén el ID de la categoría
            System.out.println("Categoria ID: " + categoriaID);
            String nombre = JOptionPane.showInputDialog(this, "Editar nombre:", getCategoriaNombre(selectedIndex));
            if (nombre != null && !nombre.trim().isEmpty()) {
                int resultado = DBMediator.updateCategoria(categoriaID, nombre.trim()); // Actualizar categoría
                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "Categoría actualizada con éxito.");
                        cargarCategorias();
                    }
                    case -1 -> JOptionPane.showMessageDialog(this, "No se ha encontrado la categoría por su ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -2 -> JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    case -3 -> JOptionPane.showMessageDialog(this, "Verifique que el nombre no se encuentre asociado a otra categoría.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    default -> JOptionPane.showMessageDialog(this, "Ocurrió un error. Verifique los datos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);                
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarCategoria(ActionEvent e) {
        int selectedIndex = categoriaList.getSelectedIndex(); // Obtener índice seleccionado
        if (selectedIndex != -1) {
            int categoriaID = getCategoriaID(selectedIndex); // Obtener ID del país
            String nombre = getCategoriaNombre(selectedIndex); // Obtener el nombre de la categoría

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la categoría \"" + nombre + "\"?\nEsta acción es irreversible.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deleteCategoria(categoriaID);
                switch(resultado) {
                    case 0 ->{
                        JOptionPane.showMessageDialog(this, "Categoría eliminada con éxito.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
                        cargarCategorias();
                    }case -1 ->{
                        JOptionPane.showMessageDialog(this, "La categoría está asociada a un producto, intente con otra.", "Error", JOptionPane.ERROR_MESSAGE);
                    }default ->{
                        JOptionPane.showMessageDialog(this, "Error al eliminar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnVolverActionPerformed(ActionEvent e) {
        this.dispose(); // Cierra la ventana actual
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }

}
