package com.king.service.highscore;

import com.king.exception.AppException;


/**
 * Factory class to create a HighScoreService.
 * <p>
 * Created by moien on 9/12/17.
 */
public class HighScoreServiceFactory {


    private static final HighScoreServiceFactory FACTORY = new HighScoreServiceFactory();
    private HighScoreServiceWithLocking lockingService;

    private HighScoreServiceFactory() {
    }

    public static HighScoreServiceFactory getInstance() {
        return FACTORY;
    }

    /**
     * only one highScore service is allowed per application.
     * requesting more will denied by exception throwing
     *
     * @param maxItems maximum allowed items into highscore list
     * @return a new highscore service
     */
    public HighScoreService getHighScoreService(int maxItems) {

        if (lockingService != null) {
            throw new AppException("NOT PERMITTED");
        }

        lockingService = new HighScoreServiceWithLocking(maxItems);
        return lockingService;
    }

}
