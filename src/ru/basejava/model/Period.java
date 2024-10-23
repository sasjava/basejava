package ru.basejava.model;

import java.time.LocalDate;

public class Period {
    private LocalDate begdate;
    private LocalDate enddate;
    private String title;
    private String description;

    public Period(LocalDate begdate, LocalDate enddate, String title, String description) {
        this.begdate = begdate;
        this.enddate = enddate;
        this.title = title;
        this.description = description;
    }

    public void setBegdate(LocalDate begdate) {
        this.begdate = begdate;
    }

    public LocalDate getBegdate() {
        return begdate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public LocalDate getEnddate() {
        return enddate;
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
