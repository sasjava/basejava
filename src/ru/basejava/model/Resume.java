package ru.basejava.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {
    // Unique identifier
    private final String uuid;
    private String fullName;

    private final Map<ContactType, String> contacts = new HashMap<>();
    private final Map<SectionType, List<AbstractSection>> sections = new HashMap<>();

    public Resume(String fullName) {
        this.uuid = UUID.randomUUID().toString();
        this.fullName = fullName;
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);
        if (result == 0) {
            result = uuid.compareTo(o.uuid);
        }
        return result;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void addContact(ContactType contactType, String value) {
        this.contacts.put(contactType, value);
    }

    public void addSection(SectionType sectionType, AbstractSection section) {
        List<AbstractSection> sectionList = this.sections.get(sectionType);
        if (sectionList == null) {
            sectionList = new ArrayList<>();
        }
        sectionList.add(section);

        this.sections.put(sectionType, sectionList);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, List<AbstractSection>> getSections() {
        return sections;
    }
}
