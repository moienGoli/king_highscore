package com.king.service.highscore;

import com.king.exception.AppException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Just testing that the factory should not create more than one instance of HighScoreService
 * Created by moien on 9/13/17.
 */
public class HighScoreServiceFactoryTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetHighScoreService() throws Exception {


        HighScoreServiceFactory.getInstance().getHighScoreService(5);
        //the second call should result in exception
        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("NOT PERMITTED");
        HighScoreServiceFactory.getInstance().getHighScoreService(5);
    }
}