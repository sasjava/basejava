package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextSection extends AbstractSection {
    private final String content;

    public TextSection(String content) {
        Objects.requireNonNull(content, "Content must not be null");
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
