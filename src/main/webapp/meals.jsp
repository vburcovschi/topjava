<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table {
            border: 1px solid #b3adad;
            border-collapse: collapse;
            padding: 5px;
        }

        table th {
            border: 1px solid #b3adad;
            padding: 5px;
            background: #f0f0f0;
            color: #313030;
        }

        table td {
            border: 1px solid #b3adad;
            text-align: center;
            padding: 5px;
            background: #ffffff;
        }
    </style>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <a href="write.jsp">Add Meal</a>
    <br><br>
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="userMeal" items="${mealList}">
            <jsp:useBean id="userMeal" type="ru.javawebinar.topjava.model.UserMealWithExcess"/>
            <tr style="color:${userMeal.excess == 'true' ? 'red':'green'}">
                <td>
<%--                    <fmt:parseDate value="${ userMeal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                                   type="both"/>
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }"/>--%>
                    <%=TimeUtil.toString(userMeal.getDateTime())%>
                </td>
                <td>${userMeal.description}</td>
                <td>${userMeal.calories}</td>
                <td>
                    <a href="edit.jsp">Edit</a>
                </td>
                <td>
                    <a href=delete">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>