<p id="loginstatus">
    <% if (session.getAttribute("auth.user") != null) { %>
    <img src="/images/userclass_16.png" alt="user"/>
    <a href="/users/<%= session.getAttribute("auth.user") %>"><%= session.getAttribute("auth.user") %></a>
    <a href="/logout.jsp"><img src="/images/tool_logout.png" alt="Login"/></a> <a href="/logout.jsp">Log out</a>

    <% } else { %>
    <a href="/login.jsp"><img src="/images/tool_login.png" alt="Login"/></a> <a href="/login.jsp">Login</a>
    <% } %>
</p>