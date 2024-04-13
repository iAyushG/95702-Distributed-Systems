<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.reflect.TypeToken"%>
<%@ page import="java.lang.reflect.Type"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Query Results</title>
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

    <%
    // Retrieve the option and JSON data string from the request
    String viewOption = (String) request.getAttribute("viewOption");
    String jsonData = (String) request.getAttribute("jsonData");

    // Instantiate Gson object
    Gson gson = new Gson();

    // Define the type of the data structure you are expecting
    Type type = new TypeToken<Map<String, Object>>(){}.getType();
    Map<String, Object> jsonMap = gson.fromJson(jsonData, type);
    %>

    <%
    // You will need to adjust the structure of your data parsing based on the viewOption
    Map<String, Object> MRData = (Map<String, Object>) jsonMap.get("MRData");
    switch (viewOption) {
        case "raceSchedule":
            Map<String, Object> raceTable = (Map<String, Object>) MRData.get("RaceTable");
            List<Map<String, Object>> races = (List<Map<String, Object>>) raceTable.get("Races");
            %>
            <h2>Query Results</h2>
            <h2>Race Schedule</h2>
            <h3>Season: <%= raceTable.get("season") %> </h3>
            <h4>Total Rounds: <%= MRData.get("total")%></h4>
            <table style="width:100%">
                <tr>
                    <th>Round</th>
                    <th>Race Name</th>
                    <th>Date</th>
                    <th>Circuit</th>
                    <th>About</th>
                </tr>
                <%
                    for (Map<String, Object> race : races) {
                        Map<String, Object> circuit = (Map<String, Object>) race.get("Circuit");
                        String raceName = (String) race.get("raceName");
                        String raceUrl = (String) race.get("url");
                        String circuitName = (String) circuit.get("circuitName");
                %>
                <tr>
                    <td><%= race.get("round") %></td>
                    <td><%= raceName %></td>
                    <td><%= race.get("date") %></td>
                    <td><%= circuitName %></td>
                    <td><a href="<%= raceUrl %>">More Information</a></td>
                </tr>
                <%
                    }
                %>
            </table>
        <%
            break;
        case "driverStandings":
            List<Map<String, Object>> standingsLists = (List<Map<String, Object>>) ((Map<String, Object>) MRData.get("StandingsTable")).get("StandingsLists");
            Map<String, Object> standings = standingsLists.get(0);
            List<Map<String, Object>> driverStandings = (List<Map<String, Object>>) standings.get("DriverStandings");
            %>
            <h2>Query Results</h2>
            <h2>Driver Standings</h2>
            <h3>Season: <%= standings.get("season") %> </h3>
            <h4>After Round: <%= standings.get("round")%></h4>
            <table style="width:100%">
                <tr>
                    <th>POS</th>
                    <th>Driver</th>
                    <th>Constructor</th>
                    <th>Points</th>
                    <th>Wins</th>
                </tr>
                <%
                    for (Map<String, Object> driverStanding : driverStandings) {
                        Map<String, Object> driver = (Map<String, Object>) driverStanding.get("Driver");
                        List<Map<String, Object>> constructors = (List<Map<String, Object>>) driverStanding.get("Constructors");
                %>
                <tr>
                    <td><%= driverStanding.get("position") %></td>
                    <td><%= driver.get("givenName") %> <%= driver.get("familyName") %></td>
                    <td><%= constructors.get(0).get("name") %></td>
                    <td><%= driverStanding.get("points") %></td>
                    <td><%= driverStanding.get("wins") %></td>
                </tr>
                <%
                    }
                %>
            </table>
    <%
            break;
        case "constructorStandings":
            List<Map<String, Object>> cStandingsLists = (List<Map<String, Object>>) ((Map<String, Object>) MRData.get("StandingsTable")).get("StandingsLists");
            Map<String, Object> cStandings = cStandingsLists.get(0); // Assuming we want the first list of standings
            List<Map<String, Object>> constructorStandings = (List<Map<String, Object>>) cStandings.get("ConstructorStandings");
            %>
            <h2>Query Results</h2>
            <h2>Constructor Standings</h2>
            <h4>Season: <%= cStandings.get("season") %> </h4>
            <h4>After Round: <%= cStandings.get("round")%></h4>
            <table style="width:100%">
                <tr>
                    <th>POS</th>
                    <th>Constructor</th>
                    <th>Nationality</th>
                    <th>Points</th>
                    <th>Wins</th>
                </tr>
                <%
                    for (Map<String, Object> constructorStanding : constructorStandings) {
                        Map<String, Object> constructor = (Map<String, Object>) constructorStanding.get("Constructor");
                %>
                <tr>
                    <td><%= constructorStanding.get("position") %></td>
                    <td><%= constructor.get("name") %></td>
                    <td><%= constructor.get("nationality") %></td>
                    <td><%= constructorStanding.get("points") %></td>
                    <td><%= constructorStanding.get("wins") %></td>
                </tr>
                <%
                    }
                %>
            </table>
    <%
            break;
    }
%>
    <br><br>
    <a href="index.jsp"><input type="submit" value="Back"></a>
</body>
</html>