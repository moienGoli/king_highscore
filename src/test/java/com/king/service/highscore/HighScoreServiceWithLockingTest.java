package com.king.service.highscore;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
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
        List<Score> scoreList = dataProvider.scoreList;
        HighScoreServiceWithLocking highScoreService = new HighScoreServiceWithLocking(maxItems);


        List<Score> highScoresForLevel;
        scoreList.forEach(highScoreService::addScore);
        for (Map.Entry<Integer, List<String>> entry : dataProvider.expectedResult.entrySet()) {
            highScoresForLevel = highScoreService.getHighScoresForLevel(entry.getKey());
            assertEquals(entry.getValue(), highScoresForLevel.stream().map(Object::toString).collect(Collectors.toList()));
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

}