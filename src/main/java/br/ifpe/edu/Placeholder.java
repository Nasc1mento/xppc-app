package br.ifpe.edu;

import java.util.Map;

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
