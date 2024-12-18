package proyecto.comercioapp;

import java.util.List;
import javax.swing.JOptionPane;
import proyecto.db.DBMediator;
import proyecto.utils.CheckUtils;

public class AddProducto extends javax.swing.JDialog {

    private ProductosView parentPV;
    private ProductoCarrito parentPC;
    private Integer productoID;
    
    public AddProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public AddProducto(ProductosView parent, boolean modal, Integer productoID) {
        super(parent, modal);
        this.parentPV = parent;
        initComponents();
        this.productoID = productoID;
         if (productoID != null){
             this.setTitle("Editar Producto #" + productoID.toString());
             cargarDatosProducto();
        }else{
             this.setTitle("Crear Producto");
         }       
    }
   
    public AddProducto(ProductoCarrito parent, boolean modal, Integer productoID) {
        super(parent, modal);
        this.parentPC = parent;
        initComponents();
        this.productoID = productoID;
         if (productoID != null){
             this.setTitle("Editar Producto #" + productoID.toString());
             cargarDatosProducto();
        }else{
             this.setTitle("Crear Producto");
         }       
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productosPanel = new javax.swing.JPanel();
        productosLabel = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        productDescriptionLabel = new javax.swing.JLabel();
        productNameTextField = new javax.swing.JTextField();
        productDescriptionTextField = new javax.swing.JTextField();
        productPriceLabel = new javax.swing.JLabel();
        productPriceTextField = new javax.swing.JTextField();
        stockLabel = new javax.swing.JLabel();
        stockTextField = new javax.swing.JTextField();
        categoriaLabel = new javax.swing.JLabel();
        categoriasComboBox = new javax.swing.JComboBox<>();
        marcaLabel = new javax.swing.JLabel();
        marcasComboBox = new javax.swing.JComboBox<>();
        imagenLabel = new javax.swing.JLabel();
        imagenTextField = new javax.swing.JTextField();
        acceptButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        productosPanel.setBackground(new java.awt.Color(153, 204, 255));

        productosLabel.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        productosLabel.setForeground(new java.awt.Color(0, 0, 0));
        productosLabel.setText("Registrar Producto");

        productNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        productNameLabel.setText("Nombre del Producto:");

        productDescriptionLabel.setForeground(new java.awt.Color(0, 0, 0));
        productDescriptionLabel.setText("Descripción del Producto:");

        productPriceLabel.setForeground(new java.awt.Color(0, 0, 0));
        productPriceLabel.setText("Precio:");

        stockLabel.setForeground(new java.awt.Color(0, 0, 0));
        stockLabel.setText("Stock:");

        categoriaLabel.setForeground(new java.awt.Color(0, 0, 0));
        categoriaLabel.setText("Categoría: ");

        List<String> categorias = DBMediator.getCategorias();
        categoriasComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(categorias.toArray(new String[0])));
        categoriasComboBox.setMaximumRowCount(5);

        marcaLabel.setForeground(new java.awt.Color(0, 0, 0));
        marcaLabel.setText("Marca:");

        List<String> marcas= DBMediator.getMarcas();
        marcasComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(marcas.toArray(new String[0])));
        marcasComboBox.setMaximumRowCount(5);

        imagenLabel.setForeground(new java.awt.Color(0, 0, 0));
        imagenLabel.setText("Imagen (URL): ");

        acceptButton.setText("Aceptar");
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productosPanelLayout = new javax.swing.GroupLayout(productosPanel);
        productosPanel.setLayout(productosPanelLayout);
        productosPanelLayout.setHorizontalGroup(
            productosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productosPanelLayout.createSequentialGroup()
                .addGroup(productosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productosPanelLayout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(productosPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(productosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(productosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(imagenLabel)
                                .addComponent(productDescriptionLabel)
                                .addComponent(stockLabel)
                                .addComponent(categoriaLabel)
                                .addComponent(productPriceLabel)
                                .addComponent(marcaLabel)
                                .addComponent(imagenTextField)
                                .addComponent(marcasComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(categoriasComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(stockTextField)
                                .addComponent(productPriceTextField)
                                .addComponent(productDescriptionTextField)
                                .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(productosPanelLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(productNameLabel))
                            .addComponent(productosLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        productosPanelLayout.setVerticalGroup(
            productosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productosPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(productosLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(productNameLabel)
                .addGap(1, 1, 1)
                .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(productDescriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(productPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(stockLabel)
                .addGap(3, 3, 3)
                .addComponent(stockTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoriaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoriasComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(marcaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(marcasComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imagenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imagenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(acceptButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
            try{
            String nombre = productNameTextField.getText();
            String descripcion = productDescriptionTextField.getText();
            Float precio = Float.valueOf(productPriceTextField.getText());
            Integer stock = Integer.valueOf(stockTextField.getText());
            String categoria = (String) categoriasComboBox.getSelectedItem();
            String marca = (String) marcasComboBox.getSelectedItem();
            String imagen = imagenTextField.getText();
            
            // Verificar si el precio es válido.
            if(precio < 0){
              JOptionPane.showMessageDialog(this, "Verifique que el precio sea válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
              return;
            }

            // Verificar si el stock es válido.
            if(stock < 0){
              JOptionPane.showMessageDialog(this, "Verifique que el stock sea válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
              return;            
            }

            int res = DBMediator.upsertProducto(productoID, nombre, descripcion, precio, stock, categoria, marca, imagen);
            switch (res) {
                case 0 -> {
                    JOptionPane.showMessageDialog(this, "Se ha actualizado exitosamente el producto.", "Notificación", JOptionPane.INFORMATION_MESSAGE);
                    if (parentPV != null){
                        parentPV.actualizarLista(); // Llama al método para actualizar la 
                    } else if (parentPC != null){
                        parentPC.actualizarLabels(true);
                    }
                    this.dispose();
                }case 1->{
                    JOptionPane.showMessageDialog(this, "Se ha registrado exitosamente el producto.", "Notificación", JOptionPane.INFORMATION_MESSAGE);
                    if (parentPV != null){
                        parentPV.actualizarLista(); // Llama al método para actualizar la lista.
                    }else if (parentPC != null){
                        parentPC.actualizarLabels(true);                        
                    }
                    this.dispose();                 
                }case -1 -> {
                    JOptionPane.showMessageDialog(this, "Verifique no dejar espacios en blanco.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }case -2 -> {
                    JOptionPane.showMessageDialog(this, "La categoría asociada no se ha encontrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }case -3->{
                    JOptionPane.showMessageDialog(this, "La marca asociada no se ha encontrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;                
                }default ->{
                    JOptionPane.showMessageDialog(this, "No se ha podido concretar la acción, intente de nuevo porfavor.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;                        
                }
            }
        }catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(this, "Verifique el stock sea un entero y el precio un número cualquiera.", "Advertencia", JOptionPane.ERROR_MESSAGE);
                return;  
        }catch (Exception e){
                e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Se ha producido un error, intente de nuevo por favor.", "Advertencia", JOptionPane.ERROR_MESSAGE);
                return; 
        }    
    }//GEN-LAST:event_acceptButtonActionPerformed

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
            java.util.logging.Logger.getLogger(AddProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddProducto dialog = new AddProducto(new javax.swing.JFrame(), true);
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

    private void cargarDatosProducto(){
        List <Object> productoInfo = DBMediator.getProductoInfo(productoID);
        productNameTextField.setText((String) productoInfo.get(0));
        productDescriptionTextField.setText((String) productoInfo.get(1));
        productPriceTextField.setText(Float.toString((Float) productoInfo.get(2)));
        stockTextField.setText(Integer.toString(((Float) productoInfo.get(3)).intValue()));
        categoriasComboBox.setSelectedItem(productoInfo.get(4));
        marcasComboBox.setSelectedItem(productoInfo.get(5));
        imagenTextField.setText((String) productoInfo.get(7));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JLabel categoriaLabel;
    private javax.swing.JComboBox<String> categoriasComboBox;
    private javax.swing.JLabel imagenLabel;
    private javax.swing.JTextField imagenTextField;
    private javax.swing.JLabel marcaLabel;
    private javax.swing.JComboBox<String> marcasComboBox;
    private javax.swing.JLabel productDescriptionLabel;
    private javax.swing.JTextField productDescriptionTextField;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JLabel productPriceLabel;
    private javax.swing.JTextField productPriceTextField;
    private javax.swing.JLabel productosLabel;
    private javax.swing.JPanel productosPanel;
    private javax.swing.JLabel stockLabel;
    private javax.swing.JTextField stockTextField;
    // End of variables declaration//GEN-END:variables
}
