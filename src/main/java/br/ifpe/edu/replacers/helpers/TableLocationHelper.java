package br.ifpe.edu.replacers.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public enum TableLocationHelper {

    INSTANCE;

    private AtomicInteger counter;

    TableLocationHelper() {
        setInitialValue();
    }

    public int nextTable() {
        return counter.incrementAndGet();
    }

    public int getValue() {
        return counter.get();
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public void setInitialValue() {
        counter = new AtomicInteger(8);
    }
}
