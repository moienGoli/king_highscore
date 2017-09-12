package com.king.service.highscore;

import com.king.exception.AppException;


/**
 * Factory class to create a HighScoreService.
 * <p>
 * Created by moien on 9/12/17.
 */
public class HighScoreServiceFactory {


    private HighScoreServiceWithLocking lockingService;
    private static final HighScoreServiceFactory FACTORY = new HighScoreServiceFactory();

    public static HighScoreServiceFactory getInstance() {
        return FACTORY;
    }

    private HighScoreServiceFactory() {
    }

    public HighScoreService getHighScoreService(int maxItems) {

        if (lockingService != null) {
            throw new AppException("NOT PERMITTED");
        }

        lockingService = new HighScoreServiceWithLocking(maxItems);
        return lockingService;
    }

}
