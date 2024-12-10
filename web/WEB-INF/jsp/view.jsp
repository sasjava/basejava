<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/theme/light.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/view-resume-styles.css">
    <jsp:useBean id="resume" type="ru.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="scrollable-panel">
    <div class="form-wrapper">
        <%--        <section>--%>
        <div class="full-name">${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a>
        </div>
        <%--    <h3>Контакты:</h3>--%>
        <div class="contacts">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<ru.basejava.model.ContactType, java.lang.String>"/>
                <div><%=contactEntry.getKey().toHtml(contactEntry.getValue())%>
                </div>
            </c:forEach>
        </div>
        <%-- <h3>Секции:</h3>--%>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.basejava.model.SectionType, ru.basejava.model.AbstractSection>"/>
            <%--        <h3><%=sectionEntry.getKey().getTitle()%></h3>--%>
            <jsp:include page="fragments/section.jsp">
                <jsp:param name="stype" value="<%=sectionEntry.getKey().name()%>"/>
            </jsp:include>
        </c:forEach>
        <%--        </section>--%>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

