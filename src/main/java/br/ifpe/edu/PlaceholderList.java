package br.ifpe.edu;


import java.util.ArrayList;
import java.util.List;

public enum PlaceholderList  {

    INSTANCE;

    private final List<Placeholder<String, String>> placeholders;

    PlaceholderList() {
        this.placeholders = new ArrayList<>();
    }

    public <V> void addPlaceholder(String key, V value) {
        this.placeholders.add(new Placeholder<>(key, value.toString()));
    }

    public List<Placeholder<String, String>> getList() {
        return placeholders;
    }
}
