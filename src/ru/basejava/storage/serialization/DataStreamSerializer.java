package ru.basejava.storage.serialization;

import ru.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getValue().getClass().getName());
                dos.writeUTF(entry.getKey().name());
                if (entry.getValue().getClass().equals(TextSection.class)) {
                    dos.writeUTF(entry.getValue().toString());
                } else if (entry.getValue().getClass().equals(ListSection.class)) {
                    ListSection lS = (ListSection) entry.getValue();
                    List<String> items = lS.getItems();
                    dos.writeInt(items.size());
                    for (String item : items) {
                        dos.writeUTF(item);
                    }
                } else if (entry.getValue().getClass().equals(CompanySection.class)) {
                    CompanySection cS = (CompanySection) entry.getValue();
                    List<Company> companies = cS.getCompanies();
                    dos.writeInt(companies.size());
                    for (Company company : companies) {
                        dos.writeUTF(company.getName());
                        dos.writeUTF(company.getUrl());
                        List<Company.Period> periods = company.getPeriods();
                        dos.writeInt(periods.size());
                        for (Company.Period p : periods) {
                            dos.writeUTF(p.getBeginDate().toString());
                            dos.writeUTF(p.getEndDate().toString());
                            dos.writeUTF(p.getTitle());
                            dos.writeUTF(p.getDescription());
                        }
                    }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String className = dis.readUTF();
                String sectionName = dis.readUTF();
                if (className.equals(TextSection.class.getName())) {
                    String value = dis.readUTF();
                    resume.addSection(SectionType.valueOf(sectionName), new TextSection(value));
                } else if (className.equals(ListSection.class.getName())) {
                    int count = dis.readInt();
                    List<String> items = new ArrayList<>();
                    for (int j = 0; j < count; j++) {
                        items.add(dis.readUTF());
                    }
                    resume.addSection(SectionType.valueOf(sectionName), new ListSection(items));
                } else if (className.equals(CompanySection.class.getName())) {
                    int count = dis.readInt();
                    List<Company> companies = new ArrayList<>();
                    for (int j = 0; j < count; j++) {  //Companies
                        String companyName = dis.readUTF();
                        String companyUrl = dis.readUTF();
                        int countP = dis.readInt();
                        List<Company.Period> periods = new ArrayList<>();
                        for (int k = 0; k < countP; k++) {
                            LocalDate beginDate = LocalDate.parse(dis.readUTF());
                            LocalDate endDate = LocalDate.parse(dis.readUTF());
                            String title = dis.readUTF();
                            String description = dis.readUTF();
                            periods.add(new Company.Period(beginDate, endDate, title, description));
                        }
                        companies.add(new Company(companyName, companyUrl, periods));
                    }
                    resume.addSection(SectionType.valueOf(sectionName), new CompanySection(companies));
                }
            }
            return resume;
        }
    }
}
