package ru.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class CompanySection extends AbstractSection {
    private List<Company> list;

    public CompanySection(Company company) {
        addLine(company);
    }

    public void addLine(Company company) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.add(company);
    }

    @Override
    public List getList() {
        return list;
    }
}
