package aalquicira.api.gateway.Util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
	
	public static String encrypt(String plainText, String secretKey) throws Exception {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("El texto a cifrar no puede estar vacío");
        }

        // 1. Generar IV aleatorio de 12 bytes
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        // 2. Configurar AES-GCM
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

        // 3. Cifrar el texto plano
        byte[] cipherTextWithTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // 4. Concatenar IV (12 bytes) + CipherTextWithTag
        byte[] combined = new byte[iv.length + cipherTextWithTag.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherTextWithTag, 0, combined, iv.length, cipherTextWithTag.length);

        // 5. Retornar en Base64
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String secretoBase64, String secretKey) throws Exception {
        if (secretoBase64 == null || secretoBase64.isBlank()) {
            throw new IllegalArgumentException("El secreto no puede estar vacío");
        }

        String secretoLimpio = secretoBase64.trim().replace(" ", "+");        
        byte[] decodedBytes = Base64.getDecoder().decode(secretoLimpio);

        if (decodedBytes.length < 28) { // Mínimo 12 bytes IV + 16 bytes Tag
            throw new IllegalArgumentException("Payload cifrado inválido o corrupto");
        }

        // Extraer IV (primeros 12 bytes)
        byte[] iv = Arrays.copyOfRange(decodedBytes, 0, 12);

        // Extraer Ciphertext + Tag (desde el byte 12 en adelante)
        byte[] cipherTextWithTag = Arrays.copyOfRange(decodedBytes, 12, decodedBytes.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

        cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

        byte[] plainTextBytes = cipher.doFinal(cipherTextWithTag);
        return new String(plainTextBytes, StandardCharsets.UTF_8);
    }
}