package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    private List<Company> companies;

    public CompanySection(Company company) {
        this.companies = new ArrayList<>();
        addItem(company);
    }

    public void addItem(Company company) {
        Objects.requireNonNull(company, "Company must not be null");
        if (this.companies == null) {
            this.companies = new ArrayList<>();
        } else {
            if (findItem(company) >= 0) {
                return;
            }
        }
        this.companies.add(company);
    }

    public List<Company> getCompanies() {
        return companies;
    }

    private int findItem(Company company) {
        int i = -1;
        String name = company.getName();
        if (!name.isEmpty()) {
            String url = company.getUrl();
            if (!url.isEmpty()) {
                for (Company comp : companies) {
                    i++;
                    if (comp.getName().equals(name) || comp.getUrl().equals(url)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
