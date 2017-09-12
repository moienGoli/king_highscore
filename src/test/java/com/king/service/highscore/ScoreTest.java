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
     * TreeSet will not eliminate it and will add them next to each other.
     * But if two equal score numbers are from the same user, zero will be returned, as a result TreeSet will eliminate one of them
     * in other cases the result of two score number comparison will be returned.
     * LevelID is not important in our  compareTo mechanism
     *
     * Because our data structure is base on TreeSet and TreeSet is an ordered data structure, tje compareTo method of Score
     * Object should cover above cases.
     */
    @Test
    public void testCompareTo() throws Exception {

        Score one;
        Score two;

        one = new Score(1,1,1);
        two = new Score(2,1,1);
        assertTrue(one.compareTo(two)==1);

        one = new Score(1,1,1);
        two = new Score(1,1,1);
        assertTrue(one.compareTo(two)==1);



    }
}