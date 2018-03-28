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
  </style>
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
			<a href="/register">Register</a>
    <% } %>
  </nav>

  <div id="container">
    <h1>Activity</h1>
    <p>Here's everything that's happened on the site so far!</p>

    <%-- TODO: add scrolling box with all activity --%>
  </div>
</body>
</html>
