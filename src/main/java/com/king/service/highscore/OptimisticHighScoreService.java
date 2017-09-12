package com.king.service.highscore;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * OPTIMISTIC APPROACH
 * This can be described as below:
 * - Scores are stored in an stream.
 * - every few seconds or so, will run a query on the stream and find the candidates,
 * candidates: whom are not expired and they are higher than level's minimum highscore
 * - a list of candidates will be merged with current high scores of the level and result will be sorted and duplicates will get removed.
 * - the collect&update operation will run periodically.
 * <p>
 * The benefits of this approach could be:
 * - The score receive/store and process are separated, so it is much easier to scale based on where the pressure is.
 * - When receiving the score, the least possible operation is being done, so the response time in this section will not be much affected
 * by number of requests
 * - The process of updating the HighscoreList will not be affected by number of requests.
 * - The process of updating the HighscoreList is not a case of concurrency and and corresponding challenges
 * <p>
 * But there are some down points for this approach:
 * - There might be a memory problem in DataStore if the consumer became lazy.
 * - There might be an 'UnNotice Score' problem. UnNotices scores are those who have wiped out before even getting processed.
 * - This approach may not be as accurate as the HighScoreServiceWithLocking approach, but it is eventually consistent and most of the times
 * it is as consistent as HighScoreServiceWithLocking.
 * <p>
 * In conclusion this approach should be configured delicately and there should be a back pressure mechanism to avoid outOfMemory exception in
 * case of insane load.
 * <p>
 * <p>
 * <p>
 * Created by moien on 9/10/17.
 */
public class OptimisticHighScoreService implements HighScoreService {

    private final ScoreStorageService storageService;
    private final Map<Integer, List<Score>> scoreBoard = new HashMap<>();
    private final int maxSize;

    OptimisticHighScoreService(ScoreStorageService storageService, int maxSize) {
        this.storageService = storageService;
        this.maxSize = maxSize;
    }

    protected int update() {

        List<Score> scores = null;
        for (Integer level : storageService.getLevels()) {
            initScoreBoardIfNecessary(level);
            List<Score> newEntries = storageService.mapScoresByUserMaxForLevel(level, getLastScoreFromBoard(level));
            scores = Stream.concat(scoreBoard.get(level).stream(), newEntries.stream())
                    .sorted().collect(Collectors.toList());
            scoreBoard.put(level, removeDuplicatesFromSortedList(scores, maxSize));
        }

        return scores != null ? scores.size() : 0;
    }

    private int getLastScoreFromBoard(int level) {

        List<Score> scores = scoreBoard.get(level);
        if (scores != null && !scores.isEmpty()) {
            return scores.get(scores.size() - 1).getScore();
        } else {
            return 0;
        }
    }

    private void initScoreBoardIfNecessary(Integer level) {

        if (!scoreBoard.containsKey(level)) {
            scoreBoard.put(level, new ArrayList<>());
        }
    }

    public List<Score> getHighScoresForLevel(int level) {
        return scoreBoard.get(level);
    }

    @Override
    public void addScore(Score score) {
        storageService.addScore(score);
    }

    private List<Score> removeDuplicatesFromSortedList(List<Score> scores, int limitSize) {

        HashSet<Score> seen = new HashSet<>();
        List<Score> distinctResult = new ArrayList<>();
        for (Score score : scores) {
            if (!seen.contains(score)) {
                distinctResult.add(score);
                seen.add(score);
            }
            if (distinctResult.size() >= limitSize) {
                break;
            }
        }
        return distinctResult;
    }

}
