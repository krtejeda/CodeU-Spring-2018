<%--
  Created by IntelliJ IDEA.
  User: EllesMacbook
  Date: 3/23/18
  Time: 11:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="/css/main.css">
    <style>
        label {
            display: inline-block;
            width: 100px;
        }
    </style>
</head>
<body>
    <nav>
        <a id="navTitle" href="/">CodeU Chat App</a>
        <a href="/conversations">Conversations</a>
        <% if (request.getSession().getAttribute("user") != null) { %>
            <a>Your Profile Page, <%= request.getSession().getAttribute("user") %>!</a>
        <% } else { %>
            <%-- TODO(Elle) use page owner's name from url in this placeholder --%>
            <a href="/login">Login</a>
        <% } %>
    </nav>

    <div id="container">
        <% if (request.getSession().getAttribute("user") != null) { %>
            <h1>About me</h1>
            <div>Some description</div>

            <%-- TODO(Elle) show list of all conversations this person is in --%>

        <% } else { %>
            <div>Please <a href="/login">login</a> to see this profile page!</div>
        <% } %>
    </div>
</body>
</html>
