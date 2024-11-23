/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.comercioapp;
import javax.swing.JFrame;       // Para la clase JFrame
import javax.swing.JButton;     // Para botones
import javax.swing.JLabel;      // Para etiquetas
import javax.swing.JTextField;  // Para campos de texto
import javax.swing.JOptionPane; // Para cuadros de diálogo
import java.awt.GridLayout;     // Para el diseño del formulario
import java.util.List;          // Para trabajar con listas
import proyecto.db.DBMediator;

public class EditarProductoView extends JFrame {
    private int productoID;

    public EditarProductoView(int productoID) {
        this.productoID = productoID;
        setTitle("Editar Producto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Campos para editar
        JTextField nombreField = new JTextField();
        JTextField precioField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField categoriaField = new JTextField();
        JTextField marcaField = new JTextField();

        // Cargar datos actuales del producto desde DBMediator
        try {
            List<Object> productoData = DBMediator.getProductoPorID(productoID);
            if (productoData != null) {
                nombreField.setText((String) productoData.get(1));
                precioField.setText(String.valueOf(productoData.get(2)));
                stockField.setText(String.valueOf(productoData.get(3)));
                categoriaField.setText((String) productoData.get(4));
                marcaField.setText((String) productoData.get(5));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar componentes al formulario
        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Precio:"));
        add(precioField);
        add(new JLabel("Stock:"));
        add(stockField);
        add(new JLabel("Categoría:"));
        add(categoriaField);
        add(new JLabel("Marca:"));
        add(marcaField);

        // Botón para guardar cambios
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            try {
                String nombre = nombreField.getText().trim();
                float precio = Float.parseFloat(precioField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String categoria = categoriaField.getText().trim();
                String marca = marcaField.getText().trim();

                if (nombre.isEmpty() || categoria.isEmpty() || marca.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int resultado = DBMediator.updateProducto(productoID, nombre, precio, stock, categoria, marca);
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        add(new JLabel()); // Espacio vacío
        add(saveButton);
    }
}
