package com.king.service.highscore;

import java.time.Instant;

/**
 * Encapsulates information of what we call point:
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
    private final Instant creationTime;

    public Score(int userId, int levelId, int point) {

        this.userId = userId;
        this.levelId = levelId;
        this.point = point;
        this.creationTime = Instant.now();
    }


    /**
     * Two equal objects in this case is two objects with the same userID and levelID.
     * The compareTo method will tell the difference between two equal objects
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score other = (Score) o;
        return userId == other.userId && levelId == other.levelId;

    }

    /**
     * Indeed it is possible to find two different pair of integers that result in same hashcode with following algorithm.
     * But since we will always do comparison for objects with same levelID, we are safe for collisions.
     */
    @Override
    public int hashCode() {

        int result = userId;
        result = 31 * result + levelId;
        return result;
    }

    public int compareTo(Score other) {

        if (other == null) {
            return 1;
        } else {
            return Integer.compare(other.point, this.point);
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

    public Instant getCreationTime() {
        return creationTime;
    }

    public String toString() {
        return userId + "=" + point;
    }
}
