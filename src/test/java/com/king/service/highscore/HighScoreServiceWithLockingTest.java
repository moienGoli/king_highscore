package com.king.service.highscore;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;


/**
 * Tests the behavior of HighScoreServiceWithLocking
 * Created by moien on 9/11/17.
 */
public class HighScoreServiceWithLockingTest {


    /**
     * Testing the result of system against predefined values. In this test followings are being tested:
     * - Each user can only have one result in the list
     * - HighScores for each level is calculated correctly
     * - Even if the list is smaller than limit, no user can have more than one score in it
     */
    @Test
    public void testAddScoreWithFixedInputAndExpectedResult() throws Exception {

        HighScoreServiceWithLocking highScoreService = HighScoreServiceWithLocking.getInstance();

        List scores = new ArrayList<>(Arrays.asList(5, 2, 12, 1, 1, 18, 15, 12, 13, 17, 3, 11, 14, 22, 21));
        List levelIds = new ArrayList<>(Arrays.asList(1, 1, 2, 1, 2, 2, 3, 3, 2, 1, 2, 3, 3, 3, 3));
        List userIds = new ArrayList<>(Arrays.asList(12, 1, 2, 5, 12, 8, 12, 13, 5, 9, 9, 13, 20, 20, 20));

        List<Score> scoreList = createScoreList(userIds, levelIds, scores);

        Map<Integer, List> expectedResult = new HashMap<>();
        expectedResult.put(1, Arrays.asList("9=17", "12=5", "1=2", "5=1"));
        expectedResult.put(2, Arrays.asList("8=18", "5=13", "2=12", "9=3", "12=1"));
        expectedResult.put(3, Arrays.asList("20=22", "12=15", "13=12"));

        scoreList.forEach(highScoreService::addScore);
        boolean match;
        for (Map.Entry<Integer, List> entry : expectedResult.entrySet()) {
            match = checkIfMatch(entry.getValue(), highScoreService.getHighScoresForLevel(entry.getKey()));
            assertTrue(match);
        }

    }


    /**
     * Adding more than limit to an specific level should NOT result to a highScoreList.size() > limit
     */
    @Test
    public void testAgainstHighscoreListLimit() throws Exception {

        HighScoreServiceWithLocking highScoreService = HighScoreServiceWithLocking.getInstance();
        int level = 1;

        for (int i = 0; i <= highScoreService.getMaxItems() + 1; i++) {
            highScoreService.addScore(new Score(i, level, i));
        }
        assertTrue(highScoreService.getHighScoresForLevel(1).size() == highScoreService.getMaxItems());
    }

    private boolean checkIfMatch(List expected, List highScoresForLevel) {

        if (expected.size() != highScoresForLevel.size()) {
            return false;
        }

        for (int i = 0; i < expected.size(); i++) {
            if (!expected.get(i).equals(highScoresForLevel.get(i).toString())) {
                return false;
            }
        }
        return true;
    }

    private List<Score> createScoreList(List<Integer> userIds, List<Integer> levelIds, List<Integer> scores) {

        List<Score> scoreList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            scoreList.add(new Score(userIds.get(i), levelIds.get(i), scores.get(i)));
        }
        return scoreList;
    }

}