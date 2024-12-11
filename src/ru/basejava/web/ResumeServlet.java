package ru.basejava.web;

import ru.basejava.Config;
import ru.basejava.model.*;
import ru.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ResumeServlet extends HttpServlet {
    private final Storage storage = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r = null;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> r = storage.get(uuid);
            case "add" -> r = new Resume();
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.setAttribute("action", action);
        request.getRequestDispatcher(
                "view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doPost(request);
        response.sendRedirect("resume");
    }

    private void doPost(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        String fullName = condense(request.getParameter("fullName"));
        if (fullName.length() == 0) {
            return;
        }
        Resume r = uuid.isEmpty() ? new Resume(fullName) : storage.get(uuid);
        r.setFullName(fullName);
        setContacts(r, request);
        setSections(r, request);

        if (uuid.isEmpty()) {
            storage.save(r);
        } else {
            storage.update(r);
        }
    }

    private void setContacts(Resume r, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = condense(request.getParameter(type.name()));
            if (value.length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void setSections(Resume r, HttpServletRequest request) {
        for (SectionType sectionType : SectionType.values()) {
            String sectionName = sectionType.name();
            String content = trim(request.getParameter(sectionName));

            if (content.length() != 0) {
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> {
                        content = condense(content);
                        if (content.length() != 0) {
                            r.addSection(sectionType, new TextSection(content));
                            continue;
                        }
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> list = getList(content);
                        if (list.size() > 0) {
                            r.addSection(sectionType, new ListSection(list));
                            continue;
                        }
                    }
                    case EXPERIENCE, EDUCATION -> {
                        String[] contentName = request.getParameterValues(sectionName);
                        String[] contentUrl = request.getParameterValues(sectionName + "url");
                        String[] contentStart = request.getParameterValues(sectionName + "start");
                        String[] contentEnd = request.getParameterValues(sectionName + "end");
                        String[] contentTitle = request.getParameterValues(sectionName + "title");
                        String[] contentDescr = request.getParameterValues(sectionName + "descr");
                        List<Company> companies = new ArrayList<>();
                        for (int i = 0; i < contentName.length; i++) {
                            String companyName = condense(contentName[i]);
                            if (!companyName.isEmpty()) {
                                Company.Period period = new Company.Period(contentStart[i], contentEnd[i], contentTitle[i], contentDescr[i]);
                                Company company = new Company(companyName, contentUrl[i], period);
                                companies.add(company);
                            }
                        }
                        r.addSection(sectionType, new CompanySection(companies));
                        continue;
                    }
                }
            }
            r.getSections().remove(sectionType);
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private String condense(String value) {
        String result = trim(value).replaceAll("[\r\n]", "")
                .replaceAll("\s+", " ");
        return result.equals(" ") ? "" : result;
    }

    private List<String> getList(String value) {
        return stream(value.split("\n"))
                .map(this::condense)
                .filter(item -> item.length() > 0)
                .collect(Collectors.toList());
    }
}
