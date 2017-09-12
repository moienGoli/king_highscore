package com.king.service.highscore;

import java.util.Iterator;
import java.util.List;

/**
 * An interface for HighScore services.
 * <p>
 * Created by moien on 9/11/17.
 */
public interface HighScoreService {

    List getHighScoresForLevel(int level);

    void addScore(Score score);
}
