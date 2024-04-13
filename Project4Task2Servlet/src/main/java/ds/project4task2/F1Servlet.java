/**
 * Name: Ayush Gupta
 * Andrew ID: ayushgu2
 */

package ds.project4task2;

// Importing the ConnectionString class to parse MongoDB connection strings.
import com.mongodb.ConnectionString;
// Importing settings to configure MongoClient according to specified options.
import com.mongodb.MongoClientSettings;
// Classes to specify which version of MongoDB's API to use.
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
// MongoClient class to manage a MongoDB connection (client).
import com.mongodb.client.MongoClient;
// Factory class to create MongoClient instances.
import com.mongodb.client.MongoClients;
// Interface representing a MongoDB database.
import com.mongodb.client.MongoDatabase;
// Interface representing a collection in a MongoDB database.
import com.mongodb.client.MongoCollection;
// Servlet API for general servlet operations.
import jakarta.servlet.*;
// Annotation type to define this class as a Servlet.
import jakarta.servlet.annotation.WebServlet;
// Classes for handling HTTP-specific functionality.
import jakarta.servlet.http.*;
// MongoDB's Document class: a representation of a BSON document.
import org.bson.Document;
// IOException class for handling IO errors.
import java.io.IOException;
// URL class to handle HTTP URLs.
import java.net.URL;
// HttpURLConnection class to allow HTTP-specific connection operations.
import java.net.HttpURLConnection;
// Scanner class for parsing primitive types and strings using regular expressions.
import java.util.Scanner;
// Logger class to log information and error messages.
import java.util.logging.Level;
import java.util.logging.Logger;
// Date class to handle date operations.
import java.util.Date;

// Annotation to declare this class as a servlet which responds to "/getF1Info" URL pattern.
@WebServlet(name = "F1Servlet", value = "/getF1Info")
public class F1Servlet extends HttpServlet {

    // Base URL for the external F1 API.
    final static String baseURL = "http://ergast.com/api/f1/";
    // Logger to log information and errors for this servlet.
    private final static Logger LOGGER = Logger.getLogger(F1Servlet.class.getName());
    // Configuration settings for the MongoDB client.
    private MongoClientSettings settings;

    // Initialize servlet.
    public void init() {
        // MongoDB connection string
        String connectionString = "mongodb+srv://ayushgupta:P4T2MongoDB@cluster0.zcpu9fx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        // MongoDB server API settings to use version 1 of the API.
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        // Configure and build MongoDB client settings.
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
    }

    // Handle GET requests.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve 'dataType' and 'year' parameters from the HTTP request.
        String dataType = request.getParameter("dataType");
        String year = request.getParameter("year");

        // Use MongoDB client created with specified settings.
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            // Access 'F1Data' database.
            MongoDatabase database = mongoClient.getDatabase("F1Data");
            // Access 'RequestsLogs' collection.
            MongoCollection<Document> collection = database.getCollection("RequestsLogs");

            // Create URL object based on request parameters.
            URL url;
            if (dataType.equals("raceSchedule")) {
                url = new URL(baseURL + year + ".json");
            }
            else {
                url = new URL(baseURL + year + "/" + dataType + ".json");
            }

            // Open HTTP connection to the URL.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Set HTTP method to GET.
            conn.setRequestMethod("GET");
            // Accept JSON responses.
            conn.setRequestProperty("Accept", "application/json");

            // Check for successful response.
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // StringBuilder to accumulate JSON response.
                StringBuilder jsonResponse = new StringBuilder();
                // Read response using Scanner.
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }

                    // Log request details and response to MongoDB.
                    Document logEntry = new Document("dataType", dataType)
                            .append("year", year)
                            .append("apiUrl", url.toString())
                            .append("response", jsonResponse.toString())
                            .append("status", "Success")
                            .append("timestamp", new Date());
                    collection.insertOne(logEntry);

                    // Set request attributes for JSP.
                    request.setAttribute("viewOption", dataType);
                    request.setAttribute("jsonData", jsonResponse.toString());
                    // Forward to result JSP.
                    RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
                    dispatcher.forward(request, response);
                }
            }
            else {
                // Handle API failure.
                throw new Exception("Failed to fetch data from API. HTTP error code: " + conn.getResponseCode());
            }
        }
        catch (Exception e) {
            // Log and handle exceptions.
            LOGGER.log(Level.SEVERE, "Error during API call", e);
            handleError(request, response);
        }
    }

    // Error handling method to forward to an error page.
    private void handleError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", "An error occurred while processing your request. Please try again later.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }
}

// End of F1Servlet class.
// Comments generated with the help of ChatGPT and CoPilot AI.