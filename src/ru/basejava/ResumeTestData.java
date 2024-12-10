package ru.basejava;

import ru.basejava.model.*;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResumeTestData {
    public static final String FNAME_1 = "Иван";
    public static final String FNAME_2 = "Мария";
    public static final String FNAME_3 = "Александр";
    public static final String FNAME_NEW = "Новый";
    public static final String DUMMY = "dummy";
    public static final Resume R1 = createResumeData(String.valueOf(UUID.randomUUID()), FNAME_1);
    public static final Resume R2 = createResumeData(String.valueOf(UUID.randomUUID()), FNAME_2);
    public static final Resume R3 = createResumeData(String.valueOf(UUID.randomUUID()), FNAME_3);
    public static final Resume R_NEW = createResumeData(String.valueOf(UUID.randomUUID()), FNAME_NEW);
    public static final Resume R_DUMMY = createResumeData("uuiddummy", DUMMY);

    public static void main(String[] args) {
        Resume r = createResumeData("uuid1", "Григорий Кислин");

        System.out.println("--------------------------------");
        System.out.println(r.getFullName());
        System.out.println("--------------------------------");

        ContactType contactType;
        Map<ContactType, String> contacts = r.getContacts();
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            contactType = entry.getKey();
            System.out.println(contactType.getTitle() + ": " + entry.getValue());
        }
        System.out.println("----------------");

        SectionType sectionType;
        AbstractSection section;
        TextSection textSection;
        ListSection listSection;
        CompanySection companySection;

        List<String> items;
        List<Company> companies;

        List<Company.Period> periods;
        String description;

        Map<SectionType, AbstractSection> sections = r.getSections();
        for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
            sectionType = entry.getKey();
            section = entry.getValue();
            System.out.println(sectionType.getTitle() + ": ");

            if (section.getClass() == TextSection.class) {
                textSection = (TextSection) section;
                System.out.println(textSection.getContent());
            } else if (section.getClass() == ListSection.class) {
                listSection = (ListSection) section;
                items = listSection.getItems();
                for (String item : items) {
                    System.out.println(item);
                }
            } else {
                companySection = (CompanySection) section;
                companies = companySection.getCompanies();
                for (Company comp : companies) {
                    System.out.println(comp.getName() + "(" + comp.getUrl() + ")");
                    periods = comp.getPeriods();
                    for (Company.Period period : periods) {
                        System.out.println("\t" + period.getBeginDate() + "\t" + period.getEndDate() + "\t" + period.getTitle());
                        description = period.getDescription();
                        if (!description.isEmpty()) {
                            System.out.println("\t\t" + period.getDescription());
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println("----------------");
    }

    public static Resume createResumeData(String uuid, String fullName) {
        Resume r = new Resume(uuid, fullName);
        switch (fullName) {
            case FNAME_1 -> {
                r.addContact(ContactType.PHONE, "+7(921) 111-1234");
                r.addContact(ContactType.SKYPE, "ivan.nn");
                r.addContact(ContactType.MAIL, "ivan@yandex.ru");
                r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/ivan");
                r.addContact(ContactType.GITHUB, "https://github.com/ivan");
                r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/112233");
                r.addContact(ContactType.HOMEPAGE, "http://ivan.ru/");
            }
            case FNAME_2 -> {
                r.addContact(ContactType.PHONE, "+7(921) 222-0077");
                r.addContact(ContactType.SKYPE, "mar.ia");
                r.addContact(ContactType.MAIL, "maria@yandex.ru");
                r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/maria");
                r.addContact(ContactType.GITHUB, "https://github.com/maria");
                r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/558877");
                r.addContact(ContactType.HOMEPAGE, "http://maria.ru/");
            }
            case FNAME_3 -> {
                r.addContact(ContactType.PHONE, "+7(999) 999-0112");
                r.addContact(ContactType.SKYPE, "alex.andr");
                r.addContact(ContactType.MAIL, "alexandr@yandex.ru");
                r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/alexandr");
                r.addContact(ContactType.GITHUB, "https://github.com/alexandr");
                r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/338422");
                r.addContact(ContactType.HOMEPAGE, "http://alexandr.ru/");
            }
            default -> {
                r.addContact(ContactType.PHONE, "+7(999) 000-1111");
                r.addContact(ContactType.SKYPE, "new.name");
                r.addContact(ContactType.MAIL, "newname@yandex.ru");
                r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/newname");
                r.addContact(ContactType.GITHUB, "https://github.com/newname");
                r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/001122");
                r.addContact(ContactType.HOMEPAGE, "http://newname.ru/");
            }
        }
        addSections(r);
        return r;
    }

    private static void addSections(Resume r) {
        //Позиция
        r.addSection(SectionType.OBJECTIVE,
                new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        //PERSONAL("Личные качества")
        r.addSection(SectionType.PERSONAL,
                new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность."));

        //ACHIEVEMENT("Достижения")
        ListSection achievement = new ListSection("Достижение 1.", "Достижение 2.");
        r.addSection(SectionType.ACHIEVEMENT, achievement);

        //QUALIFICATIONS("Квалификация")
        ListSection qualifications = new ListSection("Квалификация 1.", "Квалификация 2.");
        r.addSection(SectionType.QUALIFICATIONS, qualifications);

        //EXPERIENCE("Опыт работы")
        r.addSection(SectionType.EXPERIENCE,
                new CompanySection(
                        new Company("Java Online Projects", null, //"http://javaops.ru/",
                                new Company.Period(2013, Month.of(10),
                                        "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.")
                        ),
                        new Company("Wrike", "https://www.wrike.com/",
                                new Company.Period(2014, Month.of(10), 2016, Month.of(1),
                                        "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike.")
                        )
                )
        );

        //EDUCATION("Образование")
        r.addSection(SectionType.EDUCATION,
                new CompanySection(
                        new Company("Coursera", "https://www.coursera.org/course/progfun",
                                new Company.Period(2013, Month.of(3), 2013, Month.of(5),
                                        "Functional Programming Principles in Scala' by Martin Odersky", "")
                        ),
                        new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                                "http://www.ifmo.ru/",
                                new Company.Period(1993, Month.of(9), 1996, Month.of(7),
                                        "Аспирантура (программист С, С++)", null),
                                new Company.Period(1987, Month.of(9), 1993, Month.of(7),
                                        "Инженер (программист Fortran, C)", null)
                        )
                )
        );
    }
}
