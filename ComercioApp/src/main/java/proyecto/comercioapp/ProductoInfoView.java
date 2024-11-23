/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import proyecto.db.DBMediator;

public class ProductoInfoView extends JDialog {

    public ProductoInfoView(JFrame parent, int usuarioID, int productoID) {
        super(parent, "Información del Producto", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Panel para la información del producto
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel precioLabel = new JLabel("Precio:");
        JLabel stockLabel = new JLabel("Stock:");
        JLabel categoriaLabel = new JLabel("Categoría:");
        JLabel marcaLabel = new JLabel("Marca:");

        JLabel nombreValue = new JLabel();
        JLabel precioValue = new JLabel();
        JLabel stockValue = new JLabel();
        JLabel categoriaValue = new JLabel();
        JLabel marcaValue = new JLabel();

        try {
            List<Object> productoData = DBMediator.getProductoPorID(productoID);
            if (productoData != null) {
                nombreValue.setText((String) productoData.get(1));
                precioValue.setText("₡" + productoData.get(2));
                stockValue.setText(String.valueOf(productoData.get(3)));
                categoriaValue.setText((String) productoData.get(4));
                marcaValue.setText((String) productoData.get(5));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        infoPanel.add(nombreLabel);
        infoPanel.add(nombreValue);
        infoPanel.add(precioLabel);
        infoPanel.add(precioValue);
        infoPanel.add(stockLabel);
        infoPanel.add(stockValue);
        infoPanel.add(categoriaLabel);
        infoPanel.add(categoriaValue);
        infoPanel.add(marcaLabel);
        infoPanel.add(marcaValue);

        // Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");

        editButton.addActionListener(e -> {
            this.dispose();
            EditarProductoView editarProductoView = new EditarProductoView(productoID);
            editarProductoView.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este producto? Esta acción es irreversible.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.eliminarProducto(productoID);
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Agregar los paneles a la ventana
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

