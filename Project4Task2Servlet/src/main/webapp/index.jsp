<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <title>F1 Data</title>
  <script>
    function askForYearAndSubmit(form) {
      var year = prompt("Please enter the year", "2024");
      // Ensure year is valid
      if (year != null && year >= 1950) {
        // Get selected dataType value
        var dataType = document.querySelector('input[name="dataType"]:checked').value;
        // Construct URL with query parameters
        var action = form.action + "?dataType=" + dataType + "&year=" + year;
        // Redirect to the constructed URL
        window.location.href = action;
      }
    }
  </script>
</head>
<body>
<h1>F1 Data Retrieval</h1>
<form action="/getF1Info" method="GET" onsubmit="event.preventDefault(); askForYearAndSubmit(this);">
  What information can we provide today?<br><br>
  <label>
    <input type="radio" name="dataType" value="raceSchedule">
  </label> Race Schedule<br>
  <label>
    <input type="radio" name="dataType" value="driverStandings">
  </label> Driver Standings<br>
  <label>
    <input type="radio" name="dataType" value="constructorStandings">
  </label> Constructor Standings<br><br>
  <input type="submit" value="Submit">
</form>
<br>
<a href="dashboard"><input type="submit" value="Dashboard"></a>
</body>
</html>