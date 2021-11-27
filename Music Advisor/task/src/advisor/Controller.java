package advisor;

public class Controller {
    private Authorization spotify = new Authorization();

    public void auth() {
        spotify.getAccessCode();
        spotify.getToken();
        spotify.setAuthorised(true);
    }


    public void newReleases() {
        if (spotify.isAuthorised()) {
            spotify.getNewReleases();

        } else if (!spotify.isAuthorised()) {
            System.out.println("Please, provide access for application.");
        }
    }

    public void categories() {
        if (spotify.isAuthorised()) {
            spotify.getCategories();
        }

    }
}
