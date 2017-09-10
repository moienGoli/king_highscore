package com.king.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by moien on 9/10/17.
 */
public class HighScoreServiceWithLocking {

    private final Map<Integer, ConcurrentSkipListSet<Score>> scoreBoard = new ConcurrentHashMap<>();
    private static final int maxItems = 15;

    public void addScore(Score score) {

        initScoresForLevelIfNeeded(score.getLevelId());
        ConcurrentSkipListSet<Score> scores = scoreBoard.get(score.getLevelId());
        scores.removeIf((e) -> e.getUserId() == score.getUserId() && e.getScore() <= score.getScore());

        if (!scores.contains(score)) {
            scores.add(score);
        }
        if (scores.size() > maxItems) {
            scores.pollLast();
        }
    }

    private void initScoresForLevelIfNeeded(int level) {

        if (!scoreBoard.containsKey(level)) {
            scoreBoard.put(level, new ConcurrentSkipListSet<Score>());
        }
    }

    public List getHighScoresForLevel(int level) {

        ConcurrentSkipListSet<Score> scores = scoreBoard.get(level);
        List<Score> result = new ArrayList<>();
        if (scores != null) {
            scores.iterator().forEachRemaining(result::add);
        }
        return result;
    }


}
