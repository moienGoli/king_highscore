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
    private HighScoreServiceWithLocking lockingService;
    private ScheduledExecutorService executor;
    private static final HighScoreServiceFactory FACTORY = new HighScoreServiceFactory();

    public static HighScoreServiceFactory getInstance() {
        return FACTORY;
    }

    private HighScoreServiceFactory() {
    }

    public HighScoreService getHighScoreService(FactoryParam param) {

        if(optimisticService != null || lockingService != null){
            throw new AppException("NOT PERMITTED.");
        }

        if (param.MODE != null && param.MODE.equals(RunMode.OPTIMISTIC)) {
            ScoreStorageService scoreStorageService = new ScoreStorageService(new SimpleStorage<>(), param.TTL);
            optimisticService = new OptimisticHighScoreService(scoreStorageService, param.MAX_ITEMS);
            scheduleExecutor(optimisticService, scoreStorageService, param);
            return optimisticService;
        } else {
            lockingService = new HighScoreServiceWithLocking(param.MAX_ITEMS);
            return lockingService;
        }
    }

    private void scheduleExecutor(OptimisticHighScoreService highScoreService, ScoreStorageService storageService, FactoryParam param) {

        executor = Executors.newScheduledThreadPool(5);
        executor.scheduleWithFixedDelay(highScoreService::update, 0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(() -> storageService.retireData(3000), 0, param.TTL, TimeUnit.SECONDS);
    }

    class FactoryParam {

        final int MAX_ITEMS;
        final int TTL;
        final RunMode MODE;

        FactoryParam(int max_items, int ttl, RunMode mode) {
            MAX_ITEMS = max_items;
            TTL = ttl;
            MODE = mode;
        }
    }

    enum RunMode {OPTIMISTIC, LOCKING}
}
