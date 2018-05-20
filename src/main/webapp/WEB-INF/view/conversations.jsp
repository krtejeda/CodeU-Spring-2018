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
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">

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

        <li class="nav-item active">
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

    <% if(request.getAttribute("error") != null){ %>
        <h2 class="display-4"
            style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h3 class="display-6">New Conversation</h3>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label lead">Title:</label>
          <input type="text"
                 name="conversationTitle"
                 class="form-control"
                 placeholder="New conversation title"
                 style="margin-left: 10px;
                        width: 40%;
                        display: inline;">
        </div>

        <button type="submit"
                class="btn btn-outline-primary">Create</button>
      </form>

      <hr/>
    <% } %>

    <h1 class="display-4">Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation : conversations){
    %>
      <li class="lead"><a href="/chat/<%= conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>
