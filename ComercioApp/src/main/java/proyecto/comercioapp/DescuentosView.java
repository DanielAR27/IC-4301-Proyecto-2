package proyecto.comercioapp;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import proyecto.db.DBMediator;

public class DescuentosView extends JFrame {
    private JList<String> descuentoList;
    private DefaultListModel<String> listModel;
    private JTextField productoNameField, porcentajeField;
    private JDateChooser fechaInicioChooser, fechaFinChooser;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver;
    private Map<Integer, List<Object>> descuentoIDs = new HashMap<>();
    private int usuarioID;

    public DescuentosView(int usuarioID) {
        setTitle("Gestionar Descuentos");
        setResizable(false);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.usuarioID = usuarioID;

        // Modelo de lista para mostrar descuentos
        listModel = new DefaultListModel<>();
        descuentoList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(descuentoList);
        descuentoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Cargar descuentos al inicio
        cargarDescuentos();

        // Campos para ingresar datos del descuento
        productoNameField = new JTextField(15);
        porcentajeField = new JTextField(10);
        fechaInicioChooser = new JDateChooser();
        fechaFinChooser = new JDateChooser();
        fechaInicioChooser.setDateFormatString("yyyy-MM-dd");
        fechaFinChooser.setDateFormatString("yyyy-MM-dd");

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        // Panel para los campos de entrada
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldsPanel = new JPanel(new FlowLayout());
        fieldsPanel.add(new JLabel("Producto:"));
        fieldsPanel.add(productoNameField);
        fieldsPanel.add(new JLabel("Porcentaje:"));
        fieldsPanel.add(porcentajeField);
        fieldsPanel.add(new JLabel("Fecha Inicio:"));
        fieldsPanel.add(fechaInicioChooser);
        fieldsPanel.add(new JLabel("Fecha Fin:"));
        fieldsPanel.add(fechaFinChooser);
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
        agregarButton.addActionListener(this::agregarDescuento);
        editarButton.addActionListener(this::editarDescuento);
        eliminarButton.addActionListener(this::eliminarDescuento);
        btnVolver.addActionListener(this::btnVolverActionPerformed);
    }

