package com.king.service.highscore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * This might be the classic approach.
 * <p>
 * I am using data structures that support concurrency.
 * Every score is getting served and the scoreBoard is being updated on the arrival of each score.
 * This approach may cause bottleneck in huge loads, but it wont crash since it has handled memory consumption.
 * As a result of the bottleneck the response time will increase and the system will become lazy.
 * <p>
 * This approach is meant to be consistent
 * <p>
 * Created by moien on 9/10/17.
 */
public class HighScoreServiceWithLocking implements HighScoreService {

    private final Map<Integer, ConcurrentSkipListSet<Score>> scoreBoard = new ConcurrentHashMap<>();
    private final int maxItems;


    HighScoreServiceWithLocking(int maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * If there is already a higher score from the user in the list, this score will not inserted, but if this score is higher
     * than what user already has here, this will be replaced with that.
     * <p>
     * If there is not a highscore from the user in the list, and the highscore list size limit has not reached, this score will
     * be inserted no matter what, but if the limit has reached and this score is smaller than all of them, it will not get inserted.
     * <p>
     * In other cases this score wll get inserted in its place and the list will remain sorted.
     * <p>
     * note: there is a chance of overlap of scores in this implementation but it is limited to the scores of a same user,
     * in the real world it is almost impossible that same user post a score for the same level that fast. But if this is the case
     * we should add a sync block to this method.
     *
     * @param score the new arrived score
     */
    public void addScore(Score score) {

        initScoresForLevelIfNeeded(score.getLevelId());
        ConcurrentSkipListSet<Score> scores = scoreBoard.get(score.getLevelId());
        scores.removeIf(e -> e.getUserId() == score.getUserId() && e.getPoint() < score.getPoint());

        if (!contains(scores, score)) {
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
            scoreBoard.put(level, new ConcurrentSkipListSet<>());
        }
    }

    /**
     * Get the scores for level and iterate them while inserting into a list
     *
     * @param level the integer levelID that you want to retrieve high scores for it.
     * @return list of desc sorted contains top highscores
     */
    public List<Score> getHighScoresForLevel(int level) {

        ConcurrentSkipListSet<Score> scores = scoreBoard.get(level);
        List<Score> result = new ArrayList<>();
        if (scores != null) {
            scores.iterator().forEachRemaining(result::add);
        }
        return result;
    }

}
