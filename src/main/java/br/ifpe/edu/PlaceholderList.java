package br.ifpe.edu;


import java.util.*;
import java.util.stream.Collectors;

public enum PlaceholderList  {
    
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

    PlaceholderList() {
        this.placeholders = new ArrayList<>();
    }

    public <V> void addPlaceholder(String key, V value) {
        this.placeholders.add(new Placeholder<>(key, value.toString()));
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
            if (ph.getKey().equals("{{"+key+"}}")) {
                return ph.getValue();
            }
        }
        return null;
    }
}
