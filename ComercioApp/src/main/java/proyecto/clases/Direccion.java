package proyecto.clases;


public class Direccion {
     private int direccionID;
    private String paisNombre;
    private String estadoProvinciaNombre;
    private String direccionLinea1;
    private String direccionLinea2;
    private String ciudad;
    private String codigoPostal;
    private String contacto;
    
     // Constructor
    public Direccion(int direccionID, String paisNombre, String estadoProvinciaNombre, String direccionLinea1,
                     String direccionLinea2, String ciudad, String codigoPostal, String contacto) {
        this.direccionID = direccionID;
        this.paisNombre = paisNombre;
        this.estadoProvinciaNombre = estadoProvinciaNombre;
        this.direccionLinea1 = direccionLinea1;
        this.direccionLinea2 = direccionLinea2;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.contacto = contacto;
    }

    // Getters (sin necesidad de setters si no se van a modificar)
    public int getDireccionID() { return direccionID; }
    public String getPaisNombre() { return paisNombre; }
    public String getEstadoProvinciaNombre() { return estadoProvinciaNombre; }
    public String getDireccionLinea1() { return direccionLinea1; }
    public String getDireccionLinea2() { return direccionLinea2; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getContacto() { return contacto; }   
}
