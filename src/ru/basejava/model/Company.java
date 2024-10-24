package ru.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private final String name;
    private final String url;
    private List<Period> periods;

    public Company(String name, String url,
                   LocalDate beginDate, LocalDate endDate, String title, String description) {
        this.name = name;
        this.url = url;
        addPeriods(beginDate, endDate, title, description);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void addPeriods(LocalDate beginDate, LocalDate endDate, String title, String description) {
        Period period = new Period(beginDate, endDate, title, description);
        if (this.periods == null) {
            this.periods = new ArrayList<>();
        }
        this.periods.add(period);
    }
}
