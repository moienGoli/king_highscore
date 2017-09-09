package com.king.model;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Receives and stores scores from users
 * Created by moien on 9/8/17.
 */
public class ScoreStorageService {

    private final static ScoreStorageService service = new ScoreStorageService();
    private Queue<Score> scores = new ConcurrentLinkedQueue<>();

    public static ScoreStorageService getInstance() {
        return service;
    }

    private ScoreStorageService() {
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public Queue<Score> getScores() {
        return scores;
    }


}