<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%
    List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
%>

<!DOCTYPE html>
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
        <%
            Object user = request.getSession().getAttribute("user");
            if (user!= null) {
        %>
            <a href="/profile/<%=user%>"><%= user %></a>
        <% } else { %>
            <a href="/login">Login</a>
            <a href="/register">Register</a>
        <% } %>
        <a href="/about.jsp">About</a>
        <a href="/testdata">Load Test Data</a>
    </nav>

    <div id="container">
        <% if (request.getSession().getAttribute("user") != null) { %>
            <h1>Hello, <%= request.getSession().getAttribute("user") %>!</h1>

            <h2>About me</h2>
            <div>Some description</div>

            <%-- TODO(Elle) show conversation link --%>
            <div id="chat">
                <ul>
                    <%
                        for (Conversation conversation : conversations) {
                    %>
                    <li><strong><%= conversation.getTitle() %>:</strong></li>
                    <%
                        }
                    %>
                </ul>
            </div>

            <%-- TODO(Elle) show list of all conversations this person is in --%>

        <% } else { %>
            <div>Please <a href="/login">login</a> to see this profile page!</div>
        <% } %>
    </div>
</body>
</html>
