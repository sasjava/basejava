package ru.basejava.storage.serialization;

import ru.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {
    @FunctionalInterface
    public interface IWriter<T> {
        void run(T t) throws IOException;
    }

    @FunctionalInterface
    public interface IReader {
        void run() throws IOException;
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeCollection(
                    r.getContacts().entrySet(), dos, entry -> {
                        dos.writeUTF(entry.getKey().name());
                        dos.writeUTF(entry.getValue());
                    }
            );
            writeCollection(
                    r.getSections().entrySet(), dos, entry -> writeSection(dos, entry.getKey(), entry.getValue())
            );
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            readCollection(
                    dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF())
            );
            readCollection(
                    dis, () -> readSection(dis, SectionType.valueOf(dis.readUTF()), resume)
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
                    readTextSection(dis, sectionType, resume);      //Позиция, Личные качества
            case ACHIEVEMENT, QUALIFICATIONS ->
                    readListSection(dis, sectionType, resume);     //Достижения, Квалификация
            case EXPERIENCE, EDUCATION ->
                    readCompanySection(dis, sectionType, resume);  //Опыт работы, Образование
            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
        }
    }

    private void writeTextSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        dos.writeUTF(((TextSection) sectionValue).getContent());
    }

    private void readTextSection(DataInputStream dis, SectionType sectionType, Resume resume) throws IOException {
        resume.addSection(sectionType, new TextSection(dis.readUTF()));
    }

    private void writeListSection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        writeCollection(
                ((ListSection) sectionValue).getItems(), dos, dos::writeUTF
        );
    }

    private void readListSection(DataInputStream dis, SectionType sectionType, Resume resume) throws IOException {
        List<String> items = new ArrayList<>();
        readCollection(
                dis, () -> items.add(dis.readUTF())
        );
        resume.addSection(sectionType, new ListSection(items));
    }

    private void writeCompanySection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        writeCollection(((CompanySection) sectionValue).getCompanies(), dos, company -> {
                    dos.writeUTF(company.getName());
                    dos.writeUTF(Objects.toString(company.getUrl(), ""));

                    writeCollection(
                            company.getPeriods(), dos, p -> {
                                dos.writeUTF(p.getBeginDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                                dos.writeUTF(p.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                                dos.writeUTF(Objects.toString(p.getTitle(), ""));
                                dos.writeUTF(Objects.toString(p.getDescription(), ""));
                            }
                    );
                }
        );
    }

    private void readCompanySection(DataInputStream dis, SectionType sectionType, Resume resume) throws IOException {
        List<Company> companies = new ArrayList<>();
        readCollection(dis, () -> {
                    String companyName = dis.readUTF();
                    String companyUrl = dis.readUTF();
                    List<Company.Period> periods = new ArrayList<>();
                    readCollection(dis, () -> {
                                LocalDate beginDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                periods.add(new Company.Period(beginDate, endDate, title, description));
                            }
                    );
                    companies.add(new Company(companyName, companyUrl, periods));
                }
        );
        resume.addSection(sectionType, new CompanySection(companies));
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos,
                                     IWriter<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T line : collection) {
            action.run(line);
        }
    }

    private void readCollection(DataInputStream dis, IReader action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.run();
        }
    }
}
