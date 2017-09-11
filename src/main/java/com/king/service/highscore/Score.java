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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        if (userId != score.userId) return false;
        return levelId == score.levelId;

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
