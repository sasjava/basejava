package ru.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.basejava.model.AbstractSection;

import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;

public class JsonParser {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractSection.class, new JsonSectionAdapter())
            .registerTypeAdapter(LocalDate.class, new JsonLocalDateTypeAdapter())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }

    public static <T> T read(String content, Class<T> clas) {
        return GSON.fromJson(content, clas);
    }

    public static <T> String write(T object) {
        return GSON.toJson(object);
    }
    public static <T> String write(T object, Class<T> clas) {
        return GSON.toJson(object, clas);
    }
}