package utils;

import java.util.concurrent.ThreadLocalRandom;

public class UserDetailsGenerator {
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String CHARACTERS = LETTERS + "0123456789";

    public static String generateRandomName() {
    	int length = ThreadLocalRandom.current().nextInt(5, 15); // Random length between 5 and 10

        StringBuilder sb = new StringBuilder(length);

        // First character: must be a letter
        int firstIndex = ThreadLocalRandom.current().nextInt(LETTERS.length());
        sb.append(LETTERS.charAt(firstIndex));

        // Remaining characters: can be letters or digits
        for (int i = 1; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
    
    public static String generateRandomEmail() {
		String username = generateRandomName();
		String domain = generateRandomName();
		return username.toLowerCase() + "@" + domain.toLowerCase() + ".com";
	}

}
