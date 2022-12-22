package fr.solunea.thaleia.utils;

import javax.mail.internet.InternetAddress;

public class MailUtils {
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
}
