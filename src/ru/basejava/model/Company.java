package ru.basejava.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.time.YearMonth.of;


public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String url;
    private final List<Period> periods;

    public Company(String name, String url, Period... periods) {
        this(name, url, Arrays.asList(periods));
    }

    public Company(String name, String url, List<Period> periods) {
        this.name = name;
        this.url = url;
        this.periods = periods;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return name.equals(company.name) && url.equals(company.url) && periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, periods);
    }

    @Override
    public String toString() {
        return "Company{" + name + ", " + url + ", " + periods + '}';
    }

    public static class Period implements Serializable {
        private final LocalDate beginDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        public Period(LocalDate beginDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(title, "title must not be null");
            Objects.requireNonNull(beginDate, "begdate must not be null");
            Objects.requireNonNull(endDate, "enddate must not be null");
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public Period(int beginYear, Month beginMonth, int endYear, Month endMonth,
                      String title, String description) {
            this(of(beginYear, beginMonth).atDay(1), of(endYear, endMonth).atEndOfMonth(), title, description);
        }

        public Period(int beginYear, Month beginMonth,
                      String title, String description) {
            this(of(beginYear, beginMonth).atDay(1), LocalDate.now(), title, description);
        }

        public LocalDate getBeginDate() {
            return beginDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Period{" + beginDate + endDate + title + description + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return beginDate.equals(period.beginDate) &&
                    endDate.equals(period.endDate) &&
                    title.equals(period.title) &&
                    description.equals(period.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(beginDate, endDate, title, description);
        }
    }
}
