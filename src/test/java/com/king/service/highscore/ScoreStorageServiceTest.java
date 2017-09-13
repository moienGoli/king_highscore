package com.king.service.highscore;

import com.king.storage.SimpleStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests score storage functionality
 * <p>
 * Created by moien on 9/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ScoreStorageServiceTest {


    @Mock
    private SimpleStorage<Score> storage;
    private Queue<Score> testScores;


    /**
     * Tests if the add method of storage has been called and also the level is added to available levels list
     */
    @Test
    public void testAddScore() {

        int levelId = 1;
        ScoreStorageService storageService = new ScoreStorageService(storage, anyInt());

        Score score = new Score(1, levelId, 1);
        storageService.addScore(score);
        verify(storage).add(score);
        assertTrue(storageService.getLevels().contains(levelId));
    }

    @Before
    public void makeTestData() {
        testScores = new ConcurrentLinkedQueue<>();

        testScores.add(new Score(1, 1, 2));
        testScores.add(new Score(2, 1, 3));
        testScores.add(new Score(2, 1, 4));
        testScores.add(new Score(3, 1, 3));
        testScores.add(new Score(1, 2, 1));
        testScores.add(new Score(2, 2, 3));
    }

    @Test
    public void testMapScoresByUserMaxForLevel() throws Exception {


        ScoreStorageService storageService;
        List<Score> scoreList;

        //only the highest score of userIds which are greater than the minimum and have the desired levelId should be in the list
        storageService = new ScoreStorageService(storage, 600);
        when(storage.retrieveAll()).thenReturn(testScores);
        scoreList = storageService.mapScoresByUserMaxForLevel(1, 2);
        assertEquals(scoreList, Arrays.asList(new Score(2, 1, 4), new Score(3, 1, 3)));

        //if the score has been expired it should not be in the list
        storageService = new ScoreStorageService(storage, -1);
        scoreList = storageService.mapScoresByUserMaxForLevel(1, 2);
        assertEquals(scoreList, Collections.emptyList());

    }

    @Test
    public void testRetireData() throws Exception {

        ScoreStorageService storageService;
        when(storage.retrieveAll()).thenReturn(testScores);
        int numberOfDeletedItems;

        //set ttl to a far future
        storageService = new ScoreStorageService(storage, 6000);
        numberOfDeletedItems = storageService.retireData(testScores.size() + 1);
        assertTrue(numberOfDeletedItems == 0);

        //set ttl to a past time so all the data should be retire
        int sizeBeforeRetirement = testScores.size();
        storageService = new ScoreStorageService(storage, -1);
        numberOfDeletedItems = storageService.retireData(testScores.size() + 1);
        assertTrue(numberOfDeletedItems == sizeBeforeRetirement);


    }
}