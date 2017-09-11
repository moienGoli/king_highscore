package com.king.storage;

import com.king.service.highscore.Score;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a part of OPTIMISTIC APPROACH implementation
 * Runs around SimpleStorage and adds some querying capabilities.
 * For querying I have used Java 8 streams to filter, map and collect data from Simple storage.
 *
 * About retention seconds: because of memory consumption every score has a limited seconds to stay in the data store.
 *                          after expiring, the score wont be present in queries and eventually will be wiped out.
 *                          One of the challenges of the Optimistic approach is to find a good retention seconds to limit
 *                          and hopefully eliminating the chance of 'UnNotice Score'. UnNotices scores are those who have
 *                          wiped out before even getting processed.
 * <p>
 * Created by moien on 9/10/17.
 */
public class ScoreStorageService {

    private final SimpleStorage<Score> store;
    private final Set<Integer> levels = new HashSet<>();
    private final int retentionSeconds = 3;

    public ScoreStorageService(SimpleStorage<Score> store) {
        this.store = store;
    }


    /**
     * Input score will be stored in SimpleStorage.
     * The level id will be extracted and stored separately.
     *
     * @param score Input score from app
     */
    public void addScore(Score score) {
        levels.add(score.getLevelId());
        store.add(score);
    }

    /**
     * Use java streams to query the score's stream and find all the scores of a level that are higher than minScore.
     * Also the retention seconds is considered in this query, it means that old scores will not get selected.
     * This operation uses compareTo() to compare the elements.
     *
     * @param level a game level that we are interested in getting scores for
     * @param minScore a minimum score that all the other scores should be higher than it.
     * @return list of scores in level and higher than minScore
     */
    public List<Score> mapScoresByUserMaxForLevel(int level, int minScore) {
        Instant now = Instant.now();
        return store.retrieveAll().parallelStream().
                filter(element -> Duration.between(element.getCreationTime(), now).getSeconds() < retentionSeconds).
                filter(element -> element.getLevelId() == level).
                filter(element -> element.getScore() > minScore).
                collect(Collectors.toMap(Score::getUserId, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingInt(Score::getScore)))
                ).values().stream().collect(Collectors.toList());
    }

    /**
     * @return A set that contains all the available levels till now.
     */
    public Set<Integer> getLevels() {
        return levels;
    }
}
