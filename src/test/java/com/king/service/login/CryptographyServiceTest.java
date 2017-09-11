package com.king.service.login;

import com.king.service.login.CryptographyService;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by moien
 * tests CryptographyService.java
 */
public class CryptographyServiceTest{

    private String stringToCrypt = "ILikeBigHearts!!!AndICannotLie,YouOtherBrothersCannotDeny";

    @Test
    public void test() throws Exception {

        CryptographyService cryptService = CryptographyService.getInstance();
        String encrypted = cryptService.encrypt(stringToCrypt);

        assertTrue(stringToCrypt.equals(cryptService.decrypt(encrypted)));

        assertTrue(cryptService.decrypt("").isEmpty());
        assertTrue(cryptService.decrypt(null) == null);


    }
}
