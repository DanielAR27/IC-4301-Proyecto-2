/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import proyecto.db.DBMediator;
import java.util.List;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import proyecto.db.DBMediator;

public class UserDataView extends JFrame {
    private int usuarioID;

    // Declarar los campos como variables de instancia para usarlos en otros métodos
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JTextField fechaNacimientoField;

    public UserDataView(int usuarioID) {
        this.usuarioID = usuarioID;
        initComponents();
        cargarDatosUsuario();
    }

    private void initComponents() {
        // Configuración básica de la ventana
        setTitle("Datos Personales");
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo azul
        JPanel userDataPanel = new JPanel();
        userDataPanel.setBackground(new java.awt.Color(153, 204, 255));
        userDataPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20); // Espaciado adicional alrededor de cada campo

        // Crear etiquetas y campos de texto (no editables)
        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel apellidoLabel = new JLabel("Apellido:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel telefonoLabel = new JLabel("Teléfono:");
        JLabel fechaNacimientoLabel = new JLabel("Fecha de Nacimiento:");

        nombreField = new JTextField(20);
        apellidoField = new JTextField(20);
        emailField = new JTextField(20);
        telefonoField = new JTextField(20);
        fechaNacimientoField = new JTextField(20);

        // Configurar campos como no editables
        nombreField.setEditable(false);
        apellidoField.setEditable(false);
        emailField.setEditable(false);
        telefonoField.setEditable(false);
        fechaNacimientoField.setEditable(false);

        // Ajustar tamaño de los campos de texto
        Dimension fieldSize = new Dimension(200, 25);
        nombreField.setPreferredSize(fieldSize);
        apellidoField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        telefonoField.setPreferredSize(fieldSize);
        fechaNacimientoField.setPreferredSize(fieldSize);

        // Añadir componentes al panel con disposición en rejilla
        gbc.gridx = 0;
        gbc.gridy = 0;
        userDataPanel.add(nombreLabel, gbc);
        gbc.gridx = 1;
        userDataPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        userDataPanel.add(apellidoLabel, gbc);
        gbc.gridx = 1;
        userDataPanel.add(apellidoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        userDataPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        userDataPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        userDataPanel.add(telefonoLabel, gbc);
        gbc.gridx = 1;
        userDataPanel.add(telefonoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        userDataPanel.add(fechaNacimientoLabel, gbc);
        gbc.gridx = 1;
        userDataPanel.add(fechaNacimientoField, gbc);

        
        add(userDataPanel);
        pack(); 
    }

    private void cargarDatosUsuario() {
        // Obtener los datos del usuario desde DBMediator
        List<Object> datosUsuario = DBMediator.getUsuarioData(usuarioID);

        if (datosUsuario != null) {
            // Asignar los valores a los campos de texto
            nombreField.setText((String) datosUsuario.get(0));
            apellidoField.setText((String) datosUsuario.get(1));
            emailField.setText((String) datosUsuario.get(2));
            telefonoField.setText((String) datosUsuario.get(3));
            fechaNacimientoField.setText((String) datosUsuario.get(4));
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
