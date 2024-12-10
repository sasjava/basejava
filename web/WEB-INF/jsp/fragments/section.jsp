<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.basejava.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="resume" type="ru.basejava.model.Resume" scope="request"/>

<c:set var="stype" value="${param.stype}"/>
<jsp:useBean id="stype" type="java.lang.String"/>

<c:set var="section" value="${resume.getSection(stype)}"/>
<c:if test="${section==null}"><%
    SectionType sectionType = SectionType.valueOf(stype);
    switch (stype) {
        case "OBJECTIVE":
        case "PERSONAL": {
            resume.addSection(sectionType, new TextSection(""));
            break; }
        case "ACHIEVEMENT":
        case "QUALIFICATIONS": {
            resume.addSection(sectionType, new ListSection(""));
            break; }
        default: return;
    }%>
    <c:set var="section" value="${resume.getSection(stype)}"/>
</c:if>
<jsp:useBean id="section" type="ru.basejava.model.AbstractSection"/>

<div class="section"><%=SectionType.valueOf(stype).getTitle()%></div>
<c:choose>
    <c:when test="${action==\"edit\"||action==\"add\"}">
        <c:choose>
            <c:when test="${stype==\"OBJECTIVE\"||stype==\"PERSONAL\"}">
                <textarea class="field" name="${stype}"><%=((TextSection) section).getContent()%></textarea>
            </c:when>
            <c:when test="${stype==\"ACHIEVEMENT\"||stype==\"QUALIFICATIONS\"}">
                <textarea class="field" name="${stype}"><%
                    request.setAttribute("list", ((ListSection) section).getItems());
                %><c:forEach var="item" items="${list}">${item}&#13;&#10;</c:forEach></textarea>
            </c:when>
            <c:when test="${stype==\"EXPERIENCE\"||stype==\"EDUCATION\"}">
                <%request.setAttribute("companies", ((CompanySection) section).getCompanies());%>
                <c:forEach var="company" items="${companies}">
                    <p>
                    <input class="field" type="text" name="${stype}" placeholder="Название компании" value="${company.name}">
                    <input class="field" type="text" name="${stype}+url" placeholder="Ссылка" value="${company.url}">
                    <c:forEach var="period" items="${company.periods}">
                        <div class="date-section">
                            <input class="field date" name="${stype}+startDate" placeholder="Начало, ММ/ГГГГ" value="${period.getMonthYear(period.beginDate)}">
                            <input class="field date date-margin" name="${stype}+endDate" placeholder="Окончание, ММ/ГГГГ" value="${period.getMonthYear(period.endDate)}">
                        </div>
                        <input class="field" type="text" name="${stype}title" placeholder="Заголовок" value="${period.title}">
                        <textarea class="field" type="text" name="${stype}descr" placeholder="Описание">${period.description}</textarea>
                        <br>
                    </c:forEach>
                    <br>
                    </p>
                </c:forEach>
<%--                <textarea><%%></textarea>--%>
            </c:when>
        </c:choose>
    </c:when>
    <c:otherwise> <!--View-->
        <c:choose>
            <c:when test="${stype==\"OBJECTIVE\"||stype==\"PERSONAL\"}">
                <div class="qualities"><%=((TextSection) section).getContent()%>
                </div>
            </c:when>
            <c:when test="${stype==\"ACHIEVEMENT\"||stype==\"QUALIFICATIONS\"}">
                <%request.setAttribute("list", ((ListSection) section).getItems());%>
                <ul class="list">
                    <c:forEach var="item" items="${list}">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:when test="${stype==\"EXPERIENCE\"||stype==\"EDUCATION\"}">
                <%request.setAttribute("companies", ((CompanySection) section).getCompanies());%>
                    <c:forEach var="company" items="${companies}">
                        <div class="job-name"><a class="contact-link" href="${company.url}">${company.name}</a></div>
                        <c:forEach var="period" items="${company.periods}">
                            <jsp:useBean id="period" type="ru.basejava.model.Company.Period"/>
                            <div class="period-position">
                              <div class="period">${period.getPeriodMonthYear()}</div>
                              <div class="position">${period.title}</div>
                            </div>
                            <div class="description">${period.description}</div>
                        </c:forEach>
                    </c:forEach>
            </c:when>
<%--            <c:otherwise><%=JsonParser.write(section, AbstractSection.class)%></c:otherwise>--%>
        </c:choose>
    </c:otherwise>
</c:choose>
</h3>

