package com.king.service.login;

import com.king.exception.AppException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test AuthenticationService
 * Created by moien on 9/11/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Mock
    private CryptographyService crypt;

    private AuthenticationService auth;

    @Before
    public void setUp() throws GeneralSecurityException, IOException {

        auth = new AuthenticationService(crypt);
        when(crypt.decrypt(anyString())).then(AdditionalAnswers.returnsFirstArg());
        when(crypt.encrypt(anyString())).then(AdditionalAnswers.returnsFirstArg());
    }

    /**
     * Tests a successful doLogin and getUserID and verifies crypt method invocations
     */
    @Test
    public void testLoginAndGetUser() throws Exception {

        int userID = 123584;
        String sessionToken = auth.doLogin(userID, 100);
        verify(crypt).encrypt(anyString());
        int returnedUsrID = auth.getUserID(sessionToken);
        verify(crypt).decrypt(sessionToken);
        assertTrue(returnedUsrID == userID);
    }


    /**
     * Tests getUserID with expired sessionKey
     */
    @Test
    public void testGetUserWithExpiredSessionKey() throws GeneralSecurityException, IOException {

        int userID = 123584;
        int secondsToLive = -100;
        String expiredSessionKey = auth.doLogin(userID, secondsToLive);
        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("expired");
        auth.getUserID(expiredSessionKey);
    }


    /**
     * Tests getUserID with malformed sessionKey.
     * An exception should be thrown.
     * It is not important that crypt.* methods have been called or not
     * <p>
     * Well formed sessionKey should be like [userID][Separator][timestamp]
     */
    @Test
    public void testGetUserWithInvalidSessionKey() {

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("Crypt exception");
        auth.getUserID("InvalidUserID");
    }

    /**
     * Tests doLogin with invalid user ID which should result in exception.
     */
    @Test
    public void testLoginWithInvalidUserID() {

        int userID = -566251532;
        int secondsToLive = 100;
        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("negative");
        auth.doLogin(userID, secondsToLive);

    }


}