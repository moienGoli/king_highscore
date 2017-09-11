package com.king.service.login;

import com.king.exception.AppException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;

import static org.junit.Assert.assertTrue;

/**
 * Test AuthenticationService
 * Created by moien on 9/11/17.
 */
public class AuthenticationServiceTest {

    AuthenticationService service = AuthenticationService.getInstance();
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetUserID() throws Exception {

        Instant now = Instant.now();
        int userID = 123584;
        String sessionToken = service.doLogin(userID);
        assertTrue(service.getUserID(sessionToken) == userID);

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("expired");
        String token = userID + service.getSeparator() + (now.getEpochSecond() - 100);
        String expiredSessionKey = CryptographyService.getInstance().encrypt(token);
        service.getUserID(expiredSessionKey);
    }

    @Test
    public void testInvalidSessionKey() {

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("Crypt exception");
        service.getUserID("Non Base64 String");
    }

    @Test
    public void testSessionToken() throws Exception {

        int userID = 566251532;
        String sessionToken = service.doLogin(userID);
        String decrypted = CryptographyService.getInstance().decrypt(sessionToken);
        String[] split = decrypted.split(service.getSeparator());
        assertTrue(split[0].equals(String.valueOf(userID)));
        assertTrue(Long.valueOf(split[1]) > Instant.now().getEpochSecond());

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("negative");
        service.doLogin(-userID);


    }
}