<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.ChatbotStore" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="org.joda.time.DateTimeComparator" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <%--<style>--%>
    <%--#chat {--%>
      <%--background-color: white;--%>
      <%--color: black;--%>
      <%--padding-top: 2%;--%>
      <%--height: 500px;--%>
      <%--overflow-y: scroll--%>
    <%--}--%>
  <%--</style>--%>

  <%--<script>--%>
    <%--// scroll the chat div to the bottom--%>
    <%--function scrollChat() {--%>
      <%--var chatDiv = document.getElementById('chat');--%>
      <%--chatDiv.scrollTop = chatDiv.scrollHeight;--%>
    <%--};--%>
  <%--</script>--%>

  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
  <script src="/js/chat.js"></script>
  <link rel="stylesheet" href="/css/chat.css" type="text/css">
  <link rel="stylesheet" href="/css/bootstrap.css">

  <script>
    function scrollToBottom() {
      console.log("scrolling down");
      var chatMessages = $('#chat-messages');
      chatMessages.scrollTop(chatMessages.prop("scrollHeight"));
    }
    window.onload = scrollToBottom;
  </script>
</head>
<%--<body onload="scrollChat()">--%>
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

    <h2 class="display-4"><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h2>

    <hr/>

    <div id="chat-messages">
      <%
        Date dateOfPrevMessage = new Date();
        for (Message message : messages) {
          UUID authorId = message.getAuthorId();
          UserStore userStore = UserStore.getInstance();
          ChatbotStore chatbotStore = ChatbotStore.getInstance();
          String author;

          // time sent the message
          long secondsTilNow =
                  Instant.now().getEpochSecond() - message.getCreationTime().getEpochSecond();
          long minutesTilNow = TimeUnit.SECONDS.toMinutes(secondsTilNow);
          long hoursTilNow = TimeUnit.SECONDS.toHours(secondsTilNow);
          long daysTilNow = TimeUnit.SECONDS.toDays(secondsTilNow);
          long monthsTilNow = daysTilNow/30;
          long yearsTilNow = daysTilNow/365;
          String timeTilNow =
            yearsTilNow > 0 ? yearsTilNow + "y" :
            monthsTilNow > 0 ? monthsTilNow + "mo" :
            daysTilNow > 0 ? daysTilNow + "d" :
            hoursTilNow > 0 ? hoursTilNow + "h" :
            minutesTilNow > 0 ? minutesTilNow + "m" :
            secondsTilNow + "s";

          // date
          Date creationDate = Date.from(message.getCreationTime());
          String formattedDate = new SimpleDateFormat("EEEE, MMMM dd").format(creationDate);
          boolean showDateLabel =
            DateTimeComparator.getDateOnlyInstance().compare(
              dateOfPrevMessage, creationDate) != 0;
          if (userStore.isUserRegistered(authorId)) {
            author = userStore.getUser(authorId).getName();
            %>
              <div class="message">
                <%
                  if (showDateLabel)
                  {
                %>
                  <label><%= formattedDate %></label>
                <%
                  }
                %>
                <span class="username"
                ><strong><a href="/profile/<%=author %>"><%= author %>:</a></strong></span>
                <img src="../../images/profile-pictures/thanos.jpg" />
                <div class="bubble"
                  ><%= message.getContent() %><div class="corner"></div><span><%=
                    timeTilNow %></span></div>
              </div>
            <%
          } else if (chatbotStore.isChatbot(authorId)) {
            author = chatbotStore.getChatbot(authorId).getName();
            %>
              <div class="message">
                <%
                  if (showDateLabel)
                  {
                %>
                <label><%= formattedDate %></label>
                <%
                  }
                %>
                <span class="username"
                ><strong><a href="/profile/<%=author %>"><%= author %>:</a></strong></span>
                <img src="../../images/profile-pictures/vision.jpg" />
                <div class="bubble"
                  ><%= message.getContent() %><div class="corner"></div><span><%=
                    timeTilNow %></span></div>
              </div>
            <%
          }
          dateOfPrevMessage = creationDate;
        }
      %>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text"
               name="message"
               class="form-control"
               placeholder="Your message goes here"
               style="margin-bottom: 10px;">
        <button type="submit"
                class="btn btn-primary">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
