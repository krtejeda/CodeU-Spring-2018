<!DOCTYPE html>
<html>
<head>
  <title>Forget Password?</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    label {
      display: inline-block;
      width: 130px;
    }
  </style>
</head>
<body>
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/login">Login</a>
    <a href="/register">Register</a>
  </nav>

  <div id="container">
    <h1>Reset Password</h1>
    <% if(request.getAttribute("error") != null) { %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/forgetpassword" method="POST">
      <label for="username">Username: </label>
      <input type="text" name="username" id="username">
      <br/>
      <label for="password">New password: </label>
      <input type="password" name="password" id="password">
      <br/>
      <br/>
      <button type="submit">Submit</button>
    </form>
  </div>
</body>
</html>
