package com.king;

import com.king.model.Score;
import com.king.storage.SimpleStorage;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by moien on 9/8/17.
 */
public class Shoot {

    private static SimpleStorage<Score> storage = new SimpleStorage<>();

    public static void main(String[] args) {


        createProducer(storage);
        createObserver();

    }

    private static void createProducer(final SimpleStorage storage) {
        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    storage.add(new Score(
                            ThreadLocalRandom.current().nextInt(1, 100),
                            ThreadLocalRandom.current().nextInt(1, 5 + 1),
                            ThreadLocalRandom.current().nextInt(0, 5000)));
                }
            };
            timer.schedule(task, 0, 1);
        }).run();
    }

    private static void createObserver() {
        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {


                    Map<Integer, List<Score>> scoreBoard = new HashMap<>();
                    List a = new ArrayList(
                            Arrays.asList(
                                    new Score(1, 3, 4998),
                                    new Score(2, 3, 4995),
                                    new Score(7, 3, 5003),
                                    new Score(1, 3, 5000))
                    );
                    scoreBoard.put(3, a);


                    List<Score> collected = storage.retrieveAll().parallelStream().
                            filter(element -> element.getLevelId() == 3).
                            filter(element -> element.getScore() > 4990).
                            collect(Collectors.toMap(Score::getUserId, Function.identity(),
                                            BinaryOperator.maxBy(Comparator.comparingInt(Score::getScore)))
                            ).values().stream().collect(Collectors.toList());

                    List<Score> scores =
                            Stream.concat(scoreBoard.get(3).stream(), collected.stream()).sorted().distinct().limit(15).collect(Collectors.toList());

                    System.out.println(scores);
                }
            };
            timer.schedule(task, 15000, 15000);
        }).run();
    }
}
