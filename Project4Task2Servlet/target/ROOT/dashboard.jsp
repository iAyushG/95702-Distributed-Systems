<%@ page import="java.util.List" %>
<%@ page import="org.bson.Document" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>F1 Data Operations Dashboard</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 5px;
            text-align: left;
        }
    </style>
</head>
<body>

<h1>F1 Data Analytics</h1>
<div id="analytics">
    <p>Total Requests: <%=request.getAttribute("totalRequests")%></p>
    <p>Success Rate: <%=request.getAttribute("successRate") %> %</p>
    <p>Most Popular Data Type: <%=request.getAttribute("mostPopularDataType")%></p>
</div>

<h2>Request Logs</h2>
<div id="logs">
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Timestamp</th>
            <th>Data Type</th>
            <th>API URL</th>
            <th>Status</th>
            <th>Response</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Document> logs = (List<Document>) request.getAttribute("logs");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (logs != null && !logs.isEmpty()) {
                for (Document log : logs) {
                    Date date = log.getDate("timestamp"); // Adjust according to actual field name if different
                    String formattedDate = sdf.format(date);
        %>
        <tr>
            <td><%= log.get("_id") %></td>
            <td><%= formattedDate %></td>
            <td><%= log.getString("dataType") %></td>
            <td><%= log.getString("apiUrl") %></td>
            <td><%= log.getString("status") %></td>
            <td><%= log.getString("response").substring(0, Math.min(log.getString("response").length(), 100)) + "..." %></td> <!-- Truncated response for readability -->
        </tr>
        <%
                }
            } else {
                out.println("<tr><td colspan='6'>No logs available.</td></tr>");
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>