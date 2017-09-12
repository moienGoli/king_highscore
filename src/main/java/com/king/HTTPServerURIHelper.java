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

    private final Pattern loginPattern = Pattern.compile("/([0-9]*)/login[//]*$");
    private final Pattern scorePostPattern = Pattern.compile("/([0-9]*)/score");
    private final Pattern sessionKeyPattern = Pattern.compile("sessionKey=(.{5,})");
    private final Pattern highScoreListPattern = Pattern.compile("/([0-9]*)/highscorelist[//]*$");

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
            throw new AppException("Malformed Input");
        }
    }

    public int getLevelIDFromScorePostURI(String uri) {

        Matcher matcher = scorePostPattern.matcher(uri);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AppException("Malformed Input");
        }
    }

    public String getSessionKeyFromScorePostURI(String uri) {

        Matcher matcher = sessionKeyPattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new AppException("Malformed Input");
        }

    }

    public int getLevelIDFromHighScoreURI(String uri) {

        Matcher matcher = highScoreListPattern.matcher(uri);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AppException("Malformed Input");
        }
    }

    enum ServiceName {
        HIGHSCORE, LOGIN, SCORE;
    }
}
