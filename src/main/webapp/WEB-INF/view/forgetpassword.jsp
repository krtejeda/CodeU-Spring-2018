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
    <h2 class="display-4"
        style="color:red">Reset Password</h2>
    <% if(request.getAttribute("error") != null) { %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/forgetpassword" method="POST">
      <label for="username"
             class="form-control-label lead">Username: </label>
      <input type="text"
             name="username"
             id="username"
             class="form-control"
             placeholder="New conversation title"
             style="width: 40%;
                    display: inline;">
      <br/>
      <label for="password"
             class="form-control-label lead">New password: </label>
      <input type="password"
             name="password"
             id="password"
             class="form-control"
             placeholder="New conversation title"
             style="width: 40%;
                    display: inline;">
      <br/>
      <br/>
      <button type="submit"
              class="btn btn-danger">Reset Your Password</button>
    </form>
  </div>
</body>
</html>
