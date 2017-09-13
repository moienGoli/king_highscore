package com.king.storage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is a part of OPTIMISTIC APPROACH implementation
 * Receives and stores entries from users into an ConcurrentLinkedQueue.
 * This class is meant to be instantiated only in ScoreStorageService. That is why the constructor is package visibility.
 * <p>
 * <p>
 * Created by moien on 9/8/17.
 */
public class SimpleStorage<K extends Comparable<K>> {

    private Queue<K> entries = new ConcurrentLinkedQueue<>();

    public void add(K k) {
        entries.add(k);
    }

    public Queue<K> retrieveAll() {
        return entries;
    }


}