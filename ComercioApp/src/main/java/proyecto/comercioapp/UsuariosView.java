package proyecto.comercioapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import java.util.HashMap;
import java.util.Date;
import proyecto.db.DBMediator;

public class UsuariosView extends javax.swing.JFrame {
    private JList<String> usuarioList;
    private DefaultListModel<String> listModel;
    private JButton agregarButton, editarButton, eliminarButton, btnVolver;
    private Map<Integer, Integer> usuarioIDs = new HashMap<>();

    public UsuariosView() {
        setTitle("Gestionar Usuarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de lista para mostrar usuarios
        listModel = new DefaultListModel<>();
        usuarioList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(usuarioList);

        // Cargar usuarios al inicio
        cargarUsuarios();

        // Botones
        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(agregarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(btnVolver);

        // Agregar componentes al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para botones
        agregarButton.addActionListener(this::mostrarAgregarUsuarioDialog);
        editarButton.addActionListener(this::editarUsuario);
        eliminarButton.addActionListener(this::eliminarUsuario);
        btnVolver.addActionListener(e -> this.dispose());
    }

    private void cargarUsuarios() {
        List<List<Object>> usuarios = DBMediator.getUsuariosConID(); // Método que devuelve IDs y atributos
        listModel.clear();
        usuarioIDs.clear();
        if (usuarios != null) {
            int index = 0;
            for (List<Object> usuario : usuarios) {
                // Validar que se devuelvan exactamente 5 columnas
                if (usuario.size() < 5) {
                    JOptionPane.showMessageDialog(this, "Error al cargar usuarios: datos incompletos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int id = (int) usuario.get(0);
                    String nombre = (String) usuario.get(1);
                    String apellido = (String) usuario.get(2);
                    String email = (String) usuario.get(3);
                    String rol = (String) usuario.get(4);

                    listModel.addElement(nombre + " " + apellido + " - " + email + " (Rol: " + rol + ")");
                    usuarioIDs.put(index++, id);
                } catch (ClassCastException ex) {
                    JOptionPane.showMessageDialog(this, "Error al procesar los datos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void mostrarAgregarUsuarioDialog(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Agregar Usuario", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        // Campos para ingresar datos del usuario
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField rolField = new JTextField();
        JDateChooser fechaNacimientoChooser = new JDateChooser();

        dialog.add(new JLabel("Nombre:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Apellido:"));
        dialog.add(apellidoField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Teléfono:"));
        dialog.add(telefonoField);
        dialog.add(new JLabel("Rol:"));
        dialog.add(rolField);
        dialog.add(new JLabel("Fecha de Nacimiento:"));
        dialog.add(fechaNacimientoChooser);

        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");
        dialog.add(guardarButton);
        dialog.add(cancelarButton);

        guardarButton.addActionListener(ae -> {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String rol = rolField.getText().trim();
            String fechaNacimiento = null;

            if (fechaNacimientoChooser.getDate() != null) {
                fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").format(fechaNacimientoChooser.getDate());
            }

            int resultado = DBMediator.createUsuario(nombre, apellido, email, password, telefono, fechaNacimiento, rol);
            if (resultado == 0) {
                JOptionPane.showMessageDialog(dialog, "Usuario agregado con éxito.");
                cargarUsuarios();
                dialog.dispose();
            } else if (resultado == -1) {
                JOptionPane.showMessageDialog(dialog, "Faltan campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (resultado == -2) {
                JOptionPane.showMessageDialog(dialog, "El correo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al agregar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarButton.addActionListener(ae -> dialog.dispose());

        dialog.setVisible(true);
    }

    private int getUsuarioID(int index) {
        return usuarioIDs.getOrDefault(index, -1); // Devuelve -1 si no encuentra el ID
    }

    private void editarUsuario(ActionEvent e) {
        int selectedIndex = usuarioList.getSelectedIndex(); // Índice seleccionado
        if (selectedIndex != -1) {
            int usuarioID = getUsuarioID(selectedIndex); // Obtener ID del usuario seleccionado

            JPanel editPanel = new JPanel(new GridLayout(7, 2, 10, 10));

            // Campos para editar
            JTextField nombreField = new JTextField();
            JTextField apellidoField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JTextField telefonoField = new JTextField();
            JDateChooser fechaNacimientoChooser = new JDateChooser();
            JComboBox<String> rolComboBox = new JComboBox<>(new String[] { "Cliente", "Administrador" });

            fechaNacimientoChooser.setDateFormatString("yyyy-MM-dd");

            try {
                List<Object> usuarioData = DBMediator.getUsuariosPorID(usuarioID);
                if (usuarioData != null) {
                    nombreField.setText((String) usuarioData.get(1));
                    apellidoField.setText((String) usuarioData.get(2));
                    emailField.setText((String) usuarioData.get(3));
                    telefonoField.setText((String) usuarioData.get(4));
                    if (usuarioData.get(5) != null) {
                        fechaNacimientoChooser.setDate((Date) usuarioData.get(5));
                    }
                    rolComboBox.setSelectedItem((String) usuarioData.get(6));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            editPanel.add(new JLabel("Nombre:"));
            editPanel.add(nombreField);
            editPanel.add(new JLabel("Apellido:"));
            editPanel.add(apellidoField);
            editPanel.add(new JLabel("Email:"));
            editPanel.add(emailField);
            editPanel.add(new JLabel("Contraseña:"));
            editPanel.add(passwordField);
            editPanel.add(new JLabel("Teléfono:"));
            editPanel.add(telefonoField);
            editPanel.add(new JLabel("Fecha Nacimiento:"));
            editPanel.add(fechaNacimientoChooser);
            editPanel.add(new JLabel("Rol:"));
            editPanel.add(rolComboBox);

            int result = JOptionPane.showConfirmDialog(this, editPanel, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String nombre = nombreField.getText().trim();
                String apellido = apellidoField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String telefono = telefonoField.getText().trim();
                java.sql.Date fechaNacimiento = new java.sql.Date(fechaNacimientoChooser.getDate().getTime());
                String rol = (String) rolComboBox.getSelectedItem();

                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || fechaNacimiento == null) {
                    JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben estar completos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimientoStr = (fechaNacimiento != null) ? dateFormat.format(fechaNacimiento) : null;

                int resultado = DBMediator.updateUsuario(usuarioID, nombre, apellido, email, password, telefono, rol, fechaNacimientoStr);

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


    
    private void eliminarUsuario(ActionEvent e) {
        int selectedIndex = usuarioList.getSelectedIndex(); // Índice seleccionado
        if (selectedIndex != -1) {
            int usuarioID = usuarioIDs.get(selectedIndex); // ID del usuario seleccionado
            String usuarioInfo = listModel.get(selectedIndex);

            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este usuario?\n" + usuarioInfo,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = DBMediator.deleteUsuario(usuarioID);
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UsuariosView().setVisible(true));
    }
}
