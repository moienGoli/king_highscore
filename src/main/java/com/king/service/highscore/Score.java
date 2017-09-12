package com.king.service.highscore;

/**
 * Encapsulates information of what we call score:
 * - userId
 * - levelId
 * - score
 * <p>
 * Created by moien on 9/8/17.
 */
public class Score implements Comparable<Score> {

    private final int userId;
    private final int levelId;
    private final int score;

    public Score(int userId, int levelId, int score) {

        this.userId = userId;
        this.levelId = levelId;
        this.score = score;
    }

    /**
     * Equal objects are those with same userID and LevelID. As a result of this implementation, TreeSet will only keep the
     * one with bigger score number.
     *
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        return userId == score.userId && levelId == score.levelId;

    }

    /**
     * If two scores are from two different users but their score number are the same, then '1' will be returned, as a result
     * TreeSet will not eliminate any of them and will add them next to each other.
     *
     * If two equal score numbers are from the same user, zero will be returned, as a result TreeSet will eliminate one of them.
     *
     * If two scores are from one user but with different score numbers, then the compareto will compare the numbers and TreeSet will use
     * equal method to find and eliminate the smaller one
     *
     * LevelID is not important in our  compareTo mechanism
     *
     */
    public int compareTo(Score other) {

        if (other == null) {
            return 1;
        } else {
            int scoreCompare = Integer.compare(other.score, this.score);
            if (scoreCompare == 0) {
                if (this.userId != other.userId) {
                    return 1;
                }
            }
            return scoreCompare;
        }
    }

    public int getScore() {
        return score;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getUserId() {
        return userId;
    }

    public String toString() {
        return userId + "=" + score;
    }
}
