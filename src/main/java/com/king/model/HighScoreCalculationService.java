package com.king.model;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This service class runs an scheduled task that updates the high score board
 * Created by moien on 9/9/17.
 */
public class HighScoreCalculationService {

    private static final HighScoreCalculationService service = new HighScoreCalculationService();
    private final ScoreStorageService scoreStore;
    private Map<Integer, HashSizedSortedLinkedList<Score>> highScores = new HashMap<>();
    private static final int dataRetentionSeconds = 10;
    private static final int maxItemsPerLevel = 15;


    private HighScoreCalculationService() {

        this.scoreStore = ScoreStorageService.getInstance();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        executor.scheduleWithFixedDelay(service::collectAndUpdate, 0, 5, TimeUnit.SECONDS);
    }

    public static HighScoreCalculationService getInstance() {
        return service;
    }


    private void collectAndUpdate() {

        Queue<Score> scores = scoreStore.getScores();

        Instant now = Instant.now();
        Collection<Optional<Score>> collected = scores.parallelStream().
                filter(element -> Duration.between(element.getCreationTime(), now).getSeconds() < dataRetentionSeconds).
                filter(element -> element.compareTo(getLevelLeastHighScore(element.getLevelId())) > 0).
                collect(Collectors.groupingByConcurrent(Score::hashCode, Collectors.maxBy(Comparator.comparing(Score::getScore)))).values();

        updateHighScoreBoard(collected);

    }

    private void updateHighScoreBoard(Collection<Optional<Score>> collected) {

        Iterator<Optional<Score>> iterator = collected.iterator();
        HashSizedSortedLinkedList<Score> levelHighScores;
        Score userCurrentScore;

        while (iterator.hasNext()) {
            Score next = iterator.next().get();
            levelHighScores = getHighScoresForLevel(next.getLevelId());
            userCurrentScore = levelHighScores.get(next);
            if (userCurrentScore != null) {
                if (userCurrentScore.compareTo(next) < 1) {
                    levelHighScores.remove(userCurrentScore);
                    levelHighScores.add(next);
                }
            } else {
                levelHighScores.add(next);
            }
        }
    }

    private Score getLevelLeastHighScore(int levelId) {

        HashSizedSortedLinkedList<Score> levelHighScores = highScores.get(levelId);

        if (levelHighScores.getSize() < maxItemsPerLevel) {
            return levelHighScores.getLast();
        } else {
            return null;
        }
    }

    private HashSizedSortedLinkedList<Score> getHighScoresForLevel(int level) {

        HashSizedSortedLinkedList<Score> highScores = this.highScores.get(level);
        if (highScores == null) {
            this.highScores.put(level, new HashSizedSortedLinkedList<>(maxItemsPerLevel));
        }
        return highScores;
    }

    public List<Score> getHighScoresListForLevel(int level) {
        return highScores.get(level).getList();
    }
}