    private void cargarDescuentos() {
        List<List<Object>> descuentos = DBMediator.getDescuentosConID(); // Método que devuelve IDs y atributos
        listModel.clear();
        descuentoIDs.clear();
        if (descuentos != null) {
            int index = 0;
            for (List<Object> descuento : descuentos) {
                int id = (int) descuento.get(0); // ID del descuento
                String productoNombre = (String) descuento.get(1);
                double porcentaje = (double) descuento.get(2);
                String fechaInicio = descuento.get(3).toString();
                String fechaFin = descuento.get(4).toString();
                listModel.addElement("Producto: " + productoNombre + " - " + porcentaje + "% (Desde: " + fechaInicio + " Hasta: " + fechaFin + ")");
                descuentoIDs.put(index++, descuento); // Mapear el índice con el ID
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar descuentos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarDescuento(ActionEvent e) {
        String productoStr = productoNameField.getText().trim();
        String porcentajeStr = porcentajeField.getText().trim();

        // Obtener fechas seleccionadas
        Date fechaInicio = fechaInicioChooser.getDate();
        Date fechaFin = fechaFinChooser.getDate();

        if (!productoStr.isEmpty() && !porcentajeStr.isEmpty() && fechaInicio != null && fechaFin != null) {
            try {
                double porcentaje = Double.parseDouble(porcentajeStr);

                // Formatear las fechas a String
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioStr = dateFormat.format(fechaInicio);
                String fechaFinStr = dateFormat.format(fechaFin);

                // Llamar a DBMediator con las fechas formateadas
                int resultado = DBMediator.createDescuento(productoStr, porcentaje, fechaInicioStr, fechaFinStr);
                switch (resultado) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(this, "Descuento agregado con éxito.");
                        cargarDescuentos();
                        productoNameField.setText("");
                        porcentajeField.setText("");
                        fechaInicioChooser.setDate(null);
                        fechaFinChooser.setDate(null);
                    }case -1 -> {
                        JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);       
                    }case -2 ->{
                        JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la fecha de inicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);                         
                    }case -3 ->{
                        JOptionPane.showMessageDialog(this, "Verifique que el producto exista.", "Advertencia", JOptionPane.WARNING_MESSAGE);                                                 
                    }default ->{
                        JOptionPane.showMessageDialog(this, "Ya existe un descuento para el producto en el lapso establecido, intente con otra fecha.", "Advertencia", JOptionPane.WARNING_MESSAGE);                             
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Verifique que el porcentaje sea un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int getDescuentoID(int index) {
        if (index < 0 || index >= descuentoIDs.size()) {
            return -1; // Índice fuera de rango, devolver -1
        }
        return (Integer) descuentoIDs.get(index).get(0); // Devuelve el ID correspondiente
    }
    
    private String getProductoNombre(int index){
        if (index < 0 || index >= descuentoIDs.size()) {
            return ""; // Índice fuera de rango, devolver -1
        }
        return (String) descuentoIDs.get(index).get(1); // Devuelve el ID correspondiente        
    }
    
    private Double getPorcentaje(int index){
        if (index < 0 || index >= descuentoIDs.size()) {
            return null; // Índice fuera de rango, devolver -1
        }
        return (Double) descuentoIDs.get(index).get(2); // Devuelve el ID correspondiente                
    }
    
    private Date getFechaInicio(int index){
        if (index < 0 || index >= descuentoIDs.size()) {
            return null; // Índice fuera de rango, devolver -1
        }
        return (Date) descuentoIDs.get(index).get(3); // Devuelve el ID correspondiente                
    }

    private Date getFechaFin(int index){
        if (index < 0 || index >= descuentoIDs.size()) {
            return null; // Índice fuera de rango, devolver -1
        }
        return (Date) descuentoIDs.get(index).get(4); // Devuelve el ID correspondiente                
    }
    
    private void editarDescuento(ActionEvent e) {
        int selectedIndex = descuentoList.getSelectedIndex(); // Índice seleccionado
        if (selectedIndex != -1) {
            int descuentoID = getDescuentoID(selectedIndex); // Obtén el ID del descuento

            // Solicitar el nuevo Producto ID
            String productoStr = JOptionPane.showInputDialog(this, "Editar Producto:", getProductoNombre(selectedIndex));
            if (productoStr == null || productoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El producto no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Solicitar el nuevo porcentaje
                String porcentajeStr = JOptionPane.showInputDialog(this, "Editar Porcentaje:", Double.toString(getPorcentaje(selectedIndex)));
                if (porcentajeStr == null || porcentajeStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El porcentaje no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double porcentaje = Double.parseDouble(porcentajeStr.trim());

                // Configuración de JDateChooser para editar las fechas
                JDateChooser fechaInicioChooser = new JDateChooser();
                fechaInicioChooser.setDateFormatString("yyyy-MM-dd");
                // Inicializar la fecha de inicio
                fechaInicioChooser.setDate(getFechaInicio(selectedIndex));
                
                JDateChooser fechaFinChooser = new JDateChooser();
                fechaFinChooser.setDateFormatString("yyyy-MM-dd");
                // Inicializar la fecha de fin
                fechaFinChooser.setDate(getFechaFin(selectedIndex));

                // Mostrar diálogo para seleccionar nuevas fechas
                JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));
                datePanel.add(new JLabel("Fecha de Inicio:"));
                datePanel.add(fechaInicioChooser);
                datePanel.add(new JLabel("Fecha de Fin:"));
                datePanel.add(fechaFinChooser);

                int result = JOptionPane.showConfirmDialog(this, datePanel, "Editar Fechas del Descuento",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    Date nuevaFechaInicio = fechaInicioChooser.getDate();
                    Date nuevaFechaFin = fechaFinChooser.getDate();

                    if (nuevaFechaInicio == null || nuevaFechaFin == null) {
                        JOptionPane.showMessageDialog(this, "Ambas fechas deben ser seleccionadas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Llamar al método de DBMediator para actualizar el descuento
                    int resultado = DBMediator.updateDescuento(descuentoID, productoStr, porcentaje, nuevaFechaInicio, nuevaFechaFin);
                    switch(resultado) {
                        case 0-> {
                            JOptionPane.showMessageDialog(this, "Descuento actualizado con éxito.");
                            cargarDescuentos();
                        } case -1 ->{
                            JOptionPane.showMessageDialog(this, "No se ha encontrado el descuento por su ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);                          
                        }case -2 ->{
                            JOptionPane.showMessageDialog(this, "Verifique llenar todos los espacios correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);                               
                        }case -3 ->{
                            JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la fecha de inicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);                         
                        }case -4 ->{
                            JOptionPane.showMessageDialog(this, "Verifique que el producto exista.", "Advertencia", JOptionPane.WARNING_MESSAGE);                                                 
                        }default ->{
                            JOptionPane.showMessageDialog(this, "Ya existe un descuento para el producto en el lapso establecido, intente con otra fecha.", "Advertencia", JOptionPane.WARNING_MESSAGE);                             
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Verifique que el porcentaje deben sea un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un descuento para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }



    private void eliminarDescuento(ActionEvent e) {
        int selectedIndex = descuentoList.getSelectedIndex(); // Obtener índice seleccionado
        if (selectedIndex != -1) {
            int descuentoID = getDescuentoID(selectedIndex); // Obtener ID del descuento
            String descuentoInfo = listModel.get(selectedIndex); // Obtener información para confirmación

            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este descuento?\n" + descuentoInfo,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deleteDescuento(descuentoID);
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Descuento eliminado con éxito.");
                    cargarDescuentos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar descuento.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un descuento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnVolverActionPerformed(ActionEvent e) {
        this.dispose(); // Cierra la ventana actual
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }
}
