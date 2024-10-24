package ru.basejava;

import ru.basejava.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume r = new Resume("Григорий Кислин");
        r.addContact(ContactType.PHONE, "+7(921) 855-0482");
        r.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        r.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        r.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        r.addContact(ContactType.HOMEPAGE, "http://gkislin.ru/");

        //Позиция
        r.addSection(SectionType.OBJECTIVE,
                new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        //PERSONAL("Личные качества")
        r.addSection(SectionType.PERSONAL,
                new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность."));

        //ACHIEVEMENT("Достижения")
        ListSection achievement = new ListSection("Достижение 1.");
        achievement.addItem("Достижение 2.");
        r.addSection(SectionType.ACHIEVEMENT, achievement);

        //QUALIFICATIONS("Квалификация")
        ListSection qualifications = new ListSection("Квалификация 1.");
        qualifications.addItem("Квалификация 2.");
        r.addSection(SectionType.QUALIFICATIONS, qualifications);

        //EXPERIENCE("Опыт работы")
        Company company = new Company("Java Online Projects", "http://javaops.ru/",
                LocalDate.of(2013, 10, 1), LocalDate.now(),
                "Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок.");
        CompanySection companySection = new CompanySection(company);
        r.addSection(SectionType.EXPERIENCE, companySection);

        company = new Company("Wrike", "https://www.wrike.com/",
                LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 1),
                "Старший разработчик (backend)",
                "Проектирование и разработка онлайн платформы управления проектами Wrike.");
        companySection.addItem(company);
        r.addSection(SectionType.EXPERIENCE, companySection);

        //EDUCATION("Образование")
        company = new Company("Coursera", "https://www.coursera.org/course/progfun",
                LocalDate.of(2013, 3, 1), LocalDate.of(2013, 5, 1),
                "Functional Programming Principles in Scala' by Martin Odersky",
                "");
        companySection = new CompanySection(company);
        r.addSection(SectionType.EDUCATION, companySection);

        company = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "http://www.ifmo.ru/",
                LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 1),
                "Аспирантура (программист С, С++)", "");
        company.addPeriods(LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 1),
                "Инженер (программист Fortran, C)", "");
        companySection.addItem(company);
        r.addSection(SectionType.EDUCATION, companySection);

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

        List<String> items;
        List<Company> companies;

        List<Period> periods;
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
                    for (Period period : periods) {
                        System.out.println("\t" + period.getBeginDate() + "\t" + period.getEndDate() + "\t" + period.getTitle());
                        description = period.getDescription();
                        if (!description.isEmpty()) {
                            System.out.println("\t\t" + period.getDescription());
                        }
                    }
                }
            }
//            for (AbstractSection section : sectionList) {
//                list = section.getItems();
//                for (Object line : list) {
//                    if (line.getClass() == Company.class) {
//                        company = (Company) line;
//                        System.out.println(company.getName() + "(" + company.getUrl() + ")");
//                        periods = company.getPeriods();
//                        for (Period period : periods) {
//                            System.out.println("\t" + period.getBeginDate() + "\t" + period.getEndDate() + "\t" + period.getTitle());
//                            description = period.getDescription();
//                            if (!description.isEmpty()) {
//                                System.out.println("\t\t" + period.getDescription());
//                            }
//                        }
//                    } else {
//                        System.out.println(line);
//                    }
//                }
//            }
            System.out.println();
        }
        System.out.println("----------------");
    }
}
