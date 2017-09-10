package com.king.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * <p>
 * Created by moien on 9/9/17.
 */
public class SortedSizedList<K extends Comparable<K>> extends ArrayList<K> {

    private final int maxSize;

    public SortedSizedList(int maxSize) {
        super(maxSize);
        this.maxSize = maxSize;
    }

    @Override
    public final boolean add(K k) {

        int index = findIndex(this, k);
        if (index < maxSize) {
            super.add(index, k);
            trimToMaxSizeIfNecessary();
            return true;
        }
        return false;
    }

    @Override
    public final void add(int index, K element) {
    }

    @Override
    public final boolean addAll(Collection<? extends K> c) {
        return false;
    }

    @Override
    public final boolean addAll(int index, Collection<? extends K> c) {
        return false;
    }

    private int findIndex(List<K> list, K value) {

        int index = Collections.binarySearch(list, value);
        if (index < 0) {
            return (index * -1) - 1;
        } else if (index == 0) {
            return 0;
        } else {
            return index - 1;
        }
    }

    private void trimToMaxSizeIfNecessary() {

        int size = size();
        if (size > maxSize) {
            removeRange(maxSize - 1, size);
        }
    }
}
