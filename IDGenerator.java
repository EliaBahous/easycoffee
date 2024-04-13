import java.security.SecureRandom;


public class IDGenerator {

    // Define characters allowed in the ID
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Define the length of the ID
    private static final int ID_LENGTH = 10;

    // Method to generate random ID
    public static String generateID() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String id = generateID();
        System.out.println("Generated ID: " + id);
    }
}