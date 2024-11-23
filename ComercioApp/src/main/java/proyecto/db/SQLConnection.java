package proyecto.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
        private static final String connectionString =
                "jdbc:sqlserver://LAPTOP-CE4754FQ;"
                + "database=DB_Proyecto;"
                + "user=sa;"
                + "password='8WOcnFn<18+;"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";     
    public static Connection getConnection(){
        try {
            Connection connection;
            connection = DriverManager.getConnection(connectionString);
            return connection;
        } catch (SQLException ex) {
            System.out.println("La conexión ha fallado: " + ex.toString());
        }
        return null;
    }
    
}