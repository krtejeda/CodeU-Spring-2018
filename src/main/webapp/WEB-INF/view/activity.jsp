<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="codeu.model.data.user.User" %>
<%
List<Object> activity = (List<Object>) request.getAttribute("activity");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Activity</title>
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
  </style>

  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
  <link rel="stylesheet" href="/css/bootstrap.css">
</head>


<body>
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
        <li class="nav-item">
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

        <li class="nav-item active">
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
    <h2 class="display-4">Activity <a href="" style="float: right">&#8635;</a> </h2>
    <p class="lead">Here's everything that's happened on the site so far!</p>
    <div class="form-group" id="chat">
      <ul>
        <%
          for (Object item : activity) {
        %>
          <%
            if (item.getClass() == Conversation.class) {
              Conversation conversation = (Conversation) item;
              User userOfConversation = UserStore.getInstance()
                      .getUser(conversation.getOwnerId());
              if (userOfConversation != null) {
                  String author = userOfConversation.getName();
                  String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
                    .withZone(TimeZone.getDefault().toZoneId())
                    .format(conversation.getCreationTime());
              %>
              <li>
                <strong><%= creationTime %>:</strong> <%= author %> created a new conversation:
                  <a href="/chat/<%=conversation.getTitle()%>"> <%= conversation.getTitle() %></a>
              </li>
              <%
              }
            } else if (item.getClass() == User.class) {
                User userOfUser = (User) item;
                if (userOfUser != null) {
                  String author = userOfUser.getName();
                  String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
                    .withZone(TimeZone.getDefault().toZoneId())
                    .format(userOfUser.getCreationTime());
              %>
              <li>
                <strong><%= creationTime %>:</strong> <%= author %> joined!
              </li>
              <%
              }
            } else if (item.getClass() == Message.class) {
                Message message = (Message) item;
                User userOfMessage = UserStore.getInstance().getUser(message.getAuthorId());
                if (userOfMessage != null) {
                  String author = userOfMessage.getName();
                  String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
                    .withZone(TimeZone.getDefault().toZoneId())
                    .format(message.getCreationTime());
                  String conversation = ConversationStore.getInstance()
                    .getConversationByUUID(message.getConversationId()).getTitle();
              %>
              <li>
                <strong><%= creationTime %>:</strong> <%= author %> sent a message
                  in <a href="/chat/<%=conversation%>"> <%= conversation %></a>: "<%= message.getContent() %>"
              </li>
          <%  }
            } %>
        <% } %>
      </ul>
    </div>
    <hr/>
  </div>
</body>
</html>
