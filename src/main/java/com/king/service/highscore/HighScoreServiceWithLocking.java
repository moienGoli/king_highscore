package com.king.service.highscore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * This might be the classic approach.
 *
 * I am using data structures that support concurrency.
 * Every score is getting served and the scoreBoard is being updated on the arrival of each request.
 * This approach may cause bottleneck in huge loads, but it wont crash since it has handled memory consumption and as a result of the bottleneck
 * the response time will increase and the system will become lazy.
 * This approach is meant to be consistent but it is as consistent as the response time goes. e.g if it took 3 seconds to process a score, then
 * we are as consistent as 3 seconds.
 *
 * Created by moien on 9/10/17.
 */
public class HighScoreServiceWithLocking implements HighScoreService {

    private final static HighScoreServiceWithLocking service = new HighScoreServiceWithLocking();
    private final Map<Integer, ConcurrentSkipListSet<Score>> scoreBoard = new ConcurrentHashMap<>();
    private static final int maxItems = 15;


    public static HighScoreServiceWithLocking getInstance(){
        return service;
    }
    private HighScoreServiceWithLocking() {
    }

    public void addScore(Score score) {

        initScoresForLevelIfNeeded(score.getLevelId());
        ConcurrentSkipListSet<Score> scores = scoreBoard.get(score.getLevelId());
        scores.removeIf((e) -> e.getUserId() == score.getUserId() && e.getScore() <= score.getScore());

        if (!contains(scores,score)) {
            scores.add(score);
        }
        if (scores.size() > maxItems) {
            scores.pollLast();
        }
    }

    private boolean contains(Set<Score> set, Score score) {
        for (Score sc : set) {
            if (sc.getUserId() == score.getUserId())
                return true;
        }
        return false;
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
