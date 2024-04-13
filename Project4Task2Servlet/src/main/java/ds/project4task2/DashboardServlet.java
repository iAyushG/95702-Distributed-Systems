/**
 * Name: Ayush Gupta
 * Andrew ID: ayushgu2
 */

package ds.project4task2;

// Import statements for MongoDB client classes.
import com.mongodb.client.MongoClient;        // Interface representing a client to MongoDB.
import com.mongodb.client.MongoClients;       // Utility class to create MongoClient instances.
import com.mongodb.client.MongoDatabase;      // Interface representing a MongoDB database.
import com.mongodb.client.MongoCollection;    // Interface representing a collection in a MongoDB database.
import com.mongodb.client.model.Filters;      // Utility class for creating filter conditions.
import com.mongodb.client.model.Aggregates;   // Utility class for creating aggregation stages.
import com.mongodb.client.model.Accumulators; // Utility class for defining accumulators like sum, avg, etc.
import com.mongodb.client.model.Sorts;         // Utility class for creating sort specifications.
import jakarta.servlet.ServletException;      // Exception thrown to indicate a servlet issue.
import jakarta.servlet.annotation.WebServlet;  // Annotation to declare a servlet and map it to a URL.
import jakarta.servlet.http.HttpServlet;      // Base class for all servlets handling HTTP requests.
import jakarta.servlet.http.HttpServletRequest; // Provides request information for HTTP servlets.
import jakarta.servlet.http.HttpServletResponse;// Provides HTTP-specific functionality in sending a response.
import org.bson.Document;                     // MongoDB document represented as an immutable map.
import org.bson.conversions.Bson;             // Interface for types that can be used to update/query MongoDB documents.
import java.io.IOException;                   // Exception thrown when an I/O operation fails.
import java.util.Arrays;                      // Utility class that contains various methods for manipulating arrays.
import java.util.List;                        // Collection framework interface representing an ordered collection.
import java.util.ArrayList;                   // Resizable-array implementation of the List interface.

// WebServlet annotation to specify that this class responds to requests at "/dashboard".
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private MongoClient mongoClient; // Client instance for MongoDB operations.

    // Initializes the servlet and creates a MongoDB client instance.
    @Override
    public void init() {
        // Connection string for MongoDB
        String connectionString = "mongodb+srv://ayushgupta:P4T2MongoDB@cluster0.zcpu9fx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        mongoClient = MongoClients.create(connectionString);
    }

    // Handles HTTP GET requests.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Connect to the "F1Data" database.
        MongoDatabase database = mongoClient.getDatabase("F1Data");
        // Access the "RequestsLogs" collection.
        MongoCollection<Document> collection = database.getCollection("RequestsLogs");

        // Count the total number of documents (requests).
        long totalRequests = collection.countDocuments();
        // Count the number of successful requests using a filter.
        long successfulRequests = collection.countDocuments(Filters.eq("status", "Success"));
        // Calculate the success rate of requests.
        double successRate = totalRequests > 0 ? (double) successfulRequests / totalRequests * 100 : 0.0;

        // Aggregation pipeline to determine the most popular request data type.
        List<Bson> aggregationPipeline = Arrays.asList(
                Aggregates.group("$dataType", Accumulators.sum("count", 1)), // Group by dataType and count occurrences.
                Aggregates.sort(Sorts.descending("count")),                 // Sort groups by count in descending order.
                Aggregates.limit(1)                                         // Limit results to the top entry.
        );
        // Execute the aggregation and retrieve the result.
        Document mostPopularRequest = collection.aggregate(aggregationPipeline).first();
        // Extract the most popular data type, or default to "No data".
        String mostPopularDataType = mostPopularRequest != null ? mostPopularRequest.getString("_id") : "No data";

        // Retrieve all log documents from the collection for display.
        List<Document> logs = new ArrayList<>();
        collection.find().into(logs);

        // Set attributes for the request to be available in JSP.
        request.setAttribute("totalRequests", totalRequests);
        request.setAttribute("successRate", successRate);
        request.setAttribute("mostPopularDataType", mostPopularDataType);
        request.setAttribute("logs", logs);

        // Forward the request to the "dashboard.jsp" for rendering.
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    // Clean-up code for the servlet to close MongoDB client when it's being destroyed.
    @Override
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close(); // Close the MongoDB client.
        }
    }
}

// End of DashboardServlet class.
// Comments using generated with the help of ChatGPT and CoPilot AI.