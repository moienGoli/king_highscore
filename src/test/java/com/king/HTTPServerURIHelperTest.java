package com.king;

import com.king.exception.AppException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests URI helper
 * <p>
 * Created by moien on 9/13/17.
 */
public class HTTPServerURIHelperTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private int userID = 123;
    private int levelID = 521;
    private String sessionKey = "OohMy";
    private String GOOD_HIGHSCORE_URI = "http://locahost:9090/" + levelID + "/highscorelist";
    private String GOOD_LOGIN_URI = "http://locahost:9090/" + userID + "/login";
    private String GOOD_SCORE_POST_URI = "http://locahost:9090/" + levelID + "/score?sessionKey=" + sessionKey;
    private String BAD_SCORE_POST_URI = "http://locahost:9090/notInt/score?key=" + sessionKey;
    private String malformedMsg = "Malformed";
    private HTTPServerURIHelper helper = new HTTPServerURIHelper();

    @Test
    public void testGetService() throws Exception {

        assertTrue(helper.getService(GOOD_HIGHSCORE_URI).equals(HTTPServerURIHelper.ServiceName.HIGHSCORE));
        assertTrue(helper.getService(GOOD_LOGIN_URI).equals(HTTPServerURIHelper.ServiceName.LOGIN));
        assertTrue(helper.getService(GOOD_SCORE_POST_URI).equals(HTTPServerURIHelper.ServiceName.SCORE));
    }

    @Test
    public void testGetUserIDFromLoginURI() throws Exception {

        assertTrue(helper.getUserIDFromLoginURI(GOOD_LOGIN_URI) == userID);

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage(malformedMsg);
        String BAD_LOGIN_URI = "http://locahost:9090/notInt/login";
        assertTrue(helper.getUserIDFromLoginURI(BAD_LOGIN_URI) == userID);
    }

    @Test
    public void testGetLevelIDFromScorePostURI() throws Exception {

        assertTrue(helper.getLevelIDFromScorePostURI(GOOD_SCORE_POST_URI) == levelID);

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage(malformedMsg);
        assertTrue(helper.getLevelIDFromScorePostURI(BAD_SCORE_POST_URI) == levelID);

    }

    @Test
    public void testGetSessionKeyFromScorePostURI() throws Exception {

        assertTrue(helper.getSessionKeyFromScorePostURI(GOOD_SCORE_POST_URI).equals(sessionKey));

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage(malformedMsg);
        assertTrue(helper.getSessionKeyFromScorePostURI(BAD_SCORE_POST_URI).equals(sessionKey));
    }

    @Test
    public void testGetLevelIDFromHighScoreURI() throws Exception {

        assertTrue(helper.getLevelIDFromHighScoreURI(GOOD_HIGHSCORE_URI) == levelID);

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage(malformedMsg);
        String BAD_HIGHSCORE_URI = "http://locahost:9090/notInt/highscorelist";
        assertTrue(helper.getLevelIDFromHighScoreURI(BAD_HIGHSCORE_URI) == levelID);
    }

    @Test
    public void testGetMalformedService() throws Exception {

        String BAD_SERVICE_NAME = "http://locahost:9090/1254/jumbo";
        assertNull(helper.getService(BAD_SERVICE_NAME));
    }
}