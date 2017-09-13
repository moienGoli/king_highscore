package com.king.service.highscore;

/**
 * Encapsulates information of what we call score:
 * - userId
 * - levelId
 * - point
 * <p>
 * Created by moien on 9/8/17.
 */
public class Score implements Comparable<Score> {

    private final int userId;
    private final int levelId;
    private final int point;

    public Score(int userId, int levelId, int point) {

        this.userId = userId;
        this.levelId = levelId;
        this.point = point;
    }

    /**
     * Equal objects are those with same userID and LevelID. As a result of this implementation, TreeSet will only keep the
     * one with bigger point.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score other = (Score) o;

        return userId == other.userId && levelId == other.levelId;

    }

    @Override
    public int hashCode() {

        int result = userId;
        result = 31 * result + levelId;
        return result;
    }

    /**
     * If two scores are from two different users but their points are the same, then '1' will be returned, as a result
     * TreeSet will not eliminate any of them and will add them next to each other.
     * <p>
     * If two equal point are from the same user, zero will be returned despite the levelID, as a result TreeSet will eliminate one of them.
     * <p>
     * If two scores are from one user but with different point, then the compareTo will compare the numbers and TreeSet will use
     * equal method to find and eliminate the smaller one
     * <p>
     * To make the descending order, the compareTo is doing Integer.compare(other.point, this.point);
     * <p>
     * LevelID is not important in our  compareTo mechanism
     */
    public int compareTo(Score other) {

        if (other == null) {
            return 1;
        } else {
            int scoreCompare = Integer.compare(other.point, this.point);
            if (scoreCompare == 0 && this.userId != other.userId) {
                return 1;
            }
            return scoreCompare;
        }
    }

    public int getPoint() {
        return point;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getUserId() {
        return userId;
    }

    public String toString() {
        return userId + "=" + point;
    }
}
