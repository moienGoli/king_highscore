package com.king;

import com.king.model.Score;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by moien on 9/8/17.
 */
public class Shoot {

    private static Queue<Score> scores = new ConcurrentLinkedQueue<>();


    public static void main(String[] args) {

        new Thread(() -> {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    scores.add(new Score(
                            ThreadLocalRandom.current().nextInt(1, 5 + 1),
                            ThreadLocalRandom.current().nextInt(1, 5 + 1),
                            ThreadLocalRandom.current().nextInt(1000, 5000)));
                }
            };
            timer.schedule(task, 0, 1);
        }).run();


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {


                int size = scores.size();

                System.out.println("The size of scores is : " + size);

                Score widowCat = new Score(
                        ThreadLocalRandom.current().nextInt(1, 5 + 1),
                        ThreadLocalRandom.current().nextInt(1, 5 + 1),
                        ThreadLocalRandom.current().nextInt(4000, 5000));
                Instant now = Instant.now();

                Map<Integer, Optional<Score>> collect = scores.parallelStream().
                        filter(element -> Duration.between(element.getCreationTime(), now).getSeconds() < 20).
                        filter(element -> element.compareTo(widowCat) > 0).
                        collect(Collectors.groupingByConcurrent(Score::hashCode, Collectors.maxBy(Comparator.comparing(Score::getScore))));


                System.out.println(collect);
            }
        };
        timer.schedule(task, 5000L, 100L);
    }
}
