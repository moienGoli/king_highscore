package com.king.service.highscore;

import com.king.exception.AppException;
import com.king.storage.SimpleStorage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Factory class to create a HighScoreService.
 * For an OptimisticHighScoreService this class will also run an executor with fixed delay.
 * All the parameters of executor can be change to configurable params.
 * An optimisticHighScoreService needs two schedules:
 * - to update board
 * - to retire data from storage
 * <p>
 * Created by moien on 9/12/17.
 */
public class HighScoreServiceFactory {


    private OptimisticHighScoreService optimisticService;
    private static final HighScoreServiceFactory FACTORY = new HighScoreServiceFactory();

    public static HighScoreServiceFactory getInstance() {
        return FACTORY;
    }

    private HighScoreServiceFactory() {
    }

    public HighScoreService getHighScoreService(int maxItems, int ttlSeconds) {

        if (optimisticService != null) {
            throw new AppException("NOT PERMITTED.");
        }

        ScoreStorageService scoreStorageService = new ScoreStorageService(new SimpleStorage<>(), ttlSeconds);
        optimisticService = new OptimisticHighScoreService(scoreStorageService, maxItems);
        scheduleExecutor(optimisticService, scoreStorageService, ttlSeconds);
        return optimisticService;

    }

    private void scheduleExecutor(OptimisticHighScoreService highScoreService, ScoreStorageService storageService, int ttl) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        executor.scheduleWithFixedDelay(highScoreService::update, 0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(() -> storageService.retireData(3000), 0, ttl, TimeUnit.SECONDS);
    }

}
