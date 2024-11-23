package proyecto.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import static java.sql.Date.valueOf;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class DBMediator {
    public static int insertUsuario(String nombre, String apellido, String email, String password,
            String telefono, String fechaNacimiento){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  CrearUsuario ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            statement.setString(1, nombre); 
            statement.setString(2, apellido);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, telefono);
            statement.setDate(6, valueOf(fechaNacimiento));
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(7, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(7);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }            
    }
        
    public static String getPassword(String email){
        try{        
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  ObtenerPasswordPorEmail ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            statement.setString(1, email);
             // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.VARCHAR);           

            // Ejecutar la llamada al procedimiento
            statement.execute();
            
             // Recuperar el valor del parámetro de salida
            String resultado = statement.getString(2);
            
            //Cerrar la conexión.
            connection.close();
            
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return null;
        }   
    }
    
    public static Integer getUserID(String email){
        try{        
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetUsuarioIDPorEmail ?, ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
            // Establecer los valores de los parámetros.
            statement.setString(1, email);
             // Preparar para registrar el valor de salida.
            statement.registerOutParameter(2, Types.INTEGER);           

            // Ejecutar la llamada al procedimiento.
            statement.execute();
            
             // Recuperar el valor del parámetro de salida.
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
            
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return null;
        }   
    }
    
    public static String getRolPorUsuarioID(int usuarioID){
        try{        
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetRolPorUsuarioID ?, ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
            // Establecer los valores de los parámetros.
            statement.setInt(1, usuarioID);
             // Preparar para registrar el valor de salida.
            statement.registerOutParameter(2, Types.NVARCHAR);        
            
            // Ejecutar la llamada al procedimiento.
            statement.execute();
            
             // Recuperar el valor del parámetro de salida.
            String resultado = statement.getString(2);
            //Cerrar la conexión.
            connection.close();
            
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }           
    }
    
    public static float getTotalCarrito(int usuarioID){
        try{        
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetTotalCarrito ?, ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
            // Establecer los valores de los parámetros.
            statement.setInt(1, usuarioID);
             // Preparar para registrar el valor de salida.
            statement.registerOutParameter(2, Types.FLOAT);           

            // Ejecutar la llamada al procedimiento.
            statement.execute();
            
             // Recuperar el valor del parámetro de salida.
            float resultado = statement.getFloat(2);
            
            //Cerrar la conexión.
            connection.close();
            
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }   
    }
    
    public static float getEnvioPorDireccionID(int direccionID){
        try{        
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetEnvioPorDireccionID ?, ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
            // Establecer los valores de los parámetros.
            statement.setInt(1, direccionID);
             // Preparar para registrar el valor de salida.
            statement.registerOutParameter(2, Types.FLOAT);           

            // Ejecutar la llamada al procedimiento.
            statement.execute();
            
             // Recuperar el valor del parámetro de salida.
            float resultado = statement.getFloat(2);
            
            //Cerrar la conexión.
            connection.close();
            
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return -1;
        }   
    }
    
    public static List<Object> getProductoInfo(int productoID){
        List<Object> productoInfo = new ArrayList<>();
        try{
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductoInfo ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
             // Establecer los valores de los parámetros
             statement.setInt(1, productoID);
             
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
             
             if (!resultSet.next()){
                 connection.close();
                 return null;
             }
             
             productoInfo.add(resultSet.getString(1)); // Nombre
             productoInfo.add(resultSet.getString(2)); // Descripción.
             productoInfo.add(resultSet.getFloat(3)); // Precio.
             productoInfo.add(resultSet.getFloat(4)); // Stock.
             productoInfo.add(resultSet.getString(5)); // Categoria.
             productoInfo.add(resultSet.getString(6)); // Marca.
             productoInfo.add(resultSet.getFloat(7)); // Calificacion promedio.
             productoInfo.add(resultSet.getString(8)); // Imagen del producto.
             productoInfo.add(resultSet.getInt(9)); // Cantidad de reviews.
             productoInfo.add(resultSet.getFloat(10)); // Descuento obtenido (si hay).
             
             connection.close();
             
             // Recuperar el valor del parámetro de salida.
        }catch(SQLException ex){
            return null;
        }
        
        return productoInfo;
    }
    
     public static List<String> getPaises() {
        List<String> paises = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetPaises";
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();

            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                paises.add(resultSet.getString(1));
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return paises; // Retornar la lista de nombres de países
    }
     
    public static List<String> getMarcas(){
        List<String> marcas = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetMarcas";
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();

            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                marcas.add(resultSet.getString(1));
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return marcas; // Retornar la lista de nombres de países        
    }
    
    public static List<String> getCategorias(){
        List<String> categorias = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetCategorias";
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();

            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                categorias.add(resultSet.getString(1));
            }
            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return categorias; // Retornar la lista de nombres de países        
    }
     
    public static List<String> getEstadosPorPais(String nombrePais) {
        List<String> estados = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetEstadosPorPais ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, nombrePais);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
            
            // Si viene vacío (con null como fila, entonces retorne null)
            if (!resultSet.next()) {
                
                connection.close();
                return null; // Retornar null si no hay resultados
            }
            
            // Si hay resultados, agregar el primer elemento
            estados.add(resultSet.getString(1));
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                estados.add(resultSet.getString(1));
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return estados; // Retornar la lista de estados/provincias
    }
    
    public static List<List<Object>> getDireccionesPorUsuario(int usuarioID){
        List<List<Object>> direcciones = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetDireccionesPorUsuario ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, usuarioID);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> direccionInfo = new ArrayList<>();
                direccionInfo.add(resultSet.getInt(1)); // Agregar el id de la direccion.
                direccionInfo.add(resultSet.getString(2)); // Agregar el nombre del país.
                direccionInfo.add(resultSet.getString(3)); // Agregar el nombre del estado/provincia.
                direccionInfo.add(resultSet.getString(4)); // Agregar la dirección #1.
                direccionInfo.add(resultSet.getString(5)); // Agregar la dirección #2.
                direccionInfo.add(resultSet.getString(6)); // Agregar la ciudad.
                direccionInfo.add(resultSet.getString(7)); // Agregar la código postal.
                direccionInfo.add(resultSet.getString(8)); // Agregar la contacto.
                direcciones.add(direccionInfo);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }
        
        return direcciones;
    }
    
    public static List<List<Object>> getMetodosPagoPorUsuario(int usuarioID){
        List<List<Object>> metodosPago = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetMetodosPagoPorUsuario ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, usuarioID);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> metodoPagoInfo = new ArrayList<>();
                metodoPagoInfo.add(resultSet.getInt(1)); // Agregar el id del método de pago.
                metodoPagoInfo.add(resultSet.getString(2)); // Agrega el número de tarjeta cifrado.
                metodoPagoInfo.add(resultSet.getString(3)); // Agregar la clave cifrada.
                metodoPagoInfo.add(resultSet.getString(4)); // Agregar el nombre del titular.
                metodoPagoInfo.add(resultSet.getString(5)); // Agregar la fecha de expiración.
                metodoPagoInfo.add(resultSet.getString(6)); // Agregar el código de seguridad.
                metodosPago.add(metodoPagoInfo);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }
        
        return metodosPago;        
    }
        
    public static List<Object> getDireccionPorID(int direccionID){
        List<Object> direccionInfo = new ArrayList<>();
        try{
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetDireccionPorID ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
             // Establecer los valores de los parámetros
             statement.setInt(1, direccionID);
             
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
             
             if (!resultSet.next()){
                 connection.close();
                 return null;
             }
             
            direccionInfo.add(resultSet.getString(1)); // Agregar el nombre del país.
            direccionInfo.add(resultSet.getString(2)); // Agregar el nombre del estado/provincia.
            direccionInfo.add(resultSet.getString(3)); // Agregar la dirección #1.
            direccionInfo.add(resultSet.getString(4)); // Agregar la dirección #2.
            direccionInfo.add(resultSet.getString(5)); // Agregar la ciudad.
            direccionInfo.add(resultSet.getString(6)); // Agregar la código postal.
            direccionInfo.add(resultSet.getString(7)); // Agregar la contacto.
            connection.close();
             
        }catch(SQLException ex){
            return null;
        }
        
        return direccionInfo;
    }
    
    public static List<Object> getMetodoPagoPorID(int metodoPagoID){
        List<Object> metodoPagoInfo = new ArrayList<>();
        try{
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetMetodoPagoPorID ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
             // Establecer los valores de los parámetros
             statement.setInt(1, metodoPagoID);
             
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
             
             if (!resultSet.next()){
                 connection.close();
                 return null;
             }
             
             metodoPagoInfo.add(resultSet.getString(1));  // Tarjeta encriptada.
             metodoPagoInfo.add(resultSet.getString(2));  // Llave encriptada.
             metodoPagoInfo.add(resultSet.getString(3));  // Titular.
             metodoPagoInfo.add(resultSet.getDate(4));  // Fecha de Vencimiento.
             metodoPagoInfo.add(resultSet.getString(5)); // Código de Seguridad (CVV)
            connection.close();
             
        }catch(SQLException ex){
            return null;
        }
        
        return metodoPagoInfo;
    }    
    
    public static List<Object> getFacturaPorID(int facturaID){
        List<Object> facturaInfo = new ArrayList<>();
        try{
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetFacturaPorID ?";
            CallableStatement statement = connection.prepareCall(sql); //crear statement
             // Establecer los valores de los parámetros
             statement.setInt(1, facturaID);
             
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
             
             if (!resultSet.next()){
                 connection.close();
                 return null;
             }
             
             facturaInfo.add(resultSet.getDate(1));  // Fecha de la Factura.
             facturaInfo.add(resultSet.getFloat(2));  // Total.
             facturaInfo.add(resultSet.getFloat(3));  // Costo de Envió.
             facturaInfo.add(resultSet.getString(4));  // Estado de la factura.
            connection.close();
             
        }catch(SQLException ex){
            return null;
        }
        
        return facturaInfo;
    }    
    
    public static int updateFactura(int facturaID, String fechaFactura, Float costoEnvio, String estado) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateFactura ?, ?, ?, ?, ?"; // Procedimiento almacenado para actualizar marca
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, facturaID); // ID de la factura
            statement.setDate(2, valueOf(fechaFactura)); // Fecha de Factura
            statement.setFloat(3, costoEnvio); // Costo de Envío
            statement.setString(4, estado); // Estado
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(5, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(5);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }
    
    public static int deleteFactura(int facturaID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteFactura ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, facturaID); // ID de la factura
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }
    
    public static int deleteLineaFactura(int lineaFacturaID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteLineaFactura ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, lineaFacturaID); // ID de la línea de factua
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }
    
    
    
    public static List<List<Object>> getProductosPorPagina(int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosPorPagina ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setInt(1, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }

    /*TODO: PARÁMETRO 2. Se podría hacer que si devuelve null hacer un warning y cerrar la página.*/
    public static List<List<Object>> getProductosOrdenadosPorPrecio(String orden, int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosOrdenadosPorPrecio ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, orden);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
  
    /*TODO: PARÁMETRO 2. Se podría hacer que si devuelve null hacer un warning y cerrar la página.*/
    public static List<List<Object>> getProductosOrdenadosPorReviews(String orden, int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosOrdenadosPorReviews ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, orden);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
  
    
    /*TODO: PARÁMETRO 2. Se podría hacer que si devuelve null hacer un warning y cerrar la página.*/
    public static List<List<Object>> getProductosPorMarcaPorPagina(String marca, int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosPorMarcaPorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, marca);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
    
    /*TODO: PARÁMETRO 2. Se podría hacer que si devuelve null hacer un warning y cerrar la página.*/
    public static List<List<Object>> getProductosPorCategoriaPorPagina(String categoria, int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosPorCategoriaPorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, categoria);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
    
    /*TODO: PARÁMETRO 2. Se podría hacer que si devuelve null hacer un warning y cerrar la página.*/
    public static List<List<Object>> getProductosPorNombrePorPagina(String nombre, int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosPorNombrePorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setString(1, nombre);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
    
    public static List<List<Object>> getProductosConDescuentoPorPagina(int pagina) {
        List<List<Object>> productos = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosConDescuentoPorPagina ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setInt(1, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productoInfo.add(resultSet.getString(5)); // Agregar etiqueta de si tiene un descuento ahora mismo.
                productos.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return productos; // Retornar la lista de estados/provincias
    }
    
    public static List<List<Object>> getProductosDeUsuarioPorPagina(int usuarioID, int pagina) {
        List<List<Object>> carritoCompra = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosDeUsuarioPorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setInt(1, usuarioID);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id carritoProducto.
                productoInfo.add(resultSet.getInt(2)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(3)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getString(4)); // Agregar la descripción del producto.
                productoInfo.add(resultSet.getInt(5)); // Agregar la cantidad en el carrito.
                productoInfo.add(resultSet.getFloat(6)); // Agregar el precio original.
                productoInfo.add(resultSet.getFloat(7)); // Agregar el descuento aplicado.
                productoInfo.add(resultSet.getFloat(8)); // Agregar el precio total.
                productoInfo.add(resultSet.getString(9)); // Agregar la imagen del producto..
                carritoCompra.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return carritoCompra; // Retornar la lista de estados/provincias
    }
    
    public static List<List<Object>>  getFacturasPorPagina(int usuarioID, int pagina){
           List<List<Object>> facturas = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetFacturasPorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, usuarioID);
            statement.setInt(2, pagina);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> factura = new ArrayList<>();
                factura.add(resultSet.getInt(1)); // Agregar el id de la factura.
                factura.add(resultSet.getDate(2)); // Agregar la fecha de la factura.
                factura.add(resultSet.getFloat(3)); // Agregar el total considerando descuentos.
                factura.add(resultSet.getFloat(4)); // Agregar el costo de envío.
                factura.add(resultSet.getString(5)); // Agregar el estado del pedido.
                facturas.add(factura);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }        
        return facturas;          
    }
    
    public static List<List<Object>>  getAllFacturasPorPagina(int pagina){
           List<List<Object>> facturas = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetAllFacturasPorPagina ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, pagina);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> factura = new ArrayList<>();
                factura.add(resultSet.getInt(1)); // Agregar el id de la factura.
                factura.add(resultSet.getDate(2)); // Agregar la fecha de la factura.
                factura.add(resultSet.getFloat(3)); // Agregar el total considerando descuentos.
                factura.add(resultSet.getFloat(4)); // Agregar el costo de envío.
                factura.add(resultSet.getString(5)); // Agregar el estado del pedido.
                facturas.add(factura);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }        
        return facturas;          
    }
    
    public static List<List<Object>> getDetallesFacturaPorPagina(int facturaID, int pagina){
            List<List<Object>> lineasFacturas = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetDetallesFacturaPorPagina ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, facturaID);
            statement.setInt(2, pagina);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> lineaFactura = new ArrayList<>();
                lineaFactura.add(resultSet.getInt(1)); // Agregar el id de la linea dea factura.
                lineaFactura.add(resultSet.getInt(2)); // Agregar el id del producto.
                lineaFactura.add(resultSet.getString(3)); // Agregar el nombre del producto.
                lineaFactura.add(resultSet.getString(4)); // Agregar la descripción del producto.
                lineaFactura.add(resultSet.getInt(5)); // Agregar la cantidad.
                lineaFactura.add(resultSet.getFloat(6)); // Agregar el precio en el momento de la factura.
                lineaFactura.add(resultSet.getFloat(7)); // Agregar el descuento aplicado en caso de haberlo.
                lineaFactura.add(resultSet.getFloat(8)); // Agregar el total cobrado.
                lineaFactura.add(resultSet.getString(9)); // Agregar el url de la imagen del producto.
                lineasFacturas.add(lineaFactura);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }        
        return lineasFacturas;          
    }       
    
    public static List<List<Object>>  getProductosCompradosPorUsuario(int usuarioID, int pagina){
           List<List<Object>> productosComprados = new ArrayList<>();
        try{
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosCompradosPorUsuario ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement            
            
            // Establecer el valor del parámetro
            statement.setInt(1, usuarioID);
            statement.setInt(2, pagina);
                    
            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();                    
            
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> producto = new ArrayList<>();
                producto.add(resultSet.getInt(1)); // Agregar el id del producto.
                producto.add(resultSet.getString(2)); // Agregar el nombre del producto.
                producto.add(resultSet.getFloat(3)); // Agregar el costo del producto.
                producto.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productosComprados.add(producto);
            }

            // Cerrar la conexión
            connection.close();            
        }catch (SQLException ex){
            return null;
        }        
        return productosComprados;          
    }    

    public static List<List<Object>> getProductosConReviews(int pagina) {
        List<List<Object>> productosConReviews = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductosConReviews ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setInt(1, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> productoInfo = new ArrayList<>();
                productoInfo.add(resultSet.getInt(1)); // Agregar el id del producto.
                productoInfo.add(resultSet.getString(2)); // Agregar el nombre del producto.
                productoInfo.add(resultSet.getFloat(3)); // Agregar el precio.
                productoInfo.add(resultSet.getString(4)); // Agregar la imagen del producto.
                productosConReviews.add(productoInfo);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return productosConReviews; // Retornar la lista de estados/provincias
    }
    
    public static List<List<Object>> getReviewsPorProducto(int productoID, int pagina) {
        List<List<Object>> reviews = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetReviewsPorProducto ?, ?"; // El procedimiento almacenado que recibe un parámetro
            CallableStatement statement = connection.prepareCall(sql); // Crear statement

            // Establecer el valor del parámetro
            statement.setInt(1, productoID);
            statement.setInt(2, pagina);

            // Ejecutar la llamada al procedimiento
            ResultSet resultSet = statement.executeQuery();
                        
            // Iterar a través de los resultados y agregar los nombres a la lista
            while (resultSet.next()) {
                List<Object> review = new ArrayList<>();
                review.add(resultSet.getInt(1)); // Agregar el id del producto.
                review.add(resultSet.getString(2)); // Agregar el nombre del usuario que hizo la review.
                review.add(resultSet.getString(3)); // Agregar el apellido del usuario que hizo la review.
                review.add(resultSet.getString(4)); // Agregar el correo del usuario que hizo la review.
                review.add(resultSet.getInt(5)); // Agregar la calificación otorgada al producto.
                review.add(resultSet.getString(6)); // Agregar el comentario que fue agregado.
                reviews.add(review);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return reviews; // Retornar la lista de estados/provincias
    }
    
    public static int verificarProductosPorPagina(int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC VerificarProductosPorPagina ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }           
    }

      public static int verificarProductosPorMarcaPorPagina(String marca, int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  VerificarProductosPorMarcaPorPagina ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            statement.setString(1, marca);
            statement.setInt(2, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }           
    }

    public static int verificarProductosPorCategoriaPorPagina(String categoria, int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  VerificarProductosPorCategoriaPorPagina ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            statement.setString(1, categoria);
            statement.setInt(2, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }           
    }
    
    public static int verificarProductosPorNombrePorPagina(String nombre, int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  VerificarProductosPorNombrePorPagina ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            statement.setString(1, nombre);
            statement.setInt(2, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }           
    }    
    
    public static int verificarProductosConDescuentoPorPagina(int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC VerificarProductosConDescuentoPorPagina ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }           
    }
    
    public static int verificarProductosDeUsuarioPorPagina(int usuarioID, int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarProductosDeUsuarioPorPagina ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros


                statement.setInt(1, usuarioID);
                statement.setInt(2, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(3, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(3);

                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                return -1;
            }           
     }

    public static int verificarFacturasPorPagina(int usuarioID, int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarFacturasPorPagina ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros
                statement.setInt(1, usuarioID);
                statement.setInt(2, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(3, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(3);
                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                ex.printStackTrace();
                return -1;
            }           
     }
    
    public static int verificarAllFacturasPorPagina(int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarAllFacturasPorPagina ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros
                statement.setInt(1, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(2, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(2);
                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                ex.printStackTrace();
                return -1;
            }           
     }
    
    public static int verificarDetallesFacturaPorPagina(int facturaID, int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarDetallesFacturaPorPagina ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros


                statement.setInt(1, facturaID);
                statement.setInt(2, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(3, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(3);

                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                return -1;
            }           
     }
    
    public static int verificarProductosCompradosPorUsuario(int usuarioID, int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarProductosCompradosPorUsuario ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros


                statement.setInt(1, usuarioID);
                statement.setInt(2, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(3, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(3);

                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                return -1;
            }           
     }
    
    public static int verificarProductosConReviews(int pagina){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC VerificarProductosConReviews ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, pagina); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }             
    }
    
    public static int verificarReviewsPorProducto(int productoID, int pagina){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC VerificarReviewsPorProducto ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros


                statement.setInt(1, productoID);
                statement.setInt(2, pagina);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(3, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(3);

                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                return -1;
            }           
     }
    
    public static int procesarCompra(int usuarioID, float total, float envio){
            try{
                 /*Establecer conexiones y ejecutar el query.*/
                Connection connection = SQLConnection.getConnection();
                String sql = "EXEC ProcesarCompra ?, ?, ?, ?";
                CallableStatement statement = connection.prepareCall(sql);//crear statement
                // Establecer los valores de los parámetros


                statement.setInt(1, usuarioID);
                statement.setFloat(2, total);
                statement.setFloat(3, envio);
                // Preparar para registrar el valor de salida
                statement.registerOutParameter(4, Types.INTEGER);

                // Ejecutar la llamada al procedimiento
                statement.execute();

                // Recuperar el valor del parámetro de salida
                int resultado = statement.getInt(4);

                //Cerrar la conexión.
                connection.close();

                return resultado; //Obtener el resultado.
            }catch(SQLException ex){
                ex.printStackTrace();
                return -1;
            }           
     }   
 
    public static int deleteDireccion(int direccionID){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteDireccion ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, direccionID); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }            
    }
  
    public static int deleteMetodoPago(int metodoPagoID){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteMetodoPago ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, metodoPagoID); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            return -1;
        }            
    }
    
    public static int deleteCarritoProducto(int usuarioID, int carritoProductoID){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteCarritoProducto ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            statement.setInt(1, usuarioID);
            statement.setInt(2, carritoProductoID);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return -1;
        }            
    }
    
    public static int verificarEstadoCarrito(int usuarioID){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC VerificarEstadoCarrito ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, usuarioID); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.            
        }catch(SQLException ex){
            return -1;
        }
    }
    
    public static int verificarBorradoSinStock(int usuarioID){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC VerificarBorradoSinStock ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
  
            statement.setInt(1, usuarioID); 
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.            
        }catch(SQLException ex){
            return -1;
        }
    }    
    
    
    public static int UpsertCarritoProducto(int usuarioID, int productoID, String productoNombre, int cantidad, float precioOriginal){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  UpsertCarritoProducto ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            statement.setInt(1, usuarioID); 
            statement.setInt(2, productoID);
            statement.setString(3, productoNombre);
            statement.setInt(4, cantidad);
            statement.setFloat(5, precioOriginal);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(6, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(6);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return -1;
        }              
    }
    
    public static int upsertDireccionUsuario(Integer direccionID, int usuarioID, String direccionLinea1, String direccionLinea2, String pais,
            String estadoProvincia, String ciudad, String codigoPostal, String telefono){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  UpsertDireccionUsuario ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            // Establecer los valores de los parámetros
            if (direccionID == null) {
                statement.setNull(1, Types.INTEGER); // Si es null, usar setNull
            } else {
                statement.setInt(1, direccionID);
            }
            statement.setInt(2, usuarioID); 
            statement.setString(3, direccionLinea1);
            statement.setString(4, direccionLinea2);
            statement.setString(5, pais);
            statement.setString(6, estadoProvincia);
            statement.setString(7, ciudad);
            statement.setString(8, codigoPostal);
            statement.setString(9, telefono);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(10, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(10);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return -4;
        }            
    }
    
    public static int createCategoria(String nombre) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearCategoria ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, nombre); // Código del país
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    
    public static int updateCategoria(int categoriaID, String nombre) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateCategoria ?, ?, ?"; // Procedimiento almacenado para actualizar marca
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, categoriaID);
            statement.setString(2, nombre);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    
    public static List<List<Object>> getCategoriasConID() {
        List<List<Object>> categorias = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "SELECT CategoriaID, Nombre FROM Categorias"; // Consulta para obtener IDs y nombres
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<Object> categoria = new ArrayList<>();
                categoria.add(resultSet.getInt("CategoriaID")); // Agregar el ID
                categoria.add(resultSet.getString("Nombre"));   // Agregar el nombre
                categorias.add(categoria);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return categorias;
    }

    public static int deleteCategoria(int categoriaID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteCategoria ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, categoriaID); // ID del país
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    public static List<List<Object>> getMarcasConID() {
        List<List<Object>> marcas = new ArrayList<>();
        try {
            // Establecer la conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetMarcasConID"; // Llamada al procedimiento almacenado
            CallableStatement statement = connection.prepareCall(sql);

            // Ejecutar el procedimiento almacenado
            ResultSet rs = statement.executeQuery();

            // Iterar por los resultados
            while (rs.next()) {
                List<Object> marca = new ArrayList<>();
                marca.add(rs.getInt("MarcaID")); // ID de la marca
                marca.add(rs.getString("Nombre")); // Nombre de la marca
                marcas.add(marca);
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return marcas;
    }
    
    
    public static int createMarca(String nombre) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearMarca ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, nombre); // Código del país
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    // Actualizar marca existente
    public static int updateMarca(int marcaID, String nuevoNombre) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateMarca ?, ?, ?"; // Procedimiento almacenado para actualizar marca
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, marcaID);
            statement.setString(2, nuevoNombre);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    // Eliminar marca
    public static int deleteMarca(int marcaID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteMarca ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, marcaID); // ID del país
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }
    
    public static int createPais(String codigo, String nombre, double costoEnvio) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearPais ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, codigo); // Código del país
            statement.setString(2, nombre); // Nombre del país
            statement.setDouble(3, costoEnvio); // Costo de envío
          
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(4, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(4);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    public static int updatePais(int paisID, String codigo, String nombre, double costoEnvio) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdatePais ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, paisID); // ID del país
            statement.setString(2, codigo); // Nuevo código del país
            statement.setString(3, nombre); // Nuevo nombre del país
            statement.setDouble(4, costoEnvio); // Nuevo costo de envío
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(5, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(5);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -3; // Indicar error
        }
    }
    
    public static int deletePais(int paisID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeletePais ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, paisID); // ID del país
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }

    public static List<List<Object>> getPaisesConID() {
        List<List<Object>> paises = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetPaisesCompleto";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<Object> pais = new ArrayList<>();
                pais.add(resultSet.getInt("PaisID")); // ID del país
                pais.add(resultSet.getString("Codigo")); // Código del país
                pais.add(resultSet.getString("Nombre")); // Nombre del país
                pais.add(resultSet.getDouble("CostoEnvio")); // Costo de envío
                paises.add(pais);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return paises;
    }
    
    public static List<List<Object>> getProvinciasConID() {
        List<List<Object>> provincias = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProvinciasConID";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                List<Object> provincia = new ArrayList<>();
                provincia.add(rs.getInt("EstadoProvinciaID")); // ID de la Provincia
                provincia.add(rs.getString("NombreProvincia")); // Nombre de la Provincia
                provincia.add(rs.getString("NombrePais")); // Nombre del País
                provincia.add(rs.getString("Codigo")); // Código del país
                provincias.add(provincia);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return provincias;
    }

    public static int createProvincia(String nombre, String paisCode) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearProvincia ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, nombre); // ID del País
            statement.setString(2, paisCode); // Nombre de la Provincia
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(3, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(3);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }


    public static int updateProvincia(int provinciaID, String nombre, String paisCode) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateProvincia ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, provinciaID); // ID de la provincia
            statement.setString(2, nombre); // ID del país
            statement.setString(3, paisCode); // Nombre de la provincia
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(4, Types.INTEGER);
             // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(4);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -3; // Indicar error
        }
    }

    public static int deleteProvincia(int provinciaID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteProvincia ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, provinciaID); // ID del estado/provincia
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(2, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(2);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }
    
    public static List<Object> getDescuentoPorID(int descuentoID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetDescuentoPorID ?"; // Suponiendo que se usa un procedimiento almacenado
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, descuentoID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                List<Object> descuento = new ArrayList<>();
                descuento.add(resultSet.getInt("DescuentoID"));
                descuento.add(resultSet.getInt("ProductoID"));
                descuento.add(resultSet.getDouble("Porcentaje"));
                descuento.add(resultSet.getString("FechaInicio"));
                descuento.add(resultSet.getString("FechaFin"));
                connection.close();
                return descuento;
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Si ocurre un error o no se encuentra el descuento
    }

   
    public static List<List<Object>> getDescuentosConID() {
        List<List<Object>> descuentos = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetDescuentosConID";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                List<Object> descuento = new ArrayList<>();
                descuento.add(rs.getInt("DescuentoID")); // ID del descuento
                descuento.add(rs.getString("Nombre")); // Nombre del producto
                descuento.add(rs.getDouble("Porcentaje")); // Porcentaje del descuento
                descuento.add(rs.getDate("FechaInicio")); // Fecha de inicio
                descuento.add(rs.getDate("FechaFin")); // Fecha de fin
                descuentos.add(descuento);
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return descuentos;
    }

    public static int createDescuento(String producto, double porcentaje, String fechaInicio, String fechaFin) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearDescuento  ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, producto); // Nombre del producto
            statement.setDouble(2, porcentaje); // Porcentaje del descuento
            statement.setDate(3, valueOf(fechaInicio)); // Fecha de inicio del descuento
            statement.setDate(4, valueOf(fechaFin)); // Fecha de fin del descuento
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(5, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(5);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }

    
    public static int updateDescuento(int descuentoID, String producto, double porcentaje, Date fechaInicio, Date fechaFin) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateDescuento ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, descuentoID); // ID del descuento
            statement.setString(2, producto); // Nombre del producto
            statement.setDouble(3, porcentaje); // Porcentaje de descuento
            statement.setDate(4, new java.sql.Date(fechaInicio.getTime())); // Fecha de inicio
            statement.setDate(5, new java.sql.Date(fechaFin.getTime())); // Fecha de fin
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(6, Types.INTEGER);
            // Ejecutar la llamada al procedimiento
            statement.execute();
            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(6);
            connection.close();
            return resultado; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -2; // Indicar error
        }
    }



    public static int deleteDescuento(int descuentoID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteDescuento ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, descuentoID);
            statement.execute();
            connection.close();
            return 1; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }

    public static int createUsuario(String nombre, String apellido, String email, String password, String telefono, String rol, String fechaNacimiento) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC CrearUsuario ?, ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, email);

            // Hashear la contraseña con BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            statement.setString(4, hashedPassword);

            statement.setString(5, telefono);
            statement.setString(6, rol);
            if (fechaNacimiento != null) {
                statement.setString(7, fechaNacimiento);
            } else {
                statement.setNull(7, java.sql.Types.VARCHAR);
            }
            statement.registerOutParameter(8, java.sql.Types.INTEGER); // Resultado
            statement.execute();
            int resultado = statement.getInt(8);
            connection.close();
            return resultado;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }
    
    public static int updateUsuario(int usuarioID, String nombre, String apellido, String email, String password, String telefono, String rol, String fechaNacimiento) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateUsuarios ?, ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, usuarioID);
            statement.setString(2, nombre);
            statement.setString(3, apellido);
            statement.setString(4, email);
            statement.setString(5, password);
            statement.setString(6, telefono);
            statement.setString(7, rol);
            if (fechaNacimiento != null) {
                statement.setString(8, fechaNacimiento);
            } else {
                statement.setNull(8, java.sql.Types.VARCHAR);
            }
            statement.execute();
            connection.close();
            return 1; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }
    
    public static int deleteUsuario(int usuarioID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC DeleteUsuario ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, usuarioID);
            statement.execute();
            connection.close();
            return 1; // Indicar éxito
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Indicar error
        }
    }

    public static List<Object> getUsuariosPorID(int usuarioID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "SELECT UsuarioID, Nombre, Apellido, Email, Telefono, FechaNacimiento, Rol " +
                         "FROM Usuarios WHERE UsuarioID = " + usuarioID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                List<Object> usuarioData = new ArrayList<>();
                usuarioData.add(resultSet.getInt("UsuarioID"));        // ID
                usuarioData.add(resultSet.getString("Nombre"));        // Nombre
                usuarioData.add(resultSet.getString("Apellido"));      // Apellido
                usuarioData.add(resultSet.getString("Email"));         // Email
                usuarioData.add(resultSet.getString("Telefono"));      // Teléfono
                usuarioData.add(resultSet.getDate("FechaNacimiento")); // Fecha de nacimiento
                usuarioData.add(resultSet.getString("Rol"));           // Rol
                connection.close();
                return usuarioData;
            } else {
                connection.close();
                return null; // Usuario no encontrado
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null; // Error durante la consulta
        }
    }

    
    public static List<List<Object>> getUsuariosConID() {
        List<List<Object>> usuarios = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "SELECT UsuarioID, Nombre, Apellido, Email, Rol FROM Usuarios";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                List<Object> usuario = new ArrayList<>();
                usuario.add(resultSet.getInt("UsuarioID")); // UsuarioID
                usuario.add(resultSet.getString("Nombre")); // Nombre
                usuario.add(resultSet.getString("Apellido")); // Apellido
                usuario.add(resultSet.getString("Email")); // Email
                usuario.add(resultSet.getString("Rol")); // Rol
                usuarios.add(usuario);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }
    
    public static List<List<Object>> getProductosConID() {
        List<List<Object>> productos = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC GetProductos";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                List<Object> producto = new ArrayList<>();
                producto.add(resultSet.getInt("ProductoID")); // UsuarioID
                producto.add(resultSet.getString("Nombre")); // Nombre
                producto.add(resultSet.getString("Categoria")); // Categoría
                producto.add(resultSet.getString("Marca")); // Marca
                producto.add(resultSet.getInt("Stock")); // Stock
                productos.add(producto);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar productos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return productos;
    }
    
    public static int upsertProducto(Integer productoID, String nombre, String descripcion, Float precio, int stock,
            String categoria, String marca, String imagen){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  UpsertProducto ?, ?, ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            // Establecer los valores de los parámetros
            if (productoID == null) {
                statement.setNull(1, Types.INTEGER); // Si es null, usar setNull
            } else {
                statement.setInt(1, productoID);
            }
            statement.setString(2, nombre); 
            statement.setString(3, descripcion);
            statement.setFloat(4, precio);
            statement.setInt(5, stock);
            statement.setString(6, categoria);
            statement.setString(7, marca);
            statement.setString(8, imagen);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(9, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(9);
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.
        }catch(SQLException ex){
            ex.printStackTrace();
            return -4;
        }            
    }
    
    public static int eliminarProducto(int productoID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC EliminarProducto ?, ?"; // Llamada al procedimiento almacenado con EXEC
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, productoID);
            // Registrar el parámetro de salida
            statement.registerOutParameter(2, Types.INTEGER);

            // Ejecutar la llamada al procedimiento
            statement.execute();
            
            // Obtener el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            connection.close();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }
    
    public static int UpsertMetodoPago(Integer metodoPagoID, int usuarioID, String numeroTarjetaCifrado, String claveCifrado,
            String nombreTitular, String fechaExpiracion, String codigoSeguridad){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  UpsertMetodoPago ?, ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            // Establecer los valores de los parámetros
            if (metodoPagoID == null) {
                statement.setNull(1, Types.INTEGER); // Si es null, usar setNull
            } else {
                statement.setInt(1, metodoPagoID);
            }
            statement.setInt(2, usuarioID); 
            statement.setString(3, numeroTarjetaCifrado);
            statement.setString(4, claveCifrado);
            statement.setString(5, nombreTitular);
            statement.setDate(6, valueOf(fechaExpiracion));
            statement.setString(7, codigoSeguridad);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(8, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(8);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.            
        }catch(SQLException ex){
            ex.printStackTrace();
            return -4;
        }
    }
    
        public static List<Object> getUsuarioData(int usuarioID) {
        List<Object> datosUsuario = new ArrayList<>();
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC ObtenerDatosUsuario ?"; // Suponiendo que tienes un SP llamado ObtenerDatosUsuario
            CallableStatement statement = connection.prepareCall(sql);

            // Establecer el parámetro de entrada
            statement.setInt(1, usuarioID);

            // Ejecutar y obtener los resultados
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                datosUsuario.add(resultSet.getString("Nombre"));
                datosUsuario.add(resultSet.getString("Apellido"));
                datosUsuario.add(resultSet.getString("Email"));
                datosUsuario.add(resultSet.getString("Telefono"));
                datosUsuario.add(resultSet.getString("FechaNacimiento"));
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datosUsuario;
    }

    
    public static int updateDatosUsuario(int usuarioID, String nombre, String apellido, String email,
        String telefono, String fechaNacimiento) {
        try {
            // Establecer conexión
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC ActualizarUsuario ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);

            // Establecer los valores de los parámetros
            statement.setInt(1, usuarioID);
            statement.setString(2, nombre);
            statement.setString(3, apellido);
            statement.setString(4, email);
            statement.setString(5, telefono);
            statement.setDate(6, valueOf(fechaNacimiento));

            // Registrar el parámetro de salida
            statement.registerOutParameter(7, Types.INTEGER);

            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Obtener el valor del parámetro de salida
            int resultado = statement.getInt(7);

            // Cerrar la conexión
            connection.close();

            return resultado; // Retornar el resultado
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // Retornar -1 en caso de error
        }
    }

    public static int deleteDatosUsuario(int usuarioID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC EliminarUsuario ?, ?"; // Llamada al procedimiento almacenado con EXEC
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, usuarioID);
            // Registrar el parámetro de salida
            statement.registerOutParameter(2, Types.INTEGER);

            // Ejecutar la llamada al procedimiento
            statement.execute();
            
            // Obtener el valor del parámetro de salida
            int resultado = statement.getInt(2);
            
            connection.close();

            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static int updateProducto(int productoID, String nombre, float precio, int stock, String categoria, String marca) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC UpdateProducto ?, ?, ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, productoID);
            statement.setString(2, nombre);
            statement.setFloat(3, precio);
            statement.setInt(4, stock);
            statement.setString(5, categoria);
            statement.setString(6, marca);
            statement.execute();
            connection.close();
            return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
        
    public static List<Object> getProductoPorID(int productoID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "SELECT ProductoID, Nombre, Precio, Stock, Categoria, Marca " +
                         "FROM Productos WHERE ProductoID = " + productoID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                List<Object> productoData = new ArrayList<>();
                productoData.add(resultSet.getInt("ProductoID")); // ID
                productoData.add(resultSet.getString("Nombre"));  // Nombre
                productoData.add(resultSet.getFloat("Precio"));   // Precio
                productoData.add(resultSet.getInt("Stock"));      // Stock
                productoData.add(resultSet.getString("Categoria"));// Categoría
                productoData.add(resultSet.getString("Marca"));   // Marca
                connection.close();
                return productoData;
            } else {
                connection.close();
                return null; // Producto no encontrado
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null; // Error durante la consulta
        }
    }    

    public static int insertarReview(int usuarioID, int productoID, int calificacion, String comentario){
        try{
             /*Establecer conexiones y ejecutar el query.*/
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC  InsertarReview ?, ?, ?, ?, ?";
            CallableStatement statement = connection.prepareCall(sql);//crear statement
            // Establecer los valores de los parámetros
            
            statement.setInt(1, usuarioID);
            statement.setInt(2, productoID); 
            statement.setInt(3, calificacion);
            statement.setString(4, comentario);
            // Preparar para registrar el valor de salida
            statement.registerOutParameter(5, Types.INTEGER);
            
            // Ejecutar la llamada al procedimiento
            statement.execute();

            // Recuperar el valor del parámetro de salida
            int resultado = statement.getInt(5);
            
            //Cerrar la conexión.
            connection.close();
        
            return resultado; //Obtener el resultado.            
        }catch(SQLException ex){
            ex.printStackTrace();
            return -2;
        }        
    }
    
}
