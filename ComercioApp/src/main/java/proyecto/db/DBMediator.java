package proyecto.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import static java.sql.Date.valueOf;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

    
    public static int updateUsuario(int usuarioID, String nombre, String apellido, String email,
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

    public static boolean deleteUsuario(int usuarioID) {
        try {
            Connection connection = SQLConnection.getConnection();
            String sql = "EXEC EliminarUsuario ?"; // Llamada al procedimiento almacenado con EXEC
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, usuarioID);

            int filasAfectadas = statement.executeUpdate();
            connection.close();

            return filasAfectadas > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                paises.add(resultSet.getString("Nombre"));
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException ex) {
            return null;
        }

        return paises; // Retornar la lista de nombres de países
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
    
}
