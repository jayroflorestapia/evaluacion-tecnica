package mx.evaluacion.entrada.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AesService {
    private final SecretKeySpec key;
    public AesService(@Value("${app.aes.secret-key}") String encodedKey) {
        byte[] bytes = Base64.getDecoder().decode(encodedKey);
        if (bytes.length != 32) throw new IllegalStateException("La llave AES debe tener 32 bytes");
        key = new SecretKeySpec(bytes, "AES");
    }
    // Formato interoperable con Web Crypto: Base64(IV de 12 bytes + ciphertext + tag GCM de 16 bytes).
    public String descifrar(String input) {
        try {
            byte[] all = Base64.getDecoder().decode(input);
            if (all.length < 29) throw new IllegalArgumentException("secreto cifrado invalido");
            byte[] iv = Arrays.copyOfRange(all, 0, 12);
            byte[] cipherText = Arrays.copyOfRange(all, 12, all.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) { throw new IllegalArgumentException("No se pudo descifrar el secreto"); }
    }
}
