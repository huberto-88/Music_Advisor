package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authorization {
    private boolean isAuthorised = false;

    public static String PATH_TO_SERVER = "https://accounts.spotify.com";
    public static String PATH_TO_API = "https://api.spotify.com";

    public static String REDIRECT_URI = "http://localhost:8080";
    public static String CLIENT_ID = "304f6e6c5c81418f9e653d683b36312d";
    public static String CLIENT_SECRET = "fc742f1aa8194d47b2a3768e69e6fefd";
    public static String CODE = "";
    public static String TOKEN;

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
            System.out.println("tOKEN: " + TOKEN);
            System.out.println("---SUCCESS---");

        } catch (IOException | InterruptedException exception) {
            System.out.println("error server token response");
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    public void getNewReleases() {
        String path = PATH_TO_API + "/v1/browse/new-releases";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonObject albums = json.getAsJsonObject("albums");

            JsonElement items = albums.getAsJsonArray("items");

            for (JsonElement element : items.getAsJsonArray()) {

                if (element.isJsonObject()) {

                    System.out.println(element.getAsJsonObject().get("name").getAsString());

                    JsonArray elementArray = element.getAsJsonObject().getAsJsonArray("artists");

                    List<String> artistsList = new ArrayList<>();

                    for (JsonElement el : elementArray) {
                        artistsList.add(el.getAsJsonObject().get("name").getAsString());
                    }

                    System.out.println(artistsList);
                    System.out.println(element.getAsJsonObject().get("external_urls")
                            .getAsJsonObject().get("spotify").getAsString() + "\n");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    public Map<String, String> getListOfCategories() {
        Map<String, String> categoriesIdMap = new HashMap<>();
        String path = PATH_TO_API + "/v1/browse/categories";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonObject categories = json.getAsJsonObject("categories");
            JsonElement items = categories.getAsJsonArray("items");

            for (JsonElement element : items.getAsJsonArray()) {
                if (element.isJsonObject()) {
                    categoriesIdMap.put(
                            element.getAsJsonObject().get("name").getAsString(),
                            element.getAsJsonObject().get("id").getAsString());
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return categoriesIdMap;
    }

    public void getCategories() {
        getListOfCategories().keySet()
                .forEach(System.out::println);
    }

//----------------------------------------------------------------------------------------------------------------------

    public void getFeatured() {
        String path = PATH_TO_API + "/v1/browse/featured-playlists";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonObject categories = json.getAsJsonObject("playlists");
            JsonElement items = categories.getAsJsonArray("items");

            for (JsonElement element : items.getAsJsonArray()) {
                if (element.isJsonObject()) {
                    System.out.println(element.getAsJsonObject().get("name").getAsString());

                    JsonObject external_urls = element.getAsJsonObject().get("external_urls").getAsJsonObject();
                    String href = external_urls.get("spotify").getAsString();

                    System.out.println(href);
                    System.out.println();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------------------------

    public void playList(String chosenPlaylist) {
        Map<String, String> categoriesOfIdMap = getListOfCategories();

        if (!categoriesOfIdMap.containsKey(chosenPlaylist)) {
            System.out.println("Specified id doesn't exist");
            return;
        }

        String id = categoriesOfIdMap.get(chosenPlaylist);

        String path = PATH_TO_API + "/v1/browse/categories/" + id + "/playlists";

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + TOKEN)
                    .uri(URI.create(path))
                    .GET()
                    .build();

            try {
                HttpClient client = HttpClient.newBuilder().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonString = response.body();

                JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

                if (response.statusCode() != 200) {
                    JsonObject error = json.getAsJsonObject("error");
                    System.out.println(error.get("message").getAsString());
                    return;
                }

                try {
                    JsonObject playlists = json.getAsJsonObject("playlists");
                    JsonElement items = playlists.getAsJsonArray("items");

                    for (JsonElement element : items.getAsJsonArray()) {
                        if (element.isJsonObject()) {
                            System.out.println(element.getAsJsonObject().get("name").getAsString());
                            JsonObject url = element.getAsJsonObject().get("external_urls").getAsJsonObject();
                            System.out.println(url.get("spotify").getAsString() + "\n");
                        }
                    }
                } catch (NullPointerException e) {
                    JsonObject error = json.getAsJsonObject("error");
                    System.out.println(error.get("message").getAsString());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
    }
}




















