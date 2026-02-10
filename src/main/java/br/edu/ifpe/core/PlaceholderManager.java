package br.edu.ifpe.core;


import java.util.*;
import java.util.stream.Collectors;

public enum PlaceholderManager {
    
    INSTANCE;

    public record Placeholder<K, V>(K key, V value) implements Map.Entry<String, String> {

        @Override
        public String getKey() {
            return "{{" + key.toString() + "}}";
        }

        @Override
        public String getValue() {
            return value.toString();
        }

        @Override
        public String setValue(String s) {
            throw new UnsupportedOperationException("Placeholder is immutable");
        }
    }

    private final List<Placeholder<String, String>> placeholders;

    PlaceholderManager() {
        this.placeholders = new ArrayList<>();
    }

    public <V> void addPlaceholder(String key, V value) {
        this.removeIf(key);
        this.placeholders.add(new Placeholder<>(key, Objects.toString(value)));
    }

    public <K> void addPrefersPlaceholder(final List<? extends K> keys, final int ...indexes) {
        final Set<Integer> selectedIndices = Arrays.stream(indexes).boxed().collect(Collectors.toSet());
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            if (selectedIndices.contains(i)) {
                addPlaceholder(key.toString(), "x");
            } else {
                addPlaceholder(key.toString(), " ");
            }
        }
    }

    public List<Placeholder<String, String>> getList() {
        return placeholders;
    }

    public String getValue(String key) {
        for (Placeholder<String, String> ph : placeholders) {
            if (ph.getKey().equals(formatedKey(key))) {
                return ph.getValue();
            }
        }
        return null;
    }

    private void removeIf(String key) {
        placeholders.removeIf(p -> p.getKey().equals(formatedKey(key)));
    }

    private String formatedKey(String key) {
        return "{{" + key + "}}";
    }
}
