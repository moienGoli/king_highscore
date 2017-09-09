package com.king.model;

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


    public int hashCode() {
        return userId * 31 + levelId;
    }

    public boolean equals(Object other) {

        if (other.hashCode() == this.hashCode()) {
            if (this.score == ((Score) other).score) {
                return true;
            }
        }
        return false;
    }

    public int compareTo(Score other) {

        if (other == null) {
            return 1;
        } else {
            return Integer.compare(this.score, other.score);
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
}
