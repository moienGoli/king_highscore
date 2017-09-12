package com.king.service.login;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by moien
 * tests CryptographyService.java
 */
public class CryptographyServiceTest {

    @Test
    public void test() throws Exception {

        CryptographyService cryptService = new CryptographyService();
        String stringToCrypt = "ILikeBigHearts!!!AndICannotLie";
        String encrypted = cryptService.encrypt(stringToCrypt);

        assertTrue(stringToCrypt.equals(cryptService.decrypt(encrypted)));

        assertTrue(cryptService.decrypt("").isEmpty());
        assertTrue(cryptService.decrypt(null) == null);


    }
}
