package ru.basejava.model;

public enum ContactType {
    PHONE("Тел."),
    SKYPE("Skype"),
    MAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");
    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtmlVal(value, title);
    }

    public String toHtmlNoTitle(String value) {
        return (value == null) ? "" : toHtmlVal(value, null);
    }

    protected String toHtmlVal(String value, String title) {
        String t = title == null ? "" : title + ": ";
        return switch (this.name()) {
            case "SKYPE" -> t + asLink(value, "skype");
            case "MAIL" -> t + asLink(value, "mailto");
            case "LINKEDIN", "GITHUB", "STACKOVERFLOW", "HOMEPAGE" -> asLink(value, null);
            default -> t + value;
        };
    }

    private String asLink(String value, String type) {
        String p = type == null ? "" : type + ":";
        String t = type == null ? title : value;
        return "<a class=\"contact-link\" href=\"" + p + value + "\">" + t + "</a>";
    }
}
