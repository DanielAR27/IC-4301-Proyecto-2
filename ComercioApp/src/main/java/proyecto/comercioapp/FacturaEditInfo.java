package proyecto.comercioapp;

import java.awt.Color;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import proyecto.db.DBMediator;

public class FacturaEditInfo extends javax.swing.JFrame {

    private int usuarioID;
    private int pagina;
    private int facturaID;
    private String descuentoURL;
    private Map<Integer, JPanel> panelesProductos;
    private List<List<Object>> productosPagina;
    
    public FacturaEditInfo() {
        initComponents();
    }

    public FacturaEditInfo(int usuarioID, int facturaID){
        initComponents();
        this.setLayout(null);
        this.setTitle("Factura #"+ facturaID);
        outerPanel.setLayout((null));
        innerPanel.setLayout(null);
        productPicPanel.setLayout(null);
        this.panelesProductos = new TreeMap<>();
        this.facturaID = facturaID;
        this.usuarioID = usuarioID;
        this.descuentoURL = "https://i.ibb.co/4tMXLwq/descuento-img.png";
        this.pagina = 0;
        crearPanelesProductos();
        actualizarPanelesProductos(facturaID, pagina);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outerPanel = new javax.swing.JPanel();
        innerPanel = new javax.swing.JPanel();
        productPicPanel = new javax.swing.JPanel();
        descripcionLabel = new javax.swing.JLabel();
        regresarButton = new javax.swing.JButton();
        anteriorButton = new javax.swing.JButton();
        siguienteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        outerPanel.setBackground(new java.awt.Color(255, 255, 255));
        outerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        innerPanel.setBackground(new java.awt.Color(0, 153, 204));
        innerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        productPicPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        // Crear un borde negro de 1 píxel de grosor
        productPicPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        javax.swing.GroupLayout productPicPanelLayout = new javax.swing.GroupLayout(productPicPanel);
        productPicPanel.setLayout(productPicPanelLayout);
        productPicPanelLayout.setHorizontalGroup(
            productPicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        productPicPanelLayout.setVerticalGroup(
            productPicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        descripcionLabel.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        descripcionLabel.setText("DESCRIPCION PRODUCTO");

        regresarButton.setText("Regresar");
        regresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout innerPanelLayout = new javax.swing.GroupLayout(innerPanel);
        innerPanel.setLayout(innerPanelLayout);
        innerPanelLayout.setHorizontalGroup(
            innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(productPicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(innerPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descripcionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(144, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, innerPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(regresarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );
        innerPanelLayout.setVerticalGroup(
            innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, innerPanelLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(productPicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(innerPanelLayout.createSequentialGroup()
                        .addComponent(descripcionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(regresarButton)))
                .addGap(22, 22, 22))
        );

        anteriorButton.setText("Página Anterior");
        anteriorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anteriorButtonActionPerformed(evt);
            }
        });

        siguienteButton.setText("Siguiente Página");
        siguienteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siguienteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outerPanelLayout = new javax.swing.GroupLayout(outerPanel);
        outerPanel.setLayout(outerPanelLayout);
        outerPanelLayout.setHorizontalGroup(
            outerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(innerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(outerPanelLayout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(anteriorButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(siguienteButton)
                .addGap(190, 190, 190))
        );
        outerPanelLayout.setVerticalGroup(
            outerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outerPanelLayout.createSequentialGroup()
                .addGap(0, 354, Short.MAX_VALUE)
                .addGroup(outerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(anteriorButton)
                    .addComponent(siguienteButton))
                .addGap(18, 18, 18)
                .addComponent(innerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void regresarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarButtonActionPerformed
        FacturasEditView fev = new FacturasEditView(usuarioID);
        this.dispose();
        fev.setVisible(true);
    }//GEN-LAST:event_regresarButtonActionPerformed

    private void anteriorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anteriorButtonActionPerformed
        pagina -= 1;
        actualizarPanelesProductos(facturaID, pagina);
    }//GEN-LAST:event_anteriorButtonActionPerformed

    private void siguienteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteButtonActionPerformed
        pagina += 1;
        actualizarPanelesProductos(facturaID, pagina);
    }//GEN-LAST:event_siguienteButtonActionPerformed

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
            java.util.logging.Logger.getLogger(FacturaEditInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturaEditInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturaEditInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturaEditInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FacturaEditInfo().setVisible(true);
            }
        });
    }
    
    private void crearPanelesProductos() {
        int anchoPanel = 800; // Ancho del panel
        int altoPanel = 55; // Alto del panel ajustado para que quepa una sola línea de texto
        int espacioEntrePaneles = 10;

        // Obtener el ancho del outerPanel para centrar los paneles
        int outerPanelWidth = outerPanel.getWidth();

        // Calcular la posición x inicial para centrar el panel
        int xInicial = (outerPanelWidth - anchoPanel) / 2;
        int yInicial = 20; // Posición inicial y

        // Crear 5 paneles de productos
        for (int i = 0; i < 5; i++) {
            JPanel productoPanel = new JPanel();
            productoPanel.setLayout(null);
            productoPanel.setBounds(xInicial, yInicial, anchoPanel, altoPanel);
            productoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            productoPanel.setBackground(new Color(245, 245, 245)); // Fondo gris claro

            // Añadir etiqueta de nombre de producto como ejemplo
            JLabel nombreProductoLabel = new JLabel("Producto " + (i + 1));
            nombreProductoLabel.setBounds(10, 10, 400, 20); // Ajustar el tamaño y ancho
            productoPanel.add(nombreProductoLabel);

            JButton eliminarButton = new JButton("-");
            eliminarButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            eliminarButton.setBounds(anchoPanel - 50, 10, 40, 30);
            eliminarButton.setBackground(Color.RED);
            eliminarButton.setForeground(Color.WHITE);
            productoPanel.add(eliminarButton);
            
            // Agregar acción al botón de eliminar
            int panelIndex = i; // Capturar el índice para usarlo en el listener
            
            // Agregar un MouseAdapter para detectar clics en el panel
            productoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    panelSeleccionado(panelIndex);
                }
            });

            eliminarButton.addActionListener(e -> eliminarLineaFactura(panelIndex));
            
            // Agregar el panel al outerPanel
            outerPanel.add(productoPanel);
            panelesProductos.put(i, productoPanel);

            // Actualizar la posición y para el siguiente panel
            yInicial += altoPanel + espacioEntrePaneles;
        }

