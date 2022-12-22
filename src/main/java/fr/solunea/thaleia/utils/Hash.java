package fr.solunea.thaleia.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * <p>Hash class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class Hash {

    private static final Logger logger = Logger.getLogger(Hash.class);

    /**
     * Calcule l'empreinte SHA-256 de cette chaîne de caractère, dont les octets
     * sont obtenus en l'interprétant en UTF-8. Identique au résultat de la
     * commande Unix :
     * <p>
     * <pre>
     * echo -n toHash | sha256sum
     * </pre>
     *
     * @param toHash a {@link java.lang.String} object.
     * @return le hash
     */
    public static String getHash(String toHash) {
        String hash = "";
        try {
            hash = DigestUtils.sha256Hex((toHash).getBytes(StandardCharsets.UTF_8));

            // logger.warn("COMMENTEZ-MOI !! SHA-256 '" + toHash + "' -> '"
            // + hash + "'");

        } catch (Exception e) {
            // On ne logue pas la chaîne qui pose problème, car c'est
            // certainement un mot de passe.
            logger.error("Impossible de calculer l'empreinte de la chaîne : " + e);
        }

        return hash;
    }

    public static String getSHA1Hash(File file) {
        try {
            MessageDigest shaDigest = MessageDigest.getInstance("SHA-1");
            return getFileChecksum(shaDigest, file);

        } catch (Exception e) {
            logger.warn(e);
            return "undefined";
        }
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            byte[] bytes = digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

        } catch (Exception e) {
            logger.warn("Impossible de générer le hash SHA1 du fichier " + file.getAbsolutePath(), e);
            throw new Exception("Impossible de générer le hash SHA1 du fichier " + file.getAbsolutePath(), e);
        }
    }
}
