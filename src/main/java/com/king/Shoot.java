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
//        createObserver();
//        createObserver();

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
                    List<Score> scores;
                    scores = board.getHighScoresListForLevel(1);
                    System.out.println("Scores for level " + 1 + " : " + scores);

                    scores = board.getHighScoresListForLevel(2);
                    System.out.println("Scores for level " + 2 + " : " + scores);

                    scores = board.getHighScoresListForLevel(3);
                    System.out.println("Scores for level " + 3 + " : " + scores);

                    scores = board.getHighScoresListForLevel(4);
                    System.out.println("Scores for level " + 4 + " : " + scores);

                    scores = board.getHighScoresListForLevel(5);
                    System.out.println("Scores for level " + 5 + " : " + scores);

                    System.out.println("----------------------------------------------------------------");
                    System.out.println("++++++++++++++++++||||||||||||||||||||||||||++++++++++++++++++++");
                    System.out.println("----------------------------------------------------------------");
                }
            };
            timer.schedule(task, 500, 500);
        }).run();
    }
}
