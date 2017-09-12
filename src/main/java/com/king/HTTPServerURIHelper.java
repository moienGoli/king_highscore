package com.king;

import com.king.exception.AppException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class to extract values from URI.
 * <p>
 * Created by moien on 9/11/17.
 */
public class HTTPServerURIHelper {

    private static final Pattern loginPattern = Pattern.compile("/([0-9]*)/login[//]*$");
    private static final Pattern scorePostPattern = Pattern.compile("/([0-9]*)/score");
    private static final Pattern sessionKeyPattern = Pattern.compile("sessionKey=(.{5,})");
    private static final Pattern highScoreListPattern = Pattern.compile("/([0-9]*)/highscorelist[//]*$");
    private static final String MALFORMED_INPUT = "Malformed Input";


    public ServiceName getService(String uri) {

        if (uri.contains("highscorelist")) {
            return ServiceName.HIGHSCORE;
        } else if (uri.contains("score")) {
            return ServiceName.SCORE;
        } else if (uri.contains("login")) {
            return ServiceName.LOGIN;
        }
        return null;
    }

    public int getUserIDFromLoginURI(String uri) {

        Matcher matcher = loginPattern.matcher(uri);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AppException(MALFORMED_INPUT);
        }
    }

    public int getLevelIDFromScorePostURI(String uri) {

        Matcher matcher = scorePostPattern.matcher(uri);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AppException(MALFORMED_INPUT);
        }
    }

    public String getSessionKeyFromScorePostURI(String uri) {

        Matcher matcher = sessionKeyPattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new AppException(MALFORMED_INPUT);
        }

    }

    public int getLevelIDFromHighScoreURI(String uri) {

        Matcher matcher = highScoreListPattern.matcher(uri);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AppException(MALFORMED_INPUT);
        }
    }

    enum ServiceName {
        HIGHSCORE, LOGIN, SCORE
    }
}
