    <%=SQLHelper.executeXMLResult("SELECT * FROM entry_security ORDER BY title;")%>
    <%@ page import="com.justjournal.utility.SQLHelper" %>
    <%@ page contentType="text/xml;charset=UTF-8" language="java" %>