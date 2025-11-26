package br.ifpe.edu.replacers;

import java.util.concurrent.atomic.AtomicInteger;

public enum CurrentTable {

    INSTANCE;

    private final AtomicInteger counter = new AtomicInteger(8);

    public int newTable() {
        return counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}
