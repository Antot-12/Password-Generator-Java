package Antot_12;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

    public static String generate(int length, boolean includeUpperCase, boolean includeDigits, boolean includeSpecialChar) {
        StringBuilder password = new StringBuilder(length);
        String charPool = LOWERCASE;

        if (includeUpperCase) {
            charPool += UPPERCASE;
        }
        if (includeDigits) {
            charPool += DIGITS;
        }
        if (includeSpecialChar) {
            charPool += SPECIAL_CHARACTERS;
        }

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }

        return password.toString();
    }
}
