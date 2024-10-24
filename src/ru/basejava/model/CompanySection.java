package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    private List<Company> companies;

    public CompanySection(Company company) {
        Objects.requireNonNull(company, "Company must not be null");
        addItem(company);
    }

    public void addItem(Company company) {
        if (this.companies == null) {
            this.companies = new ArrayList<>();
        }
        this.companies.add(company);
    }

    public List<Company> getCompanies() {
        return companies;
    }
}
