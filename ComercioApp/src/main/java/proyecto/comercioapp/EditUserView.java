package proyecto.comercioapp;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.text.SimpleDateFormat;
import proyecto.db.DBMediator;
import java.util.Date;
import java.util.List; 


public class EditUserView extends JFrame {
    private int usuarioID;

    // Definir componentes
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JDateChooser fechaNacimientoChooser;
    private JButton saveButton;
    private SimpleDateFormat formatoFecha;

    public EditUserView(int usuarioID) {
        this.usuarioID = usuarioID;
        this.formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha
        initComponents();
        cargarDatosUsuario();
    }

    private void initComponents() {
        // Configuración básica de la ventana
        setTitle("Editar Datos Personales");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal con fondo azul
        JPanel editUserPanel = new JPanel();
        editUserPanel.setBackground(new java.awt.Color(153, 204, 255));
        editUserPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20); // Espaciado adicional alrededor de cada componente

        // Crear los componentes de entrada
        nombreField = new JTextField(20);
        apellidoField = new JTextField(20);
        emailField = new JTextField(20);
        telefonoField = new JTextField(20);
        fechaNacimientoChooser = new JDateChooser();
        fechaNacimientoChooser.setDateFormatString("yyyy-MM-dd"); // Formato de la fecha
        fechaNacimientoChooser.setMaxSelectableDate(new Date()); // Fecha máxima de hoy
        saveButton = new JButton("Guardar Cambios");

        // Crear etiquetas
        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel apellidoLabel = new JLabel("Apellido:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel telefonoLabel = new JLabel("Teléfono:");
        JLabel fechaNacimientoLabel = new JLabel("Fecha de Nacimiento:");

        // Añadir componentes al panel con disposición en rejilla
        gbc.gridx = 0;
        gbc.gridy = 0;
        editUserPanel.add(nombreLabel, gbc);
        gbc.gridx = 1;
        editUserPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editUserPanel.add(apellidoLabel, gbc);
        gbc.gridx = 1;
        editUserPanel.add(apellidoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editUserPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        editUserPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editUserPanel.add(telefonoLabel, gbc);
        gbc.gridx = 1;
        editUserPanel.add(telefonoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editUserPanel.add(fechaNacimientoLabel, gbc);
        gbc.gridx = 1;
        editUserPanel.add(fechaNacimientoChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        editUserPanel.add(saveButton, gbc);

        // Agregar el panel al marco
        add(editUserPanel);
        pack(); // Ajustar automáticamente el tamaño de la ventana

        // Configurar la acción del botón de guardar
        saveButton.addActionListener(e -> guardarCambios());
    }

    private void cargarDatosUsuario() {
        // Obtener los datos del usuario desde DBMediator
        List<Object> datosUsuario = DBMediator.getUsuarioData(usuarioID);

        if (datosUsuario != null) {
            // Asignar los valores a los campos de texto y al selector de fecha
            nombreField.setText((String) datosUsuario.get(0));
            apellidoField.setText((String) datosUsuario.get(1));
            emailField.setText((String) datosUsuario.get(2));
            telefonoField.setText((String) datosUsuario.get(3));

            // Convertir la fecha de String a Date y asignarla al JDateChooser
            try {
                Date fechaNacimiento = formatoFecha.parse((String) datosUsuario.get(4));
                fechaNacimientoChooser.setDate(fechaNacimiento);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la fecha de nacimiento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para manejar el evento de guardar cambios
    private void guardarCambios() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();
        Date fechaNacimiento = fechaNacimientoChooser.getDate(); // Obtener la fecha seleccionada

        // Validar que la fecha no sea nula
        if (fechaNacimiento == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una fecha de nacimiento válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convertir la fecha a String en formato yyyy-MM-dd
        String fechaNacimientoStr = formatoFecha.format(fechaNacimiento);

        // Llamar a DBMediator para actualizar el usuario en la base de datos
        int resultado = DBMediator.updateUsuario(usuarioID, nombre, apellido, email, telefono, fechaNacimientoStr);
        
        if (resultado > 0) { // Suponiendo que un resultado positivo indica éxito
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cerrar la ventana después de guardar
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
