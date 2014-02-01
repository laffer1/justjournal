<%
    if (session.getAttribute("auth.user") != null) {
%>
<p>You are logged in as
    <img src="../images/userclass_16.png" alt="user"/>
    <a href="${pageContext.request.contextPath}/users/<%= session.getAttribute("auth.user") %>"><%= session.getAttribute("auth.user") %>
    </a>.
    If you want to change <a href="index.jsp" title="Preferences">preferences</a> on another journal,
    <a href="../logout.jsp" title="Log Out">log out</a> first.</p>
<% } %>