package org.example.service;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.example.exception.KeyTokenException;
import org.example.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenService {

    private static final String KEY_FOLDER_PATH = "keys";
    private static final String PRIVATE_KEY_PATH = "keys/private.pem";
    private static final String PUBLIC_KEY_PATH = "keys/public.pem";
    private static final String KEYS_HEADER_FOOTER_REGEX_PATTERN = "-----(BEGIN|END)[^-]*?-----";
    private static final int JWT_EXPIRE_MINUTES = 30;
    private static final String JWT_ISSUER = "EXAMPLE.ORG";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtTokenService() {
        FileUtils.createFolderPathIfNotExist(KEY_FOLDER_PATH);
        if(!FileUtils.isFileExist(PRIVATE_KEY_PATH) || !FileUtils.isFileExist(PUBLIC_KEY_PATH)) {
            generateKeyPairs();
        }
        Pair<PrivateKey, PublicKey> keyPair = getKeyPairs();
        this.privateKey = keyPair.getLeft();
        this.publicKey = keyPair.getRight();
    }

    public Map<String, Object> getPublicKeyJwk() {
        JWK jwk = new RSAKey.Builder((RSAPublicKey) this.publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .build();
        JWKSet jwkSet = new JWKSet(jwk);
        return jwkSet.toJSONObject();
    }

    public RSAPublicKey getRSAPublicKey() {
        return (RSAPublicKey) this.publicKey;
    }

    public String generateJwtToken(String username) {
        Date createdDate = new Date();
        Date expireDate = DateUtils.addMinutes(createdDate, JWT_EXPIRE_MINUTES);
        return Jwts.builder()
                .subject(username)
                .issuedAt(createdDate)
                .expiration(expireDate)
                .signWith(privateKey)
                .issuer(JWT_ISSUER)
                .compact();
    }

    private void generateKeyPairs() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();

            String base64PrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            FileUtils.writeToFile(base64PrivateKey, PRIVATE_KEY_PATH);
            FileUtils.writeToFile(base64PublicKey, PUBLIC_KEY_PATH);

        } catch (IOException | NoSuchAlgorithmException exp) {
            // ignored
        }
    }

    private Pair<PrivateKey, PublicKey> getKeyPairs() {

        try {
            byte[] privateKeyBytes = loadKeyBytes(PRIVATE_KEY_PATH);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey pteKey = keyFactory.generatePrivate(keySpec);

            byte[] publicKeyBytes = loadKeyBytes(PUBLIC_KEY_PATH);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey pubkey = keyFactory.generatePublic(spec);

            return Pair.of(pteKey, pubkey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exp) {
            throw new KeyTokenException("Error reading rsa keys");
        }

    }

    private byte[] loadKeyBytes(String filepath) throws IOException {
        String keyFileContents = new String(Files.readAllBytes(Paths.get(filepath)));
        String keyString = keyFileContents
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("\\n", "")
                .replaceAll(KEYS_HEADER_FOOTER_REGEX_PATTERN, "");
        return Base64.getDecoder().decode(keyString);
    }


}