        // Refrescar el outerPanel para mostrar los paneles
        outerPanel.revalidate();
        outerPanel.repaint();
    }
    
    private void actualizarPanelesProductos(int facturaID, int numPagina) {
        int index = 0;
        productosPagina = DBMediator.getDetallesFacturaPorPagina(facturaID, numPagina);
        int stillDetalles = DBMediator.verificarDetallesFacturaPorPagina(facturaID, numPagina + 1);

        // Selecciona el primer panel por defecto.
        if (numPagina == 0 && productosPagina.isEmpty()) {
            // Crear un panel temporal con el mensaje de "No hay productos todavía en el carrito"
            JPanel panelSinProductos = new JPanel();
            panelSinProductos.setLayout(null);
            panelSinProductos.setBounds(panelesProductos.get(0).getBounds()); // Usar las coordenadas del primer panel
            panelSinProductos.setBackground(new Color(245, 245, 245));
            panelSinProductos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            JLabel mensajeLabel = new JLabel("No hay productos todavía en el carrito");
            mensajeLabel.setBounds(10, 10, 500, 20); // Ajustar el tamaño y la posición según sea necesario
            panelSinProductos.add(mensajeLabel);

            // Agregar el panel al outerPanel y hacerlo visible
            outerPanel.add(panelSinProductos);
            panelSinProductos.setVisible(true);

            // Ocultar la etiqueta de descripción.
            descripcionLabel.setVisible(false);
            // Ocultar el botón de continuar.
            siguienteButton.setVisible(false);
            // Limpiar el botón de la imagen de producto.
            productPicPanel.removeAll();
            productPicPanel.revalidate();
            productPicPanel.repaint();            
            // Ocultar todos los paneles de productos
            for (int i = 0; i < panelesProductos.size(); i++) {
                JPanel productoPanel = panelesProductos.get(i);
                if (productoPanel != null) {
                    productoPanel.setVisible(false);
                }
            }
        } else {
            // Se deja la vista para el primer producto.
            panelSeleccionado(0);            
            for (List<Object> lineaFactura : productosPagina) {
                JPanel productoPanel = panelesProductos.get(index);

                if (productoPanel != null) {
                    String nombreProducto = (String) lineaFactura.get(2);
                    int cantidad = (Integer) lineaFactura.get(4);
                    float precio = (Float) lineaFactura.get(5);
                    float descuento = (Float) lineaFactura.get(6);
                    float lineaTotal = (Float) lineaFactura.get(7);
                    // Calcular el porcentaje de descuento
                    float porcentajeDescuento = (precio > 0) ? (descuento / precio) * 100 : 0;

                    // Formatear el texto del producto con el porcentaje de descuento
                    String textoProducto = nombreProducto + "   " + cantidad + " x £" + String.format("%.2f", precio);
                    if (descuento > 0) {
                        textoProducto += " (" + String.format("%.0f", descuento) + "% de descuento)";
                    }
                    textoProducto += " = £" + String.format("%.2f", lineaTotal);

                    JLabel productoLabel = (JLabel) productoPanel.getComponent(0);
                    productoLabel.setText(textoProducto);

                    productoPanel.setVisible(true);
                    index++;
                }
            }


            // Ocultar paneles sin datos
            for (int i = index; i < panelesProductos.size(); i++) {
                JPanel productoPanel = panelesProductos.get(i);
                if (productoPanel != null) {
                    productoPanel.setVisible(false);
                }
            }
        }

        anteriorButton.setVisible(pagina > 0);
        siguienteButton.setVisible(stillDetalles == 1);
    }

    
    private void panelSeleccionado(int panelIndex) {
        if (panelIndex < productosPagina.size()) {
            List<Object> producto = productosPagina.get(panelIndex);

            // Resaltar el panel seleccionado
            for (Map.Entry<Integer, JPanel> entry : panelesProductos.entrySet()) {
                JPanel panel = entry.getValue();
                if (entry.getKey() == panelIndex) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Borde azul para el panel seleccionado
                } else {
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Borde negro para los no seleccionados
                }
            }

            // Actualizar la imagen en productPicPanel
            try {
                String urlImagen = (String) producto.get(8);
                ImageIcon iconoProducto = new ImageIcon(new URL(urlImagen));

                JLabel imagenLabel = new JLabel();
                imagenLabel.setIcon(iconoProducto);
                imagenLabel.setBounds(28, 30, iconoProducto.getIconWidth(), iconoProducto.getIconHeight());

                productPicPanel.removeAll();
                productPicPanel.setLayout(null);
                productPicPanel.add(imagenLabel);

                // Verificar si hay descuento y mostrar la imagen de descuento
                float descuento = (Float) producto.get(6);
                if (descuento > 0) {
                    try {
                        ImageIcon iconoDescuento = new ImageIcon(new URL(descuentoURL));
                        JLabel descuentoLabel = new JLabel(iconoDescuento);
                        descuentoLabel.setBounds(productPicPanel.getWidth() - iconoDescuento.getIconWidth(), 5,
                                iconoDescuento.getIconWidth(), iconoDescuento.getIconHeight());
                        productPicPanel.add(descuentoLabel);
                    } catch (Exception e) {
                        System.out.println("Error al cargar la imagen de descuento: " + e.getMessage());
                    }
                }

                productPicPanel.revalidate();
                productPicPanel.repaint();
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen del producto: " + e.getMessage());
            }

            // Actualizar la descripción
            String descripcion = "<html><b>Descripción:</b> " + (String) producto.get(3) + "</html>";
            descripcionLabel.setText(descripcion);
        }
    }
    
    private void eliminarLineaFactura(int panelIndex){
        try{
            // Validar que el índice sea válido
            if (panelIndex < productosPagina.size()) {
                // Obtener los datos de la factura desde facturasPagina
                List<Object> lineaFactura = productosPagina.get(panelIndex);
                int lineaFacturaID = (Integer) lineaFactura.get(0); // Obtener el ID de la línea de la factura
                String productoLinea = (String) lineaFactura.get(2); // Obtener el nombre del producto de la línea
                
                int respuesta = JOptionPane.showConfirmDialog(
                   this,
                   "¿Está seguro de eliminar la linea para el producto \"" + productoLinea + "\"?\nEsta acción es irreversible.",
                   "Confirmar eliminación",
                   JOptionPane.YES_NO_OPTION,
                   JOptionPane.WARNING_MESSAGE
               );
                
                if(respuesta == JOptionPane.YES_OPTION){
                    int resultado = DBMediator.deleteLineaFactura(lineaFacturaID);
                    switch (resultado) {
                        case 0 ->{
                            JOptionPane.showMessageDialog(this, "Linea de la factura eliminada con éxito.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
                            this.pagina = 0;
                            actualizarPanelesProductos(facturaID, pagina);
                        }default ->{
                            JOptionPane.showMessageDialog(this, "Error al eliminar el línea de la factura.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }                    
                }
                
            } else {
                throw new IndexOutOfBoundsException("Índice fuera de rango.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ha ocurrido un error al intentar editar la factura, intente de nuevo más tarde.",
                    "Advertencia",
                    JOptionPane.ERROR_MESSAGE);
        }     
    }

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anteriorButton;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JPanel innerPanel;
    private javax.swing.JPanel outerPanel;
    private javax.swing.JPanel productPicPanel;
    private javax.swing.JButton regresarButton;
    private javax.swing.JButton siguienteButton;
    // End of variables declaration//GEN-END:variables
}
