<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h1>An Error Occurred</h1>
    <p><%= request.getAttribute("error") %></p>
    <a href="index.jsp"><input type="submit" value="Back"></a>
</body>
</html>