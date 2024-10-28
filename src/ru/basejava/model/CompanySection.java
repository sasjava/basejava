package ru.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    private final List<Company> companies;
    public CompanySection(Company... companies) {
        this(Arrays.asList(companies));
    }
    public CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return companies.equals(that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return companies.toString();
    }
}
