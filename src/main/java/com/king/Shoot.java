package com.king;

import com.king.model.HighScoreCalculationService;
import com.king.model.Score;
import com.king.model.ScoreStorageService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by moien on 9/8/17.
 */
public class Shoot {

    private static ScoreStorageService storage = ScoreStorageService.getInstance();
    private static HighScoreCalculationService board = HighScoreCalculationService.getInstance();

    public static void main(String[] args) {

        createProducer(storage);
        createProducer(storage);
        createProducer(storage);
        createObserver();
        createObserver();
        createObserver();

    }

    private static void createProducer(final ScoreStorageService storage) {
        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    storage.addScore(new Score(
                            ThreadLocalRandom.current().nextInt(1, 100),
                            ThreadLocalRandom.current().nextInt(1, 5 + 1),
                            ThreadLocalRandom.current().nextInt(1000, 5000)));
                }
            };
            timer.schedule(task, 0, 1);
        }).run();
    }

    private static void createObserver() {
        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    int level = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                    List<Score> scores = board.getHighScoresListForLevel(level);
//                    System.out.println("Scores for level " + level + " : " + scores);
                }
            };
            timer.schedule(task, 0, 10);
        }).run();
    }
}
