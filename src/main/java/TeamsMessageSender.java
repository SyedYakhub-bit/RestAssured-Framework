import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

public class TeamsMessageSender {

    // Constants for your app
    private static final String CLIENT_ID = "6a051da7-0a16-42ea-a7aa-8456d52b5fce";
    private static final String CLIENT_SECRET = "b488ef60-b57d-4294-b460-48c46639432e";
    private static final String TENANT_ID = "a3afe9fa-a6af-4049-b0e3-05a64a7adb77";
    private static final String TEAM_ID = "your-team-id";
    private static final String CHANNEL_ID = "your-channel-id";
    private static final String AUTH_URL = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token";
    private static final String GRAPH_API_URL = "https://graph.microsoft.com/v1.0/teams/" + TEAM_ID + "/channels/" + CHANNEL_ID + "/messages";

    public static void main(String[] args) {
        // Step 1: Get the OAuth token
        String accessToken = getAccessToken();

        // Step 2: Send the message to the Teams channel
        sendTeamsMessage(accessToken, "Hello, Teams!");
    }

    // Method to get OAuth token
    private static String getAccessToken() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/x-www-form-urlencoded");

        // Body for the token request
        String requestBody = "grant_type=client_credentials" +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&scope=https://graph.microsoft.com/.default";

        // Send POST request to get the token
        Response response = request.body(requestBody).post(AUTH_URL);

        if (response.getStatusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.asString());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusLine());
        }
    }

    // Method to send a message to the Teams channel
    private static void sendTeamsMessage(String accessToken, String messageContent) {
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + accessToken);
        request.header("Content-Type", "application/json");

        // Body for the message
        JSONObject messageBody = new JSONObject();
        messageBody.put("body", new JSONObject().put("content", messageContent));

        // Send POST request to the Teams channel
        Response response = request.body(messageBody.toString()).post(GRAPH_API_URL);

        if (response.getStatusCode() == 201) {
            System.out.println("Message sent successfully!");
        } else {
            System.out.println("Failed to send message: " + response.getStatusLine());
            System.out.println("Response body: " + response.getBody().asString());
        }
    }
}
