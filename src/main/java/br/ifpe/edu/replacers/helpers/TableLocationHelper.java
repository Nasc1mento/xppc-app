package br.ifpe.edu.replacers.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public enum TableLocationHelper {

    INSTANCE;

    private final AtomicInteger counter = new AtomicInteger(0);

    public int nextTable() {
        return counter.incrementAndGet();
    }

    public int getValue() {
        return counter.get();
    }

    public void setValue(int value) {
        counter.set(value);
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public void reset() {
        setValue(0);
    }
}
