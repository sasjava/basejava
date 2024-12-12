package ru.basejava.model;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Company> companies;

    public CompanySection(Company... companies) {
        this(Arrays.asList(companies));
    }

    public CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    public CompanySection() {
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompany(Company company) {
        int ix = -1;
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).getName().equals(company.getName())) {
                ix = i;
                break;
            }
        }
        if (ix < 0) {
            companies.add(company);
        } else {
            Company companyOld = companies.get(ix);
            company.getPeriods().forEach(companyOld::addPeriod);
        }
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
