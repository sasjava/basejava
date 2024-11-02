package ru.basejava.model;

import ru.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.time.YearMonth.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
    private List<Period> periods;

    public Company(String name, String url, Period... periods) {
        this(name, url, Arrays.asList(periods));
    }

    public Company(String name, String url, List<Period> periods) {
        this.name = name;
        this.url = url;
        this.periods = periods;
    }

    public Company() {
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

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        //@JsonAdapter(LocalDateAdapter.class)
        private LocalDate beginDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
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

        public Period(int beginYear, Month beginMonth, int endYear, Month endMonth,
                      String title, String description) {
            this(of(beginYear, beginMonth).atDay(1), of(endYear, endMonth).atEndOfMonth(), title, description);
        }

        public Period(int beginYear, Month beginMonth,
                      String title, String description) {
            this(of(beginYear, beginMonth).atDay(1), LocalDate.now(), title, description);
        }

        public Period() {
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
