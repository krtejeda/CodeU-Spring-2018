<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.user.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.common.collect.ImmutableMap" %>
<%
List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
ImmutableMap<String, String> messageDisplayTimeToMessageContent =
    (ImmutableMap<String, String>) request.getAttribute("messageDisplayTimeToMessageContent");
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
            color: black;
            padding-top: 2%;
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

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
</head>
<body onload="scrollChat()">
    <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark">
        <a id="navTitle"
           class="navbar-brand"
           href="/">CodeU Chat App <span class="sr-only">(current)</span></a>
        <button class="navbar-toggler collapsed"
                type="button"
                data-toggle="collapse"
                data-target="#navbarColor01">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="navbar-collapse collapse" id="navbarColor01">
            <ul class="navbar-nav mr-auto">

                <li class="nav-item">
                    <a class="nav-link" href="/conversations">Conversations</a>
                </li>

                <%
                    Object user = request.getSession().getAttribute("user");
                    if (user!= null) {
                %>
                    <li class="nav-item active">
                        <a href="/profile/<%=user%>"><%= user %></a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link" href="/login">Login</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link"  href="/register">Register</a>
                    </li>
                <% } %>

                <li class="nav-item">
                    <a class="nav-link"  href="/about.jsp">About</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"  href="/activity">Activity</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link"  href="/testdata">Load Test Data</a>
                </li>

                <%
                    if (user != null) {
                %>
                <li class="nav-item">
                    <a class="nav-link"  href="/login">Logout</a>
                </li>
                <% } %>

            </ul>

        </div>
    </nav>

    <div class="jumbotron"
         style="width:75%; margin-left:auto; margin-right:auto; margin-top: 75px;">
        <%
            if (user != null) {
                String ownerName = owner.getName();
                boolean isMyProfile = ownerName.equals(user);
        %>
            <%-- show profile --%>
            <h2 class="display-4">
                <%= isMyProfile ? "My " : ownerName + "'s " %>Profile Page
            </h2>

            <hr/>

            <%-- description --%>
            <h3 class="display-5">About <%= isMyProfile ? "Me" : ownerName %></h3>
            <div><%=owner.getDescription()%></div>

            <br>

            <% if (isMyProfile) { %>
                <h4 class="display-6">Edit your About Me (only you can see this)</h4>
                <form action="/profile/<%= owner.getName() %>" method="POST">
                    <textarea
                        name="description"
                        cols="100"
                        rows="3"
                    ><%= owner.getDescription() %></textarea>
                    <br/>
                    <div style="overflow: hidden">
                        <button type="submit"
                                style="float: left"
                                class="btn btn-outline-primary">Submit</button>
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
            <h3 class="display-5">Conversations <%= isMyProfile ? "I'm" : ownerName + "'s" %> in</h3>
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
            <h3 class="display-5"><%= isMyProfile ? "My" : ownerName + "'s" %> Sent Messages</h3>
            <div id="chat">
                <ul>
                    <%
                        for (String displayTime : messageDisplayTimeToMessageContent.keySet()) {
                    %>
                        <li>
                            <strong><%= displayTime %>:</strong>
                            <%= messageDisplayTimeToMessageContent.get(displayTime) %></li>
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
