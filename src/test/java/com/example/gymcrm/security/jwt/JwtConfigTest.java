package com.example.gymcrm.security.jwt;

import com.example.gymcrm.security.config.JwtConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtConfigTest {

    private final JwtConfig jwtConfig = new JwtConfig();

    @Test
    void jwtDecoder_ShouldCreateValidDecoder() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        ByteArrayResource publicKeyByteResource = new ByteArrayResource(publicKeyBytes);

        JwtDecoder decoder = jwtConfig.jwtDecoder(publicKeyByteResource);

        assertNotNull(decoder);
    }

    @Test
    void jwtEncoder_ShouldCreateValidEncoder() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        ByteArrayResource publicKeyByteResource = new ByteArrayResource(publicKeyBytes);
        ByteArrayResource privateKeyByteResource = new ByteArrayResource(privateKeyBytes);

        JwtEncoder encoder = jwtConfig.jwtEncoder(publicKeyByteResource, privateKeyByteResource);

        assertNotNull(encoder);
    }
}
