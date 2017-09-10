package com.king;

import com.king.model.HighScoreServiceWithLocking;
import com.king.model.Score;
import com.king.storage.SimpleStorage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by moien on 9/8/17.
 */
public class Shoot {

    private static SimpleStorage<Score> storage = new SimpleStorage<>();
    private static HighScoreServiceWithLocking highScoreServiceWithLocking = new HighScoreServiceWithLocking();

    public static void main(String[] args) {


        makeScoresFly();
//        makeScoresFly();
//        makeScoresFly();
//        makeScoresFly();
//        makeScoresFly();
//        makeScoresFly();
        createObserver();
        createObserver();

    }

    private static void makeScoresFly() {
        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    highScoreServiceWithLocking.addScore(new Score(
                            ThreadLocalRandom.current().nextInt(1, 5),
                            ThreadLocalRandom.current().nextInt(1, 5),
                            ThreadLocalRandom.current().nextInt(0, 5000)));
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
                    System.out.println(highScoreServiceWithLocking.getHighScoresForLevel(3));
                }
            };
            timer.schedule(task, 500, 500);
        }).run();
    }
}
