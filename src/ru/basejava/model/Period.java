package ru.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private LocalDate beginDate;
    private LocalDate endDate;
    private String title;
    private String description;

    public Period(LocalDate beginDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(beginDate, "begdate must not be null");
        Objects.requireNonNull(endDate, "enddate must not be null");
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
