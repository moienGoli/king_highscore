package com.king.service.highscore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the behaviour of OptimisticHighscoreService.
 *
 * <p>
 * Created by moien on 9/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class OptimisticHighScoreServiceTest {

    private int maxSize = 15;

    @Mock
    private ScoreStorageService storageService;
    private OptimisticHighScoreService highScoreService;

    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        highScoreService = new OptimisticHighScoreService(storageService, maxSize);
    }

    /**
     * Test if storageService.addScore is getting called
     */
    @Test
    public void testAddScore() {

        Score score = new Score(1, 1, 1);
        highScoreService.addScore(score);
        verify(storageService).addScore(score);
    }


    /**
     * Testing the result of system against predefined values. In this test the result of getHighScoreForLevel us being tested against:
     * - Each user can only have one result in the list
     * - HighScores for each level is calculated correctly
     * - Even if the list is smaller than limit, no user can have more than one score in it
     *
     */
    @Test
    public void testHighscoreBoardUpdate() throws Exception {

        TestDataProvide dataProvide = new TestDataProvide();
        List<Score> scoreList = dataProvide.scoreList;

        for (Integer level : dataProvide.getAvailableLevels()) {
            when(storageService.mapScoresByUserMaxForLevel(eq(level), anyInt())).thenReturn(dataProvide.getScoresForLevel(scoreList, level));
        }
        when(storageService.getLevels()).thenReturn(new HashSet<>(dataProvide.getAvailableLevels()));

        highScoreService.update();

        List<Score> highScoresForLevel;
        for (Map.Entry<Integer, List<String>> entry : dataProvide.expectedResult.entrySet()) {
            highScoresForLevel = highScoreService.getHighScoresForLevel(entry.getKey());
            assertEquals(entry.getValue(), highScoresForLevel.stream().map(Object::toString).collect(Collectors.toList()));
        }
    }

    /**
     * Adding more than limit to an specific level should NOT result to a highScoreList.size() > limit
     */
    @Test
    public void testAgainstHighscoreListLimit() throws Exception {

        int levelID = 1;
        when(storageService.mapScoresByUserMaxForLevel(eq(levelID), anyInt())).thenReturn(getListWithUniqueScores(levelID, maxSize + levelID));
        when(storageService.getLevels()).thenReturn(new HashSet<>(Collections.singletonList(levelID)));

        highScoreService.update();
        assertTrue(highScoreService.getHighScoresForLevel(levelID).size() == maxSize);
    }

    private List<Score> getListWithUniqueScores(int level, int count) {

        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            scoreList.add(new Score(i, level, i));
        }
        return scoreList;
    }

}