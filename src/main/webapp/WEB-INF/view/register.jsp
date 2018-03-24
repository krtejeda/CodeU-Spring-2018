<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    lable {
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
    <h1>Register</h1>

    <% if(request.getAttribute("error")!=null) { %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/register" method="POST">
      <label for="username">Username: </label>
      <input type="text" name="username" id="username">
      <br/>
      <label for="password">Password: </label>
      <input type="password" name ="password" id="password">
      <br/><br/>
      <button type="submit">Submit</button>
    </form>
  </div>
</body>
</html>
