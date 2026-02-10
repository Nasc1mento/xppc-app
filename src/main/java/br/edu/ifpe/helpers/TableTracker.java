package br.edu.ifpe.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public enum TableTracker {

    INSTANCE;

    private final AtomicInteger counter = new AtomicInteger();

    public int nextTable() {
        return counter.incrementAndGet();
    }

    public int getValue() {
        return counter.get();
    }

    public void setValue(final int value) {
        counter.set(value);
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public void reset() {
        setValue(0);
    }
}
