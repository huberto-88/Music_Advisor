package advisor;

import advisor.model.Model;

public class Controller {
    private final Authorization spotifyAuth = new Authorization();
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void auth() {
        spotifyAuth.getAccessCode();
        spotifyAuth.getToken();
        spotifyAuth.setAuthorised(true);
        model.setTOKEN(spotifyAuth.getTOKEN());
    }

    public void newReleases() {
        if (spotifyAuth.isAuthorised()) {
            model.newReleases();
            view.display(model.getNewReleaseList());
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void categories() {
        if (spotifyAuth.isAuthorised()) {
            model.listCategories();
            view.display(model.getCategoriesList());
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void featured() {
        if (spotifyAuth.isAuthorised()) {
            model.featured();
            view.display(model.getFeaturedList());
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void playList(String chosenPlaylist) {
        if (spotifyAuth.isAuthorised()) {
            model.playlist(chosenPlaylist);
            view.display(model.getPlayListsList());
        } else {
            System.out.println("Please, provide access for application.");
        }
    }
}