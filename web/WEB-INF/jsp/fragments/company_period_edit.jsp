<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="date-section">
    <input class="field date" name="${param.stype}start${param.num}" placeholder="Начало, ММ/ГГГГ" value="${param.start}">
    <input class="field date date-margin" name="${param.stype}end${param.num}" placeholder="Окончание, ММ/ГГГГ" value="${param.end}">
</div>
<input class="field" type="text" name="${param.stype}title${param.num}" placeholder="Заголовок" value="${param.title}">
<textarea class="field" name="${param.stype}descr${param.num}" placeholder="Описание">${param.description}</textarea>
<br>