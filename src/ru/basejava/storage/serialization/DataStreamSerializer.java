package ru.basejava.storage.serialization;

import ru.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                writeSection(dos, entry.getKey(), entry.getValue());
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
                readSection(dis, SectionType.valueOf(dis.readUTF()), resume);
            }
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection value) throws IOException {
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case OBJECTIVE -> writeTextSection(dos, value);      //Позиция
            case PERSONAL -> writeTextSection(dos, value);       //Личные качества
            case ACHIEVEMENT -> writeListSection(dos, value);    //Достижения
            case QUALIFICATIONS -> writeListSection(dos, value); //Квалификация
            case EXPERIENCE -> writeCompanySection(dos, value);  //Опыт работы
            case EDUCATION -> writeCompanySection(dos, value);   //Образование
            default -> throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private void readSection(DataInputStream dis, SectionType sectionType, Resume resume) throws IOException {
        switch (sectionType) {
            case OBJECTIVE -> readTextSection(dis, sectionType.name(), resume);       //Позиция
            case PERSONAL -> readTextSection(dis, sectionType.name(), resume);        //Личные качества
            case ACHIEVEMENT -> readListSection(dis, sectionType.name(), resume);     //Достижения
            case QUALIFICATIONS -> readListSection(dis, sectionType.name(), resume);  //Квалификация
            case EXPERIENCE -> readCompanySection(dis, sectionType.name(), resume);   //Опыт работы
            case EDUCATION -> readCompanySection(dis, sectionType.name(), resume);    //Образование
            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
        }
    }

    private void writeTextSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        dos.writeUTF(sectionValue.toString());
    }

    private void writeListSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        ListSection lS = (ListSection) sectionValue;
        List<String> items = lS.getItems();
        dos.writeInt(items.size());
        for (String item : items) {
            dos.writeUTF(item);
        }
    }

    private void writeCompanySection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        CompanySection cS = (CompanySection) sectionValue;
        List<Company> companies = cS.getCompanies();
        dos.writeInt(companies.size());
        for (Company company : companies) {
            dos.writeUTF(company.getName());
            dos.writeUTF(Objects.toString(company.getUrl(), ""));
            List<Company.Period> periods = company.getPeriods();
            dos.writeInt(periods.size());
            for (Company.Period p : periods) {
                dos.writeUTF(p.getBeginDate().toString());
                dos.writeUTF(p.getEndDate().toString());
                dos.writeUTF(Objects.toString(p.getTitle(), ""));
                dos.writeUTF(Objects.toString(p.getDescription(), ""));
            }
        }
    }

    private void readTextSection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        String value = dis.readUTF();
        resume.addSection(SectionType.valueOf(sectionName), new TextSection(value));
    }

    private void readListSection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        int count = dis.readInt();
        List<String> items = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            items.add(dis.readUTF());
        }
        resume.addSection(SectionType.valueOf(sectionName), new ListSection(items));
    }

    private void readCompanySection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
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
