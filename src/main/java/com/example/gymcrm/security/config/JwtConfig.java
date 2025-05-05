package com.example.gymcrm.security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.public-key}") Resource publicKeyResource) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        RSAPublicKey publicKey = (RSAPublicKey)
                java.security.KeyFactory.getInstance("RSA")
                        .generatePublic(new java.security.spec.X509EncodedKeySpec(
                                publicKeyResource.getInputStream().readAllBytes()));

        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(
            @Value("${jwt.public-key}") Resource publicKeyResource,
            @Value("${jwt.private-key}") Resource privateKeyResource) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        RSAPublicKey publicKey = (RSAPublicKey)
                java.security.KeyFactory.getInstance("RSA")
                        .generatePublic(new java.security.spec.X509EncodedKeySpec(
                                publicKeyResource.getInputStream().readAllBytes()));

        RSAPrivateKey privateKey = (RSAPrivateKey)
                java.security.KeyFactory.getInstance("RSA")
                        .generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(
                                privateKeyResource.getInputStream().readAllBytes()));

        JWK jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(java.util.UUID.randomUUID().toString())
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
