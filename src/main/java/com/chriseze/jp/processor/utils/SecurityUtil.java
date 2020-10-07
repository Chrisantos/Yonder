package com.chriseze.jp.processor.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SecurityUtil {

    public static final String HASHED_PASSWORD_KEY = "hashedPassword";
    public static final String SALT_KEY = "salt";

    public static final String BEARER = "Bearer";

    private SecretKey securityKey;

    @PostConstruct
    private void init() {
        securityKey = generateKey();
    }

    public Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private SecretKey generateKey() {
        return MacProvider.generateKey(SignatureAlgorithm.HS512);
    }

    public SecretKey getSecurityKey() {
        return securityKey;
    }

}
