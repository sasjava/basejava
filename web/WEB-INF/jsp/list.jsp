<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.basejava.model.ContactType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/theme/light.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/resume-list-styles.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="scrollable-panel">
    <div class="table-wrapper">
        <div class="add-resume">
            <a class="no-underline-anchor" href="resume?action=add"><img src="img/add.png" alt=""></a>
            <a class="text-anchor" href="resume?action=add"><p class="add-resume-title">Добавить резюме</p></a>
        </div>
        <div class="resumes-list">
            <table>
                <tr class="t-header">
                    <th class="name-column">Имя</th>
                    <th class="info-column">E-mail</th>
                    <th class="img-column">Редактировать</th>
                    <th class="img-column">Удалить</th>
                </tr>
                <c:forEach items="${resumes}" var="resume">
                    <%-- <% for (Resume resume : (List<Resume>) request.getAttribute("resumes")) { %>--%>
                    <jsp:useBean id="resume" type="ru.basejava.model.Resume"/>
                    <tr class="t-body">
                        <td class="name-column"><a href=" resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a>
                        </td>
                        <td class="info-column"><%=ContactType.MAIL.toHtmlNoTitle(resume.getContact(ContactType.MAIL))%>
                        </td>
                        <td class="img-column"><a href="resume?uuid=${resume.uuid}&action=edit"><img
                                src="img/pencil.png" alt=""></a></td>
                        <td class="img-column"><a href="resume?uuid=${resume.uuid}&action=delete"><img
                                src="img/delete.png" alt=""></a></td>
                    </tr>
                </c:forEach>
                <%-- <% } %>--%>
            </table>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
