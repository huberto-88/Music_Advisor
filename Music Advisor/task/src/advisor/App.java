package advisor;

import java.util.Scanner;

public class App {
    private Controller controller = new Controller();

    public void startApp() {
        Scanner scanner = new Scanner(System.in);

        for (;;) {
            String userRequest = scanner.nextLine();
            String chosenPlaylist = null;

            if (userRequest.contains("playlists")) {
                chosenPlaylist = userRequest.replace("playlists", "").strip();
                userRequest = "playlists";
            }

            switch (userRequest) {
                case "auth": {
                        controller.auth();
                    break;
                }
                case "featured": {
                    controller.featured();
                    break;
                }
                case "new": {
                    controller.newReleases();
                    break;
                }
                case "categories": {
                    controller.categories();
                    break;
                }
                case "playlists": {
                    controller.playList(chosenPlaylist);
                    break;
                }
                case "exit": {
                    System.out.println("---GOODBYE!---");
                    System.exit(0);
                }
                default: {
                    System.out.println("Nieznana komenda");
                }
            }
        }
    }
}
