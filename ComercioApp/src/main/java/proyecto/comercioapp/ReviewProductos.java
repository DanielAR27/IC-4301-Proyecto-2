package proyecto.comercioapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import proyecto.db.DBMediator;

public class ReviewProductos extends javax.swing.JFrame {

    private int usuarioID;
    private int pagina;
    private Map<Integer, JButton> botonesProductos;
    private List<List<Object>> productosPagina;

    public ReviewProductos() {
        initComponents();
    }

    public ReviewProductos(int usuarioID) {
        initComponents();
        actualizarLabels();
        this.usuarioID = usuarioID;
        this.pagina = 0;
        this.productosPagina = new ArrayList<>();
        this.botonesProductos = new TreeMap<>();
        crearBotonesProductos();
        actualizarBotones(pagina);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        anteriorButton = new javax.swing.JButton();
        siguienteButton = new javax.swing.JButton();
        homeIcon = new javax.swing.JButton();
        reviewsViewIcon = new javax.swing.JButton();
        reviewLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú de Reviews");
        setResizable(false);

        anteriorButton.setText("Página Anterior");
        anteriorButton.setPreferredSize(new java.awt.Dimension(120, 25));
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

        try {
            URL urlHome = new URL("https://i.ibb.co/LvrDQZx/home-img.png");
            ImageIcon home = new ImageIcon(urlHome);
            homeIcon.setIcon(home);

            // Agregar texto debajo del ícono y usar HTML para formatearlo en negrita
            homeIcon.setText("<html><center><b>Regresar</b></center></html>");

            // Configurar la posición del texto y el ícono
            homeIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            homeIcon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

            // Hacer el fondo transparente y quitar el borde
            homeIcon.setContentAreaFilled(false); // Hace que el fondo sea transparente
            homeIcon.setBorderPainted(false); // Quita el borde
            homeIcon.setFocusPainted(false); // Quita el borde de enfoque al hacer clic
        }catch(Exception e){
            homeIcon.setText("No se ha encontrado la imagen.");
        }
        homeIcon.setPreferredSize(new java.awt.Dimension(64, 64));
        // Code adding the component to the parent container - not shown here
        homeIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeIconActionPerformed(evt);
            }
        });

        try {
            URL urlReviewView = new URL("https://i.ibb.co/dMmjWK2/ver-reviews.png");
            ImageIcon reviewView = new ImageIcon(urlReviewView);
            reviewsViewIcon.setIcon(reviewView);

            // Agregar texto debajo del ícono y usar HTML para formatearlo en negrita
            reviewsViewIcon.setText("<html><center><b>Ver Reviews</b></center></html>");

            // Configurar la posición del texto y el ícono
            reviewsViewIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            reviewsViewIcon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

            // Hacer el fondo transparente y quitar el borde
            reviewsViewIcon.setContentAreaFilled(false); // Hace que el fondo sea transparente
            reviewsViewIcon.setBorderPainted(false); // Quita el borde
            reviewsViewIcon.setFocusPainted(false); // Quita el borde de enfoque al hacer clic
        }catch(Exception e){
            reviewsViewIcon.setText("No se ha encontrado la imagen.");
        }
        reviewsViewIcon.setPreferredSize(new java.awt.Dimension(64, 64));
        // Code adding the component to the parent container - not shown here
        reviewsViewIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewsViewIconActionPerformed(evt);
            }
        });

        reviewLabel.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        reviewLabel.setText("Menú de Reviews");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(310, 310, 310)
                .addComponent(anteriorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(siguienteButton)
                .addContainerGap(386, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(reviewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(reviewsViewIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(homeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(homeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reviewsViewIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(reviewLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(anteriorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(siguienteButton))
                .addGap(95, 95, 95))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void anteriorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anteriorButtonActionPerformed
        pagina -= 1;
        actualizarBotones(pagina);
    }//GEN-LAST:event_anteriorButtonActionPerformed

    private void siguienteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteButtonActionPerformed
        pagina += 1;
        actualizarBotones(pagina);
    }//GEN-LAST:event_siguienteButtonActionPerformed

    private void homeIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeIconActionPerformed
        this.dispose();
        HomeView home = new HomeView(usuarioID);
        home.setVisible(true);
    }//GEN-LAST:event_homeIconActionPerformed

    private void reviewsViewIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewsViewIconActionPerformed
        this.dispose();
        ReviewsView rv = new ReviewsView(usuarioID);
        rv.setVisible(true);
    }//GEN-LAST:event_reviewsViewIconActionPerformed

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
            java.util.logging.Logger.getLogger(ReviewProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReviewProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReviewProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReviewProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReviewProductos().setVisible(true);
            }
        });
    }

    private void crearBotonesProductos() {
        int xInicial = 80; // Posición inicial x
        int yInicial = 130; // Posición inicial y
        int anchoBoton = 150;
        int altoBoton = 150;
        int espacioEntreBotones = 10;

        // Crear 10 botones
        for (int  i = 0; i < 10; i++) {
            int botonPosition = i; // Variable temporal para capturar el valor de i
            JButton boton = new JButton("Botón " + (i + 1));
            boton.setBounds(xInicial, yInicial, anchoBoton, altoBoton);
            boton.setVisible(true); // Todos los botones visibles por defecto
            boton.addActionListener(e -> {showProductoInfo(botonPosition);});
            // Agregar el botón al mapa
            botonesProductos.put(i, boton);
            this.add(boton);
            
            // Actualizar la posición x para el siguiente botón
            xInicial += anchoBoton + espacioEntreBotones;

            // Si llegamos al quinto botón, bajar a la siguiente fila y resetear x
            if ((i + 1) % 5 == 0) {
                xInicial = 80; // Volver a la posición inicial en x
                yInicial += altoBoton + espacioEntreBotones; // Bajar en y
            }
            
        }   
    }
  
    private void actualizarBotones(int numPagina) {
        int index = 0;
        productosPagina = DBMediator.getProductosCompradosPorUsuario(usuarioID, pagina);
        int stillProducts = DBMediator.verificarProductosCompradosPorUsuario(usuarioID, pagina + 1);

        // Limpiar los íconos de descuento existentes de los botones
        for (JButton boton : botonesProductos.values()) {
            // Remover todos los componentes dentro del botón
            boton.removeAll();
            boton.revalidate();
            boton.repaint();
        }
        
        for (List<Object> producto : productosPagina) {
          JButton boton = botonesProductos.get(index);
          if (boton != null) {
              try {
                  // Cargar la imagen de forma asíncrona
                  cargarImagenEnBoton(boton, (String) producto.get(3));

                  // Configurar el texto con el nombre y el precio en negrita
                  String nombreProducto = (String) producto.get(1);
                  float precio = (Float) producto.get(2);
                  String texto = "<html><center>" + nombreProducto + "<br><b>£" + precio + "</b></center></html>";
                  boton.setText(texto);

                  // Configurar la posición del texto y la imagen
                  boton.setHorizontalTextPosition(SwingConstants.CENTER);
                  boton.setVerticalTextPosition(SwingConstants.BOTTOM);                
                  boton.setVisible(true);

              } catch (Exception e) {
                  System.out.println("Error al cargar la imagen: " + e.getMessage());
                  boton.setText("Error en imagen");
              }
              index++;
          }
      }
        // Ocultar los botones que no tienen datos
        for (int i = index; i < 10; i++) {
            JButton boton = botonesProductos.get(i);
            if (boton != null) {
                boton.setVisible(false);
            }
        }

        // Mostrar/ocultar botón "Anterior"
        anteriorButton.setVisible(pagina > 0);

        // Mostrar/ocultar botón "Siguiente" si hay menos de 10 productos
        siguienteButton.setVisible(stillProducts == 1);
    } 
  
    private void showProductoInfo(int botonPosition){
        int productoID = (Integer) productosPagina.get(botonPosition).get(0); //Obtener el ID del producto del cual se toca el botón.
        ProductoReview pr = new ProductoReview(this, true, usuarioID, productoID);
        pr.setVisible(true);
    }
    
    private void cargarImagenEnBoton(JButton boton, String urlImagen) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    // Cargar la imagen en un hilo en segundo plano
                    URL url = new URL(urlImagen);
                    return new ImageIcon(url);
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icono = get();
                    if (icono != null) {
                        boton.setIcon(icono);
                        boton.revalidate();
                        boton.repaint();
                    } else {
                        boton.setText("Imagen no disponible");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    boton.setText("Error en imagen");
                }
            }
        };

        // Ejecutar el worker
        worker.execute();
    }
    
    private void actualizarLabels(){
        this.setLayout(null);
        reviewsViewIcon.setBounds(homeIcon.getX() - 100, homeIcon.getY(),
                reviewsViewIcon.getWidth(), reviewsViewIcon.getHeight()); // Ajusta el tamaño según sea necesario
        this.revalidate();
        this.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anteriorButton;
    private javax.swing.JButton homeIcon;
    private javax.swing.JLabel reviewLabel;
    private javax.swing.JButton reviewsViewIcon;
    private javax.swing.JButton siguienteButton;
    // End of variables declaration//GEN-END:variables
}
