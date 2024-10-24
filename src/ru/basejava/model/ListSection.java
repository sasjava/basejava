package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private final List<String> items = new ArrayList<>();

    public ListSection(String item) {
        Objects.requireNonNull(item, "line must not be null");
        addItem(item);
    }

    public void addItem(String line) {
        this.items.add(line);
    }

    public List<String> getItems() {
        return items;
    }
}
