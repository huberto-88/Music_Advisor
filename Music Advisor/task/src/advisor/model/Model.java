package advisor.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Model {

    public static String PATH_TO_API = "https://api.spotify.com";
    private String TOKEN;

    private List<NewRelease> newReleaseList = new ArrayList<>();
    private List<String> categoriesList = new ArrayList<>();
    private List<Playlist> playListsList = new ArrayList<>();
    private final List<Featured> featuredList = new ArrayList<>();

    public List<NewRelease> getNewReleaseList() {
        return newReleaseList;
    }

    public List<String> getCategoriesList() {
        Collections.sort(this.categoriesList);
        Collections.reverse(this.categoriesList);

        return categoriesList;
    }

    public List<Playlist> getPlayListsList() {
        return playListsList;
    }

    public List<Featured> getFeaturedList() {
        return featuredList;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public void newReleases() {
        String path = PATH_TO_API + "/v1/browse/new-releases";
        List<NewRelease> tempList = new ArrayList<>();

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
                NewRelease newRelease = new NewRelease();

                if (element.isJsonObject()) {

                    newRelease.setTitle(element.getAsJsonObject().get("name").getAsString());

                    JsonArray elementArray = element.getAsJsonObject().getAsJsonArray("artists");

                    List<String> artistsList = new ArrayList<>();

                    for (JsonElement el : elementArray) {
                        artistsList.add(el.getAsJsonObject().get("name").getAsString());
                    }

                    newRelease.setAuthors(artistsList);
                    newRelease.setHref(element.getAsJsonObject().get("external_urls")
                            .getAsJsonObject().get("spotify").getAsString());
                }

                tempList.add(newRelease);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        newReleaseList = tempList;
    }

//----------------------------------------------------------------------------------------------------------------------

    public Map<String, String> getMapOfCategories() {
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

    //----------------------------------------------------------------------------------------------------------------------
    public void listCategories() {
        this.categoriesList = new ArrayList<>(getMapOfCategories().keySet());
    }

//----------------------------------------------------------------------------------------------------------------------

    public void featured() {
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
                    Featured featured = new Featured();

                    featured.setName(element.getAsJsonObject().get("name").getAsString());

                    JsonObject external_urls = element.getAsJsonObject().get("external_urls").getAsJsonObject();

                    featured.setUrl(external_urls.get("spotify").getAsString());
                    this.featuredList.add(featured);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------------------------

    public void playlist(String chosenPlaylist) {
        Map<String, String> categoriesOfIdMap = getMapOfCategories();

        if (!categoriesOfIdMap.containsKey(chosenPlaylist)) {
            System.out.println("Specified id doesn't exist");
            this.playListsList = Collections.emptyList();
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
                this.playListsList = Collections.emptyList();
                return;
            }

            try {
                JsonObject playlists = json.getAsJsonObject("playlists");
                JsonElement items = playlists.getAsJsonArray("items");

                for (JsonElement element : items.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        Playlist playlist = new Playlist();

                        playlist.setName(element.getAsJsonObject().get("name").getAsString());
                        JsonObject url = element.getAsJsonObject().get("external_urls").getAsJsonObject();
                        playlist.setUrl(url.get("spotify").getAsString());
                        playListsList.add(playlist);
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
