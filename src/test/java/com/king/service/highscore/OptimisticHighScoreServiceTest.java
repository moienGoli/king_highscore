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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the behaviour of OptimisticHighscoreService
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
     * Testing the result of system against predefined values. In this test followings are being tested:
     * - Each user can only have one result in the list
     * - HighScores for each level is calculated correctly
     * - Even if the list is smaller than limit, no user can have more than one score in it
     */
    @Test
    public void testHighscoreBoardUpdate() throws Exception {

        List<Integer> scores = new ArrayList<>(Arrays.asList(5, 2, 12, 1, 1, 18, 15, 12, 13, 17, 3, 11, 14, 22, 21));
        List<Integer> levelIds = new ArrayList<>(Arrays.asList(1, 1, 2, 1, 2, 2, 3, 3, 2, 1, 2, 3, 3, 3, 3));
        List<Integer> userIds = new ArrayList<>(Arrays.asList(12, 1, 2, 5, 12, 8, 12, 13, 5, 9, 9, 13, 20, 20, 20));

        Map<Integer, List> expectedResult = new HashMap<>();
        expectedResult.put(1, Arrays.asList("9=17", "12=5", "1=2", "5=1"));
        expectedResult.put(2, Arrays.asList("8=18", "5=13", "2=12", "9=3", "12=1"));
        expectedResult.put(3, Arrays.asList("20=22", "12=15", "13=12"));

        List<Score> scoreList = createListOfScores(userIds, levelIds, scores);

        when(storageService.mapScoresByUserMaxForLevel(eq(1), anyInt())).thenReturn(getScoresForLevel(scoreList, 1));
        when(storageService.mapScoresByUserMaxForLevel(eq(2), anyInt())).thenReturn(getScoresForLevel(scoreList, 2));
        when(storageService.mapScoresByUserMaxForLevel(eq(3), anyInt())).thenReturn(getScoresForLevel(scoreList, 3));
        when(storageService.getLevels()).thenReturn(new HashSet<>(Arrays.asList(1, 2, 3)));

        highScoreService.update();

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

        int level = 1;
        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i <= maxSize + 1; i++) {
            scoreList.add(new Score(i, level, i));
        }
        when(storageService.mapScoresByUserMaxForLevel(eq(1), anyInt())).thenReturn(scoreList);
        when(storageService.getLevels()).thenReturn(new HashSet<>(Collections.singletonList(1)));

        highScoreService.update();
        assertTrue(highScoreService.getHighScoresForLevel(1).size() == maxSize);
    }

    private List<Score> getScoresForLevel(List<Score> scoreList, int level) {
        return scoreList.stream().filter(element -> element.getLevelId() == level).collect(Collectors.toList());
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

    private List<Score> createListOfScores(List<Integer> userIds, List<Integer> levelIds, List<Integer> scores) {

        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            scoreList.add(new Score(userIds.get(i), levelIds.get(i), scores.get(i)));
        }
        return scoreList;
    }
}