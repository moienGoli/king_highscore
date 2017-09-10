package com.king.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Receives and stores scores from users.
 * Also dispose old scores from storage.
 * <p>
 * Created by moien on 9/8/17.
 */
public class ScoreStorageService {

    private static ScoreStorageService service;
    public static final int dataRetentionSeconds = 5;
    private final long disposalIterationSeconds = 1;
    private final int disposalBatchSize = 50000;

    private Queue<Score> scores = new ConcurrentLinkedQueue<>();


    public static ScoreStorageService getInstance() {

        if (service == null) {
            service = new ScoreStorageService();
            service.runStorageEngine();
            return service;
        }
        return service;
    }

    private ScoreStorageService() {
    }

    private void runStorageEngine() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        executor.scheduleWithFixedDelay(service::disposeScores, disposalIterationSeconds, disposalIterationSeconds, TimeUnit.SECONDS);
    }

    private int disposeScores() {

        Score element;
        long secondsElapsedFromCreation;
        int loopCounter = 0;
        Instant now = Instant.now();
        while (loopCounter < disposalBatchSize) {
            element = scores.peek();
            if (element != null) {
                secondsElapsedFromCreation = Duration.between(element.getCreationTime(), now).getSeconds();
                if (secondsElapsedFromCreation > dataRetentionSeconds) {
                    scores.remove();
                } else {
                    break;
                }
            } else {
                break;
            }
            loopCounter++;
        }
        return loopCounter;
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public Queue<Score> getScores() {
        return scores;
    }


}