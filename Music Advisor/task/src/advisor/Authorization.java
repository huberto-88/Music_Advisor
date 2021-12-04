package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Authorization {
    private boolean isAuthorised = false;
    public static String PATH_TO_SERVER = "https://accounts.spotify.com";
    private final String REDIRECT_URI = "http://localhost:8080";
    private final String CLIENT_ID = "304f6e6c5c81418f9e653d683b36312d";
    private final String CLIENT_SECRET = "fc742f1aa8194d47b2a3768e69e6fefd";
    private String CODE = "";
    private String TOKEN;

    public String getTOKEN() {
        return TOKEN;
    }

    public boolean isAuthorised() {
        return isAuthorised;
    }

    public void setAuthorised(boolean authorised) {
        isAuthorised = authorised;
    }


    public void getAccessCode() {
        String uri = PATH_TO_SERVER + "/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code";
        System.out.println("use this link to request the access code:");
        System.out.println(uri);

        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();

            server.createContext("/",
                    exchange -> {
                        String query = exchange.getRequestURI().getQuery();
                        String request;
                        if (query != null && query.contains("code")) {
                            CODE = query.substring(5);
                            System.out.println("code received");
                            System.out.println(CODE);
                            request = "Got the code. Return back to your program.";
                        } else {
                            request = "Authorization code not found. Try again.";
                        }
                        exchange.sendResponseHeaders(200, request.length());
                        exchange.getResponseBody().write(request.getBytes());
                        exchange.getResponseBody().close();
                    });

            System.out.println("waiting for code...");
            while (CODE.length() == 0) {
                Thread.sleep(100);
            }
            server.stop(5);

        } catch (IOException | InterruptedException e) {
            System.out.println("Server error");
        }
    }


    public void getToken() {
        System.out.println("making http request for access_token...");
        System.out.println("response");

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(PATH_TO_SERVER + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + CODE
                                + "&client_id=" + CLIENT_ID
                                + "&client_secret=" + CLIENT_SECRET
                                + "&redirect_uri=" + REDIRECT_URI))
                .build();
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            TOKEN = jsonObject.get("access_token").getAsString();
            System.out.println("---SUCCESS---");

        } catch (IOException | InterruptedException exception) {
            System.out.println("error server token response");
        }
    }
}




















