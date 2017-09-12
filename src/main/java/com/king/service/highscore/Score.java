package com.king.service.highscore;

import java.time.Instant;

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
    private final Instant creationTime;

    public Score(int userId, int levelId, int score) {

        this.userId = userId;
        this.levelId = levelId;
        this.score = score;
        this.creationTime = Instant.now();
    }


    /**
     * Two equal Score objects in this case is two scores with the same userID and levelID.
     * The compare will tell the difference between equal Score objects
     *
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
     * But since we will always do comparison for Scores with same levelID, we are safe for collisions.
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
            return Integer.compare(other.score, this.score);
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

    public Instant getCreationTime() {
        return creationTime;
    }

    public String toString() {
        return userId + "=" + score;
    }
}
