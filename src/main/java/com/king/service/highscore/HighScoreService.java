package com.king.service.highscore;

import java.util.List;

/**
 * An interface for different HighScore services.
 * <p>
 * Created by moien on 9/11/17.
 */
public interface HighScoreService {

    List getHighScoresForLevel(int level);

    void addScore(Score score);
}
