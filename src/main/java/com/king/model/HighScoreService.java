package com.king.model;

import com.king.storage.ScoreStorageService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by moien on 9/10/17.
 */
public class HighScoreService {

    private final ScoreStorageService storageService;
    private final Map<Integer, List<Score>> scoreBoard = new HashMap<>();

    public HighScoreService(ScoreStorageService storageService) {
        this.storageService = storageService;
    }

    public void update() {

        for (Integer level : storageService.getLevels()) {
            initScoreBoardIfNecessary(level);
            List<Score> newEntries = storageService.mapScoresByUserMaxForLevel(level, getLastScoreFromBoard(level));

            List<Score> scores = Stream.concat(scoreBoard.get(level).stream(), newEntries.stream())
                            .sorted().distinct().limit(15).collect(Collectors.toList());

            scoreBoard.put(level, scores);
        }

    }

    private int getLastScoreFromBoard(int level) {
        return ((LinkedList<Score>) scoreBoard.get(level)).getLast().getScore();
    }

    private void initScoreBoardIfNecessary(Integer level) {

        if (!scoreBoard.containsKey(level)) {
            scoreBoard.put(level, new LinkedList<>());
        }
    }

    public List getHighScoresForLevel(int level){
        return scoreBoard.get(level);
    }

}
