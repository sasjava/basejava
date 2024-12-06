package ru.basejava.util;

import org.junit.Assert;
import org.junit.Test;
import ru.basejava.model.AbstractSection;
import ru.basejava.model.Resume;
import ru.basejava.model.TextSection;

import static ru.basejava.ResumeTestData.R1;

public class JsonParserTest {

    @Test
    public void testResume() {
        String json = JsonParser.write(R1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(R1, resume);
    }

    @Test
    public void write() {
        AbstractSection section1 = new TextSection("Ёксперт");
        String json = JsonParser.write(section1, AbstractSection.class);
        System.out.println(json);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assert.assertEquals(section1, section2);
    }
}