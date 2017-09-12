package com.king.service.highscore;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests delicate methods of Score object.
 *
 * Created by moien on 9/13/17.
 */
public class ScoreTest {


    /**
     * If two scores are from two different users but their score number are the same, then '1' will be returned, as a result
     * TreeSet will not eliminate any of them and will add them next to each other.
     *
     * If two equal score numbers are from the same user, zero will be returned despite the levelID, as a result TreeSet will eliminate one of them.
     *
     * If two scores are from one user but with different score numbers, then the compareTo will compare the numbers and TreeSet will use
     * equal method to find and eliminate the smaller one
     *
     * To make the descending order, the compareTo is doing Integer.compare(other.score, this.score);
     *
     * LevelID is not important in our  compareTo mechanism
     *
     */
    @Test
    public void testCompareTo() throws Exception {

        Score one;
        Score two;

        one = new Score(1,1,1);
        two = new Score(2,1,1);
        assertTrue(one.compareTo(two)==1);

        one = new Score(1,1,1);
        two = new Score(1,2,1);
        assertTrue(one.compareTo(two)==0);

        one = new Score(1,1,1);
        two = new Score(1,1,2);
        assertTrue(two.compareTo(one)==-1);


    }
}