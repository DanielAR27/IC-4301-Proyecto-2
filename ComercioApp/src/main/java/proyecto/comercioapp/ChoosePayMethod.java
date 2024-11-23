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
import proyecto.utils.CheckUtils;

public class ChoosePayMethod extends javax.swing.JFrame {
    
    private int usuarioID;
    private int direccionID;
    
    public ChoosePayMethod() {
        initComponents();
    }

    public ChoosePayMethod(int usuarioID, int direccionID) {
        this.usuarioID = usuarioID;
        this.direccionID = direccionID;
        initComponents();
        cargarMetodosPago();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PayPanel = new javax.swing.JPanel();
        metodosDePagoLabel = new javax.swing.JLabel();
        PayMethodMenuButton = new javax.swing.JButton();
        returnButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Escoger Método de Pago");
        setResizable(false);

        PayPanel.setBackground(new java.awt.Color(153, 204, 255));

        metodosDePagoLabel.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        metodosDePagoLabel.setForeground(new java.awt.Color(0, 0, 0));
        metodosDePagoLabel.setText("Métodos de Pago disponibles");

        PayMethodMenuButton.setText("Crear un Método de Pago");
        PayMethodMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayMethodMenuButtonActionPerformed(evt);
            }
        });

        returnButton.setText("Regresar");
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PayPanelLayout = new javax.swing.GroupLayout(PayPanel);
        PayPanel.setLayout(PayPanelLayout);
        PayPanelLayout.setHorizontalGroup(
            PayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayPanelLayout.createSequentialGroup()
                .addContainerGap(207, Short.MAX_VALUE)
                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PayMethodMenuButton)
                .addGap(171, 171, 171))
            .addGroup(PayPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(metodosDePagoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PayPanelLayout.setVerticalGroup(
            PayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(metodosDePagoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                .addGroup(PayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PayMethodMenuButton)
                    .addComponent(returnButton))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PayMethodMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayMethodMenuButtonActionPerformed
        this.dispose();
        PayMethodMenu pm = new PayMethodMenu(usuarioID);
        pm.setVisible(true);
    }//GEN-LAST:event_PayMethodMenuButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        this.dispose();
        ChooseDirection chDirection = new ChooseDirection(usuarioID);
        chDirection.setVisible(true);
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
            java.util.logging.Logger.getLogger(ChoosePayMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChoosePayMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChoosePayMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChoosePayMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChoosePayMethod().setVisible(true);
            }
        });
    }

    private void cargarMetodosPago() {
        List<List<Object>> metodosPago = DBMediator.getMetodosPagoPorUsuario(usuarioID);
        // Se eliminan todos los elementos del panel.
        PayPanel.removeAll();
        // Se agregan los labels principales nuevamente.
        PayPanel.add(metodosDePagoLabel);
        PayPanel.add(returnButton);

        // Verificar si hay métodos de pago.
        if (metodosPago.isEmpty()) {
            PayPanel.add(PayMethodMenuButton);
            JLabel noMetodosLabel = new JLabel("No hay métodos de pago disponibles, necesita agregar uno.");
            noMetodosLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
            noMetodosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noMetodosLabel.setBounds((PayPanel.getWidth() - 450) / 2, metodosDePagoLabel.getHeight() + 70, 450, 60);
            PayPanel.add(noMetodosLabel);
        } else {
            // Posicionar y agregar los métodos de pago al panel
            int yOffset = metodosDePagoLabel.getHeight() + 30; // Espaciado vertical justo debajo del título
            for (int i = 0; i < metodosPago.size(); i++) {
                List<Object> metodoPago = metodosPago.get(i);
                JPanel pmPanel = crearPanelMetodoPago(i + 1, metodoPago);

                // Centrar el panel dentro del DirectionPanel
                pmPanel.setBounds((PayPanel.getWidth() - 315) / 2, yOffset, 315, 156);
                PayPanel.add(pmPanel);
                yOffset += 166; // Actualizar el desplazamiento vertical
            }
        }

        // Refrescar el panel principal
        PayPanel.revalidate();
        PayPanel.repaint();
    }
   
 
    private JPanel crearPanelMetodoPago(int numMetodoPago, List<Object> metodoPago) {
        JPanel panel = new JPanel();
        Integer metodoPagoID = (Integer) metodoPago.get(0);
        panel.setLayout(new BorderLayout());
        panel.setBackground(new java.awt.Color(200, 230, 255));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Título
        JLabel titulo = new JLabel("Método de Pago #" + numMetodoPago + ":");
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        // Obtener el número de tarjeta desencriptado pero ocultar los primeros números excepto los últimos 4.
        String maskedCardNumber = CheckUtils.maskCardNumber(
                CheckUtils.decrpytCardNumber((String) metodoPago.get(1), (String) metodoPago.get(2)));

        // Información del método de pago
        String contenidoMetodoPago = String.format(
                "<b>Tarjeta:</b> %s<br><b>Titular:</b> %s<br><b>Expiración:</b> %s<br><b>CVV:</b> %s",
                maskedCardNumber,
                metodoPago.get(3), // Nombre del Titular
                metodoPago.get(4), // Fecha de Expiración
                metodoPago.get(5)  // CVV
        );

        JLabel contenidoLabel = new JLabel("<html>" + contenidoMetodoPago + "</html>");
        panel.add(contenidoLabel, BorderLayout.CENTER);

        // Botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton seleccionarButton = new JButton("Seleccionar");

        seleccionarButton.addActionListener(e -> {
            this.dispose();
            EndShop endShop = new EndShop(usuarioID, direccionID, metodoPagoID);
            endShop.setVisible(true);
        });

        botonesPanel.add(seleccionarButton);

        panel.add(botonesPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PayMethodMenuButton;
    private javax.swing.JPanel PayPanel;
    private javax.swing.JLabel metodosDePagoLabel;
    private javax.swing.JButton returnButton;
    // End of variables declaration//GEN-END:variables
}
