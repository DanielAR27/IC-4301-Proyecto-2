package proyecto.utils;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class CheckUtils {
        // Método para encriptar la contraseña
        public static String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

        // Método para verificar la contraseña
        public static boolean checkPassword(String plainPassword, String hashedPassword) {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        }

         // Método para validar si una cadena es un correo electrónico válido
        public static boolean isValidEmail(String email) {
            String emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
            return (email != null && !email.isEmpty()) ? email.matches(emailRegex) : false;
        }

        // Método para validar si una cadena es un número de teléfono válido
        public static boolean isValidPhoneNumber(String phoneNumber) {
            // Expresión regular para validar números de teléfono que aceptan dígitos, guiones y el símbolo +
            String phoneRegex = "^[+]?\\d{1,3}?[\\s-]?\\d{1,4}[\\s-]?\\d{1,4}[\\s-]?\\d{1,9}$|^\\d{8,15}$";
            return (phoneNumber != null && !phoneNumber.isEmpty()) ? phoneNumber.matches(phoneRegex) : false;
        }

        // Método para validar la longitud de la contraseña
        public static boolean isPasswordValid(String password) {
            return (password != null && password.length() >= 5);
        }

        // Método para validar si una cadena es un código de postal válido
        public static boolean isValidPostalCode(String postalCode){
            String postalCodeRegex = "^[A-Za-z0-9 ]{3,20}$";
            return (postalCode != null && !postalCode.isEmpty()) ? postalCode.matches(postalCodeRegex) : false;
        }

        // Método para validar si una cadena es un número de tarjeta de crédito válido
        public static boolean isValidCardNumber(String cardNumber) {
            // Expresión regular para validar números de tarjeta de crédito con espacios o guiones opcionales
            String cardRegex = "^(\\d{4}[-\\s]?){3}\\d{4}$";
            return (cardNumber != null && !cardNumber.isEmpty()) ? cardNumber.matches(cardRegex) : false;
        }

        // Método para validar si una cadena es un código de seguridad (CVV) válido
        public static boolean isValidSecurityCode(String securityCode) {
            // Expresión regular para validar códigos de seguridad de 3 a 4 dígitos
            String cvvRegex = "^\\d{3,4}$";
            return (securityCode != null && !securityCode.isEmpty()) ? securityCode.matches(cvvRegex) : false;
        }

        // Método para generar una clave AES
        public static String generateKey() throws Exception {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); 
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

    
        // Método para cifrar datos
        public static String encrypt(String data, String key) throws Exception {
            // Usa directamente los bytes de la clave
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }


        // Método para desencriptar datos
        public static String decrypt(String encryptedData, String key) throws Exception {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, "UTF-8");
        }

        public static String decrpytCardNumber(String encryptedCardNumber, String encryptedKey) {
            String decryptedCardNumber;
            try{
            String claveMaestra =  "X9f!z7L*Q4b@d2W#";
            String decryptedKey = decrypt(encryptedKey, claveMaestra);
            decryptedCardNumber = decrypt(encryptedCardNumber, decryptedKey);
            }catch (Exception ex){
                return null;
            }
            return decryptedCardNumber;
        }
        
        public static String maskCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.length() < 4) {
                throw new IllegalArgumentException("El número de tarjeta debe tener al menos 4 caracteres.");
            }

            // Reemplazar todos los caracteres excepto los últimos 4 con '*'
            String masked = cardNumber.replaceAll(".(?=.{4})", "*");
            return masked;
        }

// Ejemplo de uso
public static void main(String[] args) {
    String cardNumber = "1234-1234-1234-1234";
    String maskedCardNumber = maskCardNumber(cardNumber);
    System.out.println(maskedCardNumber); // Salida esperada: "****-****-****-1234"
}


}    
