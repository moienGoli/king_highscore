package com.king.service.highscore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds test data for OptimisticHighScoreServiceTest
 * This is where I hide test data to make test classes cleaner.
 * These data are not dummy and they mean something.
 * <p>
 * Created by moien on 9/12/17.
 */
public class TestDataProvider {

    private List<Integer> scores = new ArrayList<>(Arrays.asList(5, 2, 12, 1, 1, 18, 15, 12, 13, 17, 3, 11, 14, 22, 21));
    private List<Integer> levelIds = new ArrayList<>(Arrays.asList(1, 1, 2, 1, 2, 2, 3, 3, 2, 1, 2, 3, 3, 3, 3));
    private List<Integer> userIds = new ArrayList<>(Arrays.asList(12, 1, 2, 5, 12, 8, 12, 13, 5, 9, 9, 13, 20, 20, 20));
    public List<Integer> availableLevels = new ArrayList<>(Arrays.asList(1, 2, 3));
    public Map<Integer, List<String>> expectedResult;
    public List<Score> scoreList;

    public TestDataProvider() {

        scoreList = createListOfScores();
        expectedResult = new HashMap<>();
        expectedResult.put(1, Arrays.asList("9=17", "12=5", "1=2", "5=1"));
        expectedResult.put(2, Arrays.asList("8=18", "5=13", "2=12", "9=3", "12=1"));
        expectedResult.put(3, Arrays.asList("20=22", "12=15", "13=12"));
    }

    private List<Score> createListOfScores() {

        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            scoreList.add(new Score(userIds.get(i), levelIds.get(i), scores.get(i)));
        }
        return scoreList;
    }

    public List<Score> getScoresForLevel(List<Score> scoreList, int level) {
        return scoreList.stream().filter(element -> element.getLevelId() == level).collect(Collectors.toList());
    }


}
