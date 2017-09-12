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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        return userId == score.userId && levelId == score.levelId;

    }

    /**
     * if two scores are from two different users but their score number are the same, then onr will be returned.
     * but if two equal score numbers are from same the user, zero will be returned.
     * in other cases the result of two score number comparison will be returned
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
