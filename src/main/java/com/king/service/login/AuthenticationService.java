package com.king.service.login;

import com.king.exception.AppException;
import com.king.service.logger.Logger;

import java.time.Instant;

/**
 * Provide services for user:
 * - Create a timed session key from userID as a part of login process. This sessionKey contains the expiration time.
 * - Decrypt sessionKey with the use of CryptographyService and extract the userID, an exception will be thrown if the sessionKey is expired.
 * <p>
 * <p>
 * Created by moien on 9/11/17.
 */
public class AuthenticationService {


    private static final String SEPARATOR = "&TIME&";
    private final CryptographyService cryptService;

    public AuthenticationService(CryptographyService cryptographyService) {
        this.cryptService = cryptographyService;
    }


    /**
     * Decrypt the token and extract the userID from it.
     * If the token is expired, AppException will be thrown.
     *
     * @param token sessionKey to decrypt
     * @return user id from the sessionKey
     */
    public int getUserID(String token) {
        try {
            String user = cryptService.decrypt(token);
            String[] split = user.split(SEPARATOR);
            long expirationEpoch = Long.parseLong(split[1]);
            if (Instant.now().getEpochSecond() > expirationEpoch) {
                throw new AppException("Session has expired");
            }
            return Integer.valueOf(split[0]);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Crypt exception", e);
        }
    }

    /**
     * Creates a token from userID with embedded expiration time using CryptographyService.
     *
     * @param userID        user id which is an unsigned integer value
     * @param secondsToLive number of seconds that the sessionKey will be active
     * @return session key
     */
    public String doLogin(int userID, int secondsToLive) {

        if (userID < 0) {
            throw new AppException("UserID can not be negative");
        }
        Logger.log("Login for user: " + userID);
        return doLogin(String.valueOf(userID), secondsToLive);
    }


    private String doLogin(String userID, int secondsToLive) {

        String token = createTokenWithExpirationTime(userID, secondsToLive);
        return cryptService.encrypt(token);
    }

    private String createTokenWithExpirationTime(String userID, int secondsToLive) {

        Instant expireTime = Instant.now().plusSeconds(secondsToLive);
        return userID + SEPARATOR + expireTime.getEpochSecond();
    }

}
