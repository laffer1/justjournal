<%=SQLHelper.executeXMLResult("SELECT * FROM user_link where id='" + new Integer(request.getParameter("userid")) + "' order by title;")%>
<%@ page import="com.justjournal.db.SQLHelper" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>