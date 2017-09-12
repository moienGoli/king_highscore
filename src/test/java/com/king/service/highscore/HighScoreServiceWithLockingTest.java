package com.king.service.highscore;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        int maxItems = 15;
        TestDataProvider dataProvider = new TestDataProvider();
        HighScoreServiceWithLocking highScoreService = new HighScoreServiceWithLocking(maxItems);

        List<Score> scoreList = dataProvider.scoreList;


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
        int maxItems = 5;
        HighScoreServiceWithLocking highScoreService = new HighScoreServiceWithLocking(maxItems);
        int level = 1;

        for (int i = 0; i <= maxItems + 1; i++) {
            highScoreService.addScore(new Score(i, level, i));
        }
        assertTrue(highScoreService.getHighScoresForLevel(1).size() == maxItems);
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