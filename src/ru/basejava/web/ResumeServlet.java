package ru.basejava.web;

import ru.basejava.Config;
import ru.basejava.model.Resume;
import ru.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
        final String CS = "UTF-8";
        request.setCharacterEncoding(CS);
        response.setCharacterEncoding(CS);
        response.setContentType("text/html; charset=" + CS);

        Writer w = response.getWriter();
//        String name = request.getParameter("name");
//        w.write(name == null ? "Привет!!!" : "Hello," + name + "!");

        w.write("<html>");
        w.write("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + CS + "\">");
        String id = request.getParameter("uuid");
        if (id == null) {
            w.write("<h2>Resumes</h2></head><body>");
            w.write("<table border=1 cellpadding=8 cellspacing=0><th>uuid</th><th>name</th>");
            List<Resume> resumes = storage.getAllSorted();
            for (Resume r : resumes) {
                String uuid = r.getUuid();
                w.write("<tr><td><a href =/resumes/resume?uuid=" + uuid + ">" + uuid + "</a></td><td>" + r.getFullName() + "</td></tr>");
            }
        } else {
            w.write("<head><h3>Resume</h3></head><body>");
            w.write("<p><a href=\"javascript:history.back()\"><-- Назад</a></p>");
            w.write("<table border=1 cellpadding=8 cellspacing=0><th>id</th><th>name</th>");
            Resume r = storage.get(id);
            w.write("<tr><td>" + r.getUuid() + "</td><td>" + r.getFullName() + "</td></tr>");
        }
        w.write("</table></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
