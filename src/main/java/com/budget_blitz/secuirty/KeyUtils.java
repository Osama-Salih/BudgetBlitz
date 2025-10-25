package com.budget_blitz.secuirty;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    private KeyUtils() {}

    public static PrivateKey loadPrivateKey(final String pemPath) throws Exception {
        final String key = loadKeyFromResource(pemPath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] keyByte = Base64.getDecoder().decode(key);
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyByte);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(final String pemPath) throws Exception {
        final String key = loadKeyFromResource(pemPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] keyByte = Base64.getDecoder().decode(key);
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static String loadKeyFromResource(final String pemPath) throws Exception {
        try (final InputStream in = KeyUtils.class.getClassLoader().getResourceAsStream(pemPath)) {
            if (in == null) {
                throw new RuntimeException("Invalid pem file path");
            }
            return new String(in.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
