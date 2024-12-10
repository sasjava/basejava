<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.basejava.model.ContactType" %>
<%@ page import="ru.basejava.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/theme/light.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/edit-resume-styles.css">
    <jsp:useBean id="resume" type="ru.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <%--<div>--%>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <div class="scrollable-panel">
            <div class="form-wrapper">
                <div class="button-section">
                    <button class="green-submit-button" type="submit">Сохранить</button>
                    <button class="red-cancel-button" type="reset" onclick="window.history.back()">Отменить</button>
                </div>
                <dl>
                    <dt><h3>Имя</h3></dt>
                    <dd><input class="field" type="text" name="fullName" placeholder="ФИО" size=50 value="${resume.fullName}"></dd>
                </dl>
                <%-- <h3>Контакты:</h3>--%>
                <c:forEach var="type" items="<%=ContactType.values()%>">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input class="field" type="text" name="${type.name()}" size=30
                                   value="${resume.getContact(type)}"></dd>
                    </dl>
                </c:forEach>
                <div class="spacer"></div>
                <!-- <h3>Секции:</h3>-->
                <c:forEach var="type" items="<%=SectionType.values()%>">
                    <jsp:include page="fragments/section.jsp">
                        <jsp:param name="stype" value="${type.name()}"/>
                    </jsp:include>
                </c:forEach>
                <div class="spacer"></div>
            </div>
        </div>
    </form>
    <%--</div>--%>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
