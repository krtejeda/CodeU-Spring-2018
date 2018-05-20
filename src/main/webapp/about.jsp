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
<!DOCTYPE html>
<html>
<head>
  <title>The Avengers</title>
  <link rel="stylesheet" href="/css/main.css">

  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
  <link rel="stylesheet" href="/css/bootstrap.css">
</head>
<body>
  <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark">
    <a id="navTitle" class="navbar-brand" href="/">CodeU Chat App</a>
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

        <li class="nav-item active">
          <a class="nav-link"
             href="/about.jsp">About <span class="sr-only">(current)</span></a>
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
      <h2 class="display-4">About Us</h2>
      <h2 class="display5">The Avengers: CodeU Spring 2018 Team 27</h2>
      <p>
        We are a group of Kevin Rugg and four college students, who apparently
        are fans of The Avengers and can't wait to see the new movies.
        Although we are at different parts of the world, we came together to
        make chat apps great again by using memes to communicate.
        We believe that people express themselves best by showing memes of their
        choices to their friends. So why bother tagging friends in memes when
        you have a chat app specifically designed for commemenication.
        Once again, memes are great and chat apps are awesome. Who wouldn't
        want them both?
      </p>

      <h2>Bios</h2>

      <div>
        <img src="./images/members/Kevin.jpg" class="profileImage" align="top">
        <p>
          <strong> Kevin Rugg - PA:</strong>
          I've been coding at Google for ~8 years, almost all in Java.
          10 companies before Google, mostly small startups.  I grew up in Montana
          and still enjoy the outdoors, but find coding to be a much more enjoyable
          way to make a living than farming.  I have a 13 year old daughter who is
          also learning to code using github and Google AppEngine (although python).
          This is my first time helping out on CodeU. My favorite Avenger is probably
          Iron Man (seems like a popular choice).
        </p>
      </div>

      <div>
        <img src="./images/members/Alexandra_Lopez.jpg" class="profileImage" align="top">
        <p>
          <strong>Alexandra (Ale) Lopez:</strong> I am a sophomore
          studying computer science at the University of Notre Dame.
          I am from San Juan, Puerto Rico. I love to dance and I am very
          passionate about community service. My favorite Avenger is Iron Man.
        </p>
      </div>

       <div>
        <img src="./images/members/Elle.jpg" class="profileImage" align="top">
        <p>
          <strong>Elle Tojaroon:</strong> I am from Bangkok, Thailand, and
          am a third year senior, studying computer science at
          Cornell University. In my free time, I try to go to the gym and
          be healthy. Unfortunately, that does not always happen.
          I love to walk and run. When the weather is nice,
          I go hiking and exploring the city. I also love to watch
          TV shows and listen to audiobooks. On the weekend, I try out
          new restaurants and fancy-looking dessert places. My favorite Avenger
          is Iron Man.
        </p>
      </div>

      <div>
        <img src="./images/members/Kelvin.jpg" class="profileImage" align="top">
        <p>
          <strong>Kelvin Tejeda:</strong> I'm a sophomore at Williams College.
          I like to spend my time in nature, wearing nice shoes and eating
          honey buns. I'm super excited to work with you guys on this project
          and I'm even more excited to learn a ton of skills that I'll be
          able to carry with me throughout my career in computer science.
          My favorite Avenger is Hulk's pants.
        </p>
      </div>

      <div>
        <img src="./images/members/my.jpg" class="profileImage" align="top">
        <p>
          <strong>My Nguyen:</strong>  I'm a sophomore at Bryn Mawr College.
          I love traveling, sleeping, learning different languages and
          listening to music (especially Kpop or Ghibli soundtracks!).
          My favorite Avenger is Iron Man.
        </p>
      </div>

  </div>
</body>
</html>
