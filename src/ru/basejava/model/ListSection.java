package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends AbstractSection {
    private final List<String> list = new ArrayList<>();

    public ListSection(String line) {
        addLine(line);
    }

    public void addLine(String line) {
        this.list.add(line);
    }

    @Override
    public List getList() {
        return list;
    }
}
