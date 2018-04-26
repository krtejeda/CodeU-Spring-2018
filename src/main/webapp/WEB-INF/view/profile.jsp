<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.user.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%
List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
List<Message> messages = (List<Message>) request.getAttribute("messages");
User owner = (User) request.getAttribute("owner");
%>

<!DOCTYPE html>
<html>
<head>
    <title><%=owner.getName() %>'s Profile</title>
    <link rel="stylesheet" href="/css/main.css">
    <style>
        label {
            display: inline-block;
            width: 100px;
        }

        #chat {
            background-color: white;
            height: 500px;
            overflow-y: scroll
        }

        textarea {
            width:100%;
            font-size: 18px;
        }
    </style>

    <script>
      // scroll the chat div to the bottom
      function scrollChat() {
        var chatDiv = document.getElementById('chat');
        chatDiv.scrollTop = chatDiv.scrollHeight;
      };
    </script>
</head>
<body onload="scrollChat()">
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
        <%
            if (user != null) {
                String ownerName = owner.getName();
                boolean isMyProfile = ownerName.equals(user);
        %>
            <%-- show profile --%>
            <h1>
                <%= isMyProfile ? "My " : ownerName + "'s " %>Profile Page
            </h1>

            <hr/>

            <%-- description --%>
            <h2>About <%= isMyProfile ? "Me" : ownerName %></h2>
            <div><%=owner.getDescription()%></div>

            <% if (isMyProfile) { %>
                <h3>Edit your About Me (only you can see this)</h3>
                <form action="/profile/<%= owner.getName() %>" method="POST">
                    <textarea
                        name="description"
                        cols="100"
                        rows="3"
                    ><%= owner.getDescription() %></textarea>
                    <br/>
                    <div style="overflow: hidden">
                        <button type="submit" style="float: left">Submit</button>
                        <% if(request.getAttribute("updateDescriptionError") != null) { %>
                            <div style="color:red; padding-left: 85px;">
                                <%= request.getAttribute("updateDescriptionError") %>
                            </div>
                        <% } %>
                    </div>
                </form>
            <% } %>

            <hr/>

            <%-- conversations --%>
            <h2>Conversations <%= isMyProfile ? "I'm" : ownerName + "'s" %> in</h2>
            <div>
                <ul>
                    <%
                        for (Conversation conversation : conversations) {
                            String title = conversation.getTitle();
                    %>
                        <li><a href="/chat/<%=title %>"><%= title %></a></li>
                    <%
                        }
                    %>
                </ul>
            </div>

            <hr/>

            <%-- messages --%>
            <h2><%= isMyProfile ? "My" : ownerName + "'s" %> Sent Messages</h2>
            <div id="chat">
                <ul>
                    <%
                        for (Message message : messages) {
                            String creationTime =
                                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
                                    .withZone(ZoneId.systemDefault())
                                    .format(message.getCreationTime());
                    %>
                        <li>
                            <strong><%= creationTime %>:</strong>
                            <%= message.getContent() %></li>
                    <%
                        }
                    %>
                </ul>
            </div>

            <hr/>

        <% } else { %>
            <%-- does not show profile --%>
            <div>
                <br/><br/>
                Please <a href="/login">login</a> to see this profile page
            </div>
        <% } %>
    </div>
</body>
</html>
