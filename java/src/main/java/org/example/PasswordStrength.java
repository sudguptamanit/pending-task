package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |


public class PasswordStrength {

    public static String checkStrength(String password) {
        if (password == null) return "Weak";

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        String specialChars = "!@#$%^&*(";

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (specialChars.indexOf(ch) != -1) {
                hasSpecial = true;
            }
        }

        int length = password.length();

        // Strong condition
        if (hasLower && hasUpper && hasDigit && hasSpecial && length >= 8) {
            return "Strong";
        }

        // Moderate condition
        if (hasLower && hasUpper && hasSpecial && length >= 6) {
            return "Moderate";
        }

        return "Weak";
    }

    public static void main(String[] args) {
        System.out.println(checkStrength("gfg!@12")); // Moderate
        System.out.println(checkStrength("SapientGlobalMarkets!@12")); // Strong
    }
}
