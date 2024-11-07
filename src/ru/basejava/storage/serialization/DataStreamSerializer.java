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

            writeWithException(
                    r.getContacts().entrySet(), dos, entry -> {
                        dos.writeUTF(entry.getKey().name());
                        dos.writeUTF(entry.getValue());
                    }
            );
            writeWithException(
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

            readWithException(
                    dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF())
            );
            readWithException(
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
        writeWithException(
                ((ListSection) sectionValue).getItems(), dos, dos::writeUTF
        );
    }

    private void readListSection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        List<String> items = new ArrayList<>();
        readWithException(
                dis, () -> items.add(dis.readUTF())
        );
        resume.addSection(SectionType.valueOf(sectionName), new ListSection(items));
    }

    private void writeCompanySection(DataOutputStream dos, AbstractSection sectionValue) throws IOException {
        CompanySection cS = (CompanySection) sectionValue;
        List<Company> companies = cS.getCompanies();
        writeWithException(companies, dos, company -> {
                    dos.writeUTF(company.getName());
                    dos.writeUTF(Objects.toString(company.getUrl(), ""));

                    writeWithException(
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

    private void readCompanySection(DataInputStream dis, String sectionName, Resume resume) throws IOException {
        List<Company> companies = new ArrayList<>();
        readWithException(dis, () -> {
                    String companyName = dis.readUTF();
                    String companyUrl = dis.readUTF();
                    List<Company.Period> periods = new ArrayList<>();
                    readWithException(dis, () -> {
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
        resume.addSection(SectionType.valueOf(sectionName), new CompanySection(companies));
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos,
                                        IWriter<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T line : collection) {
            action.run(line);
        }
    }

    private void readWithException(DataInputStream dis, IReader action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.run();
        }
    }
}
