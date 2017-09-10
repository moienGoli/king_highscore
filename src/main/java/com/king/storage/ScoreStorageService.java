package com.king.storage;

import com.king.model.Score;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by moien on 9/10/17.
 */
public class ScoreStorageService {

    private final SimpleStorage<Score> store;
    private final Set<Integer> levels = new HashSet<>();

    public ScoreStorageService(SimpleStorage<Score> store) {
        this.store = store;
    }


    public void addScore(Score score) {

        levels.add(score.getLevelId());
        store.add(score);
    }

    public List<Score> mapScoresByUserMaxForLevel(int level, int minScore) {

        List<Score> collected = store.retrieveAll().parallelStream().
                filter(element -> element.getLevelId() == level).
                filter(element -> element.getScore() > minScore).
                collect(Collectors.toMap(Score::getUserId, Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparingInt(Score::getScore)))
                ).values().stream().collect(Collectors.toList());
        return collected;
    }

    public Set<Integer> getLevels() {
        return levels;
    }

    //    public Collection<Optional<Score>> groupScoresByUserMaxForLevel(int level, int minScore) {
//
//        Collection<Optional<Score>> collected = store.retrieveAll().parallelStream().
//                filter(element -> element.getLevelId() == level).
//                filter(element -> element.getScore() > minScore).sorted().
//                collect(Collectors.groupingBy(Score::getUserId,
//                        Collectors.maxBy(Comparator.comparing(Score::getScore)))).values();
//        return collected;
//    }

}
