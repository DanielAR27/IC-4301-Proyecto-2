package proyecto.comercioapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import proyecto.db.DBMediator;

public class ProductosSimilares extends javax.swing.JDialog {

    private int productoID;
    private int usuarioID;
    private Map<Integer, Integer> productosRelacionados;
    private HomeView parent;
 
    public ProductosSimilares(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    public ProductosSimilares(HomeView parent, boolean modal, int usuarioID, int productoID) {
        super(parent, modal);
        initComponents();        
        this.usuarioID = usuarioID;
        this.productoID = productoID;
        this.parent = parent;
        this.setTitle("Productos Relacionados | Producto #" + productoID);
        this.productosRelacionados = new HashMap<>();
        actualizarBotones();
    }    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        similaresPanel = new javax.swing.JPanel();
        similarButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        similarButton2 = new javax.swing.JButton();
        similarButton3 = new javax.swing.JButton();
        similarButton4 = new javax.swing.JButton();
        similarButton5 = new javax.swing.JButton();
        returnButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        similaresPanel.setBackground(new java.awt.Color(255, 255, 255));

        similarButton1.setText("Similar #1");

        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("La gente también ha comprado");

        similarButton2.setText("Similar #2");

        similarButton3.setText("Similar #3");

        similarButton4.setText("Similar #4");

        similarButton5.setText("Similar #5");

        returnButton.setText("Regresar");
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout similaresPanelLayout = new javax.swing.GroupLayout(similaresPanel);
        similaresPanel.setLayout(similaresPanelLayout);
        similaresPanelLayout.setHorizontalGroup(
            similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(similaresPanelLayout.createSequentialGroup()
                .addContainerGap(68, Short.MAX_VALUE)
                .addGroup(similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, similaresPanelLayout.createSequentialGroup()
                        .addGroup(similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(similaresPanelLayout.createSequentialGroup()
                                    .addComponent(similarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20))
                                .addGroup(similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(similarButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(similarButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(similarButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(similarButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, similaresPanelLayout.createSequentialGroup()
                        .addComponent(returnButton)
                        .addContainerGap())))
        );
        similaresPanelLayout.setVerticalGroup(
            similaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(similaresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(similarButton1)
                .addGap(18, 18, 18)
                .addComponent(similarButton2)
                .addGap(18, 18, 18)
                .addComponent(similarButton3)
                .addGap(18, 18, 18)
                .addComponent(similarButton4)
                .addGap(18, 18, 18)
                .addComponent(similarButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(returnButton)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(similaresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(similaresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        ProductoCarrito pc = new ProductoCarrito(parent, false, usuarioID, productoID);
        this.dispose();
        pc.setVisible(true);
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
            java.util.logging.Logger.getLogger(ProductosSimilares.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductosSimilares.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductosSimilares.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductosSimilares.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProductosSimilares dialog = new ProductosSimilares(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    private void actualizarBotones(){
        List<List<Object>> productosInfo = DBMediator.getSugerenciasPorProductoID(productoID);
        for(int i = 0; i < 5; i++){
            int buttonIndex = i;
            int productoID = (Integer) productosInfo.get(buttonIndex).get(0);
            String nombreProducto = (String) productosInfo.get(buttonIndex).get(1);
            int cuenta = (Integer) productosInfo.get(buttonIndex).get(2);             
            switch(buttonIndex){
                case 0 ->{
                    System.out.println(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton1.setText(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton1.addActionListener(e -> {consultarRelacionado(buttonIndex);});
                }case 1 ->{
                    similarButton2.setText(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton2.addActionListener(e -> {consultarRelacionado(buttonIndex);});
                }case 2 ->{
                    similarButton3.setText(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton3.addActionListener(e -> {consultarRelacionado(buttonIndex);});
                }case 3 ->{
                    similarButton4.setText(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton4.addActionListener(e -> {consultarRelacionado(buttonIndex);});
                }default ->{
                    similarButton5.setText(nombreProducto + " | Ocurrencias: " + cuenta);
                    similarButton5.addActionListener(e -> {consultarRelacionado(buttonIndex);});
                }
            }     
            productosRelacionados.put(buttonIndex, productoID);
        }
            
    }
    
    private void consultarRelacionado(int buttonIndex){
        int productoID = productosRelacionados.get(buttonIndex);
        ProductoCarrito pc = new ProductoCarrito(parent, false, usuarioID, productoID);
        this.dispose();
        pc.setVisible(true);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton returnButton;
    private javax.swing.JButton similarButton1;
    private javax.swing.JButton similarButton2;
    private javax.swing.JButton similarButton3;
    private javax.swing.JButton similarButton4;
    private javax.swing.JButton similarButton5;
    private javax.swing.JPanel similaresPanel;
    // End of variables declaration//GEN-END:variables
}