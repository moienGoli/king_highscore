//package com.king.model;
//
//import com.king.storage.SimpleStorage;
//
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * This service class runs an scheduled task that updates the high score board
// * Created by moien on 9/9/17.
// */
//public class HighScoreCalculationService {
//
//    private static HighScoreCalculationService service;
//    private SimpleStorage scoreStore;
//    private Map<Integer, SortedSizedList<Score>> highScores = new HashMap<>();
//    private final int maxItemsPerLevel = 15;
//    private final int boardUpdateIntervalSeconds = 1;
//
//
//    private HighScoreCalculationService() {
//
//    }
//
//    public static HighScoreCalculationService getInstance() {
//
//        if (service == null) {
//            service = new HighScoreCalculationService();
//            service.scoreStore = SimpleStorage.getInstance();
//            service.runUpdateEngine();
//        }
//        return service;
//    }
//
//    private void runUpdateEngine() {
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
//        executor.scheduleWithFixedDelay(service::collectAndUpdate, 0, boardUpdateIntervalSeconds, TimeUnit.SECONDS);
//    }
//
//    private int collectAndUpdate() {
//
//        Queue<Score> scores = scoreStore.getScores();
//        Score lastHigh = getLastHighScore(element.getLevelId());
//
//
//        updateHighScoreBoard(collected);
//        return collected.size();
//    }
//
//    private void updateHighScoreBoard(Collection<Optional<Score>> collected) {
//
//        Iterator<Optional<Score>> iterator = collected.iterator();
//        SortedSizedList<Score> levelHighScores;
//        Score userCurrentScore;
//
//        while (iterator.hasNext()) {
//            Score next = iterator.next().get();
//            levelHighScores = getHighScoresForLevel(next.getLevelId());
//            userCurrentScore = getUserCurrentScore(levelHighScores, next);
//            if (userCurrentScore != null) {
//                if (userCurrentScore.compareTo(next) < 1) {
//                    levelHighScores.remove(userCurrentScore);
//                    levelHighScores.add(next);
//                }
//            } else {
//                levelHighScores.add(next);
//            }
//        }
//    }
//
//    private Score getUserCurrentScore(List<Score> highScores, Score next) {
//
//        for (Score score : highScores) {
//            if (score.hashCode() == next.hashCode()) {
//                return score;
//            }
//        }
//        return null;
//    }
//
//    private Score getLastHighScore(int levelId) {
//
//        SortedSizedList<Score> levelHighScores = highScores.get(levelId);
//        if (levelHighScores != null) {
//            if (levelHighScores.size() < maxItemsPerLevel) {
//                return null;
//            } else {
//                return levelHighScores.get(maxItemsPerLevel - 1);
//            }
//        }
//        return null;
//    }
//
//    private SortedSizedList<Score> getHighScoresForLevel(int level) {
//
//        SortedSizedList<Score> highScores = this.highScores.get(level);
//        if (highScores == null) {
//            highScores = new SortedSizedList<>(maxItemsPerLevel);
//            this.highScores.put(level, highScores);
//        }
//        return highScores;
//    }
//
//    public List<Score> getHighScoresListForLevel(int level) {
//
//        SortedSizedList<Score> highScores = this.highScores.get(level);
//        if (highScores != null) {
//            return highScores;
//        } else {
//            return new ArrayList<>();
//        }
//    }
//}
//
//
