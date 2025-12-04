package br.ifpe.edu.replacers.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public enum CurrentTable {

    INSTANCE;

    private final AtomicInteger counter = new AtomicInteger(8);

    public int nextTable() {
        return counter.incrementAndGet();
    }

    public int getValue() {
        return counter.get();
    }

    public AtomicInteger getCounter() {
        return counter;
    }
}
