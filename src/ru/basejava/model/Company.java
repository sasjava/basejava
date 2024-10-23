package ru.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private final String name;
    private final String website;
    private List<Period> periods;

    public Company(String name, String website,
                   LocalDate begdate, LocalDate enddate, String title, String description) {
        this.name = name;
        this.website = website;
        addPeriods(begdate, enddate, title, description);
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void addPeriods(LocalDate begdate, LocalDate enddate, String title, String description) {
        Period period = new Period(begdate, enddate, title, description);
        if (this.periods == null) {
            this.periods = new ArrayList<>();
        }
        this.periods.add(period);
    }
}
