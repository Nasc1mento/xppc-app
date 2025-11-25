package br.ifpe.edu.replacers;

import java.util.concurrent.atomic.AtomicInteger;

public enum TableCounter {

    INSTANCE;

    private final AtomicInteger counter = new AtomicInteger(10);

    public void newTable() {
        counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}
