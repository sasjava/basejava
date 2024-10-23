package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class TextSection extends AbstractSection {
    private final String value;

    public TextSection(String value) {
        this.value = value;
    }

    @Override
    public List getList() {
        List<String> sectionList = new ArrayList<>();
        sectionList.add(value);
        return sectionList;
    }
}
