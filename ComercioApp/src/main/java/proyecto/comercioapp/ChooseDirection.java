package proyecto.comercioapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import proyecto.db.DBMediator;

public class ChooseDirection extends javax.swing.JFrame {
    
     private int usuarioID;
    
    public ChooseDirection() {
        initComponents();
    }

    public ChooseDirection(int usuarioID) {
        this.usuarioID = usuarioID;
        initComponents();
        cargarDirecciones();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DirectionPanel = new javax.swing.JPanel();
        direccionesLabel = new javax.swing.JLabel();
        DirectionMenuButton = new javax.swing.JButton();
        returnButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Escoger Dirección");
        setResizable(false);

        DirectionPanel.setBackground(new java.awt.Color(153, 204, 255));

        direccionesLabel.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        direccionesLabel.setForeground(new java.awt.Color(0, 0, 0));
        direccionesLabel.setText("Direcciones Disponibles");

        DirectionMenuButton.setText("Crear una Dirección");
        DirectionMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DirectionMenuButtonActionPerformed(evt);
            }
        });

        returnButton.setText("Regresar");
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DirectionPanelLayout = new javax.swing.GroupLayout(DirectionPanel);
        DirectionPanel.setLayout(DirectionPanelLayout);
        DirectionPanelLayout.setHorizontalGroup(
            DirectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DirectionPanelLayout.createSequentialGroup()
                .addContainerGap(207, Short.MAX_VALUE)
                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DirectionMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
            .addGroup(DirectionPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(direccionesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DirectionPanelLayout.setVerticalGroup(
            DirectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DirectionPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(direccionesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                .addGroup(DirectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DirectionMenuButton)
                    .addComponent(returnButton))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DirectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DirectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DirectionMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DirectionMenuButtonActionPerformed
        this.dispose();
        DirectionMenu direction = new DirectionMenu(usuarioID);
        direction.setVisible(true);
    }//GEN-LAST:event_DirectionMenuButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        this.dispose();
        CarritoView carrito = new CarritoView(usuarioID);
        carrito.setVisible(true);
    }//GEN-LAST:event_returnButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChooseDirection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChooseDirection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChooseDirection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChooseDirection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChooseDirection().setVisible(true);
            }
        });
    }

    private void cargarDirecciones() {
        List<List<Object>> direcciones = DBMediator.getDireccionesPorUsuario(usuarioID);    
        // Se eliminan todos los elementos del panel.
        DirectionPanel.removeAll();
        // Se agregan los labels principales nuevamente.
        DirectionPanel.add(direccionesLabel);
        DirectionPanel.add(returnButton);

        // Verificar si hay direcciones
        if (direcciones.isEmpty()) {
            DirectionPanel.add(DirectionMenuButton);
            JLabel noDireccionesLabel = new JLabel("No hay direcciones disponibles, necesita crear una.");
            noDireccionesLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
            noDireccionesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noDireccionesLabel.setBounds((DirectionPanel.getWidth() - 450) / 2, direccionesLabel.getHeight() + 70, 450, 60); // Ajustar el tamaño y posición
            DirectionPanel.add(noDireccionesLabel);
        } else {
            // Crear paneles para cada dirección
            int yOffset = direccionesLabel.getHeight() + 30; // Espaciado vertical justo debajo del título
            for (int i = 0; i < direcciones.size(); i++) {
                List<Object> direccion = direcciones.get(i);
                JPanel dirPanel = crearPanelDireccion(i + 1, direccion);

                // Centrar el panel dentro del DirectionPanel
                dirPanel.setBounds((DirectionPanel.getWidth() - 315) / 2, yOffset, 315, 156);
                DirectionPanel.add(dirPanel);
                yOffset += 166; // Actualizar el desplazamiento vertical
            }
        }

        // Refrescar el panel principal
        DirectionPanel.revalidate();
        DirectionPanel.repaint();
    }

    private JPanel crearPanelDireccion(int numDireccion, List<Object> direccion) {
        JPanel panel = new JPanel();
        Integer direccionID = (Integer) direccion.get(0);
        panel.setLayout(new BorderLayout());
        panel.setBackground(new java.awt.Color(200, 230, 255));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Título
        JLabel titulo = new JLabel("Dirección #" + numDireccion + ":");
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        // Información de la dirección
        String contenidoDireccion = String.format("%s - %s<br>%s<br>%s<br>%s<br>%s<br>%s",
                direccion.get(1), // Nombre del país
                direccion.get(2), // Estado/provincia
                direccion.get(3), // Dirección Línea 1
                direccion.get(4), // Dirección Línea 2
                direccion.get(5), // Ciudad
                direccion.get(6), // Código postal
                direccion.get(7)  // Contacto
        );
        JLabel contenidoLabel = new JLabel("<html>" + contenidoDireccion + "</html>");
        panel.add(contenidoLabel, BorderLayout.CENTER);

        // Botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton seleccionarButton = new JButton("Seleccionar");

        seleccionarButton.addActionListener(e -> {
                this.dispose();
                ChoosePayMethod chPayMethod = new ChoosePayMethod(usuarioID, direccionID);
                chPayMethod.setVisible(true);
            ;});

        botonesPanel.add(seleccionarButton);

        panel.add(botonesPanel, BorderLayout.SOUTH);

        return panel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DirectionMenuButton;
    private javax.swing.JPanel DirectionPanel;
    private javax.swing.JLabel direccionesLabel;
    private javax.swing.JButton returnButton;
    // End of variables declaration//GEN-END:variables
}
