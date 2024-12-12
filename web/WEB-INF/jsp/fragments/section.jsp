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
        case "EXPERIENCE":
        case "EDUCATION": {
            resume.addSection(sectionType, new CompanySection());
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
                <%int num = 0;%>
                <c:forEach var="company" items="${companies}">
                    <c:set var="num" value="<%=++num%>"></c:set>
                    <jsp:include page="company_edit.jsp">
                        <jsp:param name="name" value="${company.name}"/>
                        <jsp:param name="url" value="${company.url}"/>
                        <jsp:param name="num" value="${num}"/>
                    </jsp:include>
                    <c:forEach var="period" items="${company.periods}">
                        <jsp:include page="company_period_edit.jsp">
                            <jsp:param name="start" value="${period.dateAsMMYYYY(period.beginDate)}"/>
                            <jsp:param name="end" value="${period.dateAsMMYYYY(period.endDate)}"/>
                            <jsp:param name="title" value="${period.title}"/>
                            <jsp:param name="description" value="${period.description}"/>
                            <jsp:param name="num" value="${num}"/>
                        </jsp:include>
                    </c:forEach>
                    <jsp:include page="company_period_edit.jsp">
                        <jsp:param name="start" value=""/>
                        <jsp:param name="end" value=""/>
                        <jsp:param name="title" value=""/>
                        <jsp:param name="description" value=""/>
                        <jsp:param name="num" value="${num}"/>
                    </jsp:include>
                    <div class="spacer"></div>
                </c:forEach><br>
                <c:set var="num" value="<%=++num%>"></c:set>
                <jsp:include page="company_edit.jsp">
                    <jsp:param name="name" value=""/>
                    <jsp:param name="url" value=""/>
                </jsp:include>
                <jsp:include page="company_period_edit.jsp">
                    <jsp:param name="start" value=""/>
                    <jsp:param name="end" value=""/>
                    <jsp:param name="title" value=""/>
                    <jsp:param name="description" value=""/>
                    <jsp:param name="num" value="${num}"/>
                </jsp:include>
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
                        <c:set var="href" value="${company.url}"></c:set>
                        <c:if test="${href==\"\"}" >
                            <c:set var="href" value="javascript:void(0)"></c:set>
                        </c:if>
                        <div class="job-name"><a class="contact-link" href="${href}" >${company.name}</a></div>
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

