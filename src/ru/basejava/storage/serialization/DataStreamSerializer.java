package ru.basejava.storage.serialization;

import ru.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {
    @FunctionalInterface
    public interface SerialConsumer<T> {
        void accept(T t) throws IOException;
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Set<Map.Entry<ContactType, String>> contactSet = r.getContacts().entrySet();
            writeWithException(
                    contactSet, dos, entry -> {
                        dos.writeUTF(entry.getKey().name());
                        dos.writeUTF(entry.getValue());
                    }
            );
            Set<Map.Entry<SectionType, AbstractSection>> sectionSet = r.getSections().entrySet();
            writeWithException(
                    sectionSet, dos, entry -> writeSection(dos, entry.getKey(), entry.getValue())
            );
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            readWithException(
                    resume, dis, r -> r.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF())
            );
            readWithException(
                    resume, dis, r -> readSection(dis, SectionType.valueOf(dis.readUTF()), r)
            );
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection value) throws IOException {
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case OBJECTIVE, PERSONAL -> writeTextSection(dos, value);         //Позиция, Личные качества
            case ACHIEVEMENT, QUALIFICATIONS -> writeListSection(dos, value); //Достижения, Квалификация
            case EXPERIENCE, EDUCATION -> writeCompanySection(dos, value);    //Опыт работы, Образование
            default -> throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private void readSection(DataInputStream dis, SectionType sectionType, Resume resume) throws IOException {
        switch (sectionType) {
            case OBJECTIVE, PERSONAL ->
                    readTextSection(dis, sectionType.name(), resume);          //Позиция, Личные качества
            case ACHIEVEMENT, QUALIFICATIONS ->
                    readListSection(dis, sectionType.name(), resume); //Достижения, Квалификация
            case EXPERIENCE, EDUCATION ->
                    readCompanySection(dis, sectionType.name(), resume);    //Опыт работы, Образование
            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
        }
    }

    private void writeTextSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        TextSection tS = (TextSection) sectionValue;
        dos.writeUTF(tS.getContent());
    }

    private void readTextSection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        String value = dis.readUTF();
        resume.addSection(SectionType.valueOf(sectionName), new TextSection(value));
    }

    private void writeListSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        ListSection lS = (ListSection) sectionValue;
        List<String> items = lS.getItems();
        dos.writeInt(items.size());
        for (String item : items) {
            dos.writeUTF(item);
        }
    }

    private void readListSection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        int count = dis.readInt();
        List<String> items = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            items.add(dis.readUTF());
        }
        resume.addSection(SectionType.valueOf(sectionName), new ListSection(items));
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
                dos.writeUTF(p.getBeginDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                dos.writeUTF(p.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                dos.writeUTF(Objects.toString(p.getTitle(), ""));
                dos.writeUTF(Objects.toString(p.getDescription(), ""));
            }
        }
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

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos,
                                        SerialConsumer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T line : collection) {
            action.accept(line);
        }
    }

    private void readWithException(Resume resume, DataInputStream dis,
                                   SerialConsumer<Resume> action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept(resume);
        }
    }
}
