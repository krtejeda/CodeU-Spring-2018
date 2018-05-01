<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.time.format.FormatStyle" %>
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
      height: 500px;
      overflow-y: scroll
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
    <a href="/activity">Activity</a>
    <a href="/testdata">Load Test Data</a>
    <%
      if (user != null) {
    %>
      <a href="/login">Logout</a>
    <% } %>
  </nav>

  <div id="container">
    <h1>Activity <a href="" style="float: right">&#8635;</a> </h1>
    <p>Here's everything that's happened on the site so far!</p>
    <div id="chat">
      <ul>
    <%
      for (Object item : activity) {
    %>
      <li>
      <%
        if (item.getClass() == Conversation.class) {
          Conversation conversation = (Conversation) item;
          String author = UserStore.getInstance()
            .getUser(conversation.getOwnerId()).getName();
          String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            .withZone(TimeZone.getDefault().toZoneId())
            .format(conversation.getCreationTime());
      %>
        <strong><%= creationTime %>:</strong> <%= author %> created a new conversation:
          <a href="/chat/<%=conversation.getTitle()%>"> <%= conversation.getTitle() %></a>
      <%
        } else if (item.getClass() == User.class) {
          User User = (User) item;
          String author = User.getName();
          String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            .withZone(TimeZone.getDefault().toZoneId())
            .format(User.getCreationTime());
      %>
        <strong><%= creationTime %>:</strong> <%= author %> joined!
      <%
        } else if (item.getClass() == Message.class) {
          Message message = (Message) item;
          String author = UserStore.getInstance()
            .getUser(message.getAuthorId()).getName();
          String creationTime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            .withZone(TimeZone.getDefault().toZoneId())
            .format(message.getCreationTime());
          String conversation = ConversationStore.getInstance()
            .getConversationByUUID(message.getConversationId()).getTitle();
      %>
        <strong><%= creationTime %>:</strong> <%= author %> sent a message
          in <a href="/chat/<%=conversation%>"> <%= conversation %></a>: "<%= message.getContent() %>"
      <% } %>
        </li>
    <% } %>
      </ul>
    </div>
    <hr/>
  </div>
</body>
</html>
