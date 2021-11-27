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
                    featured();
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
                    playList(chosenPlaylist);
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


    public void featured() {
//        if (spotify.isAuthorised()) {
//            System.out.println("---FEATURED---");
//            System.out.println("Mellow Morning");
//            System.out.println("Wake Up and Smell the Coffee");
//            System.out.println("Monday Motivation");
//            System.out.println("Songs to Sing in the Shower");
//
//        } else if (!spotify.isAuthorised()) {
//            System.out.println("Please, provide access for application.");
//        }
    }


    public void playList(String chosenPlaylist) {
        switch (chosenPlaylist) {
            case "Top Lists": {
                break;
            }
            case "Pop": {
                break;
            }

            case "Mood": {
                playMoodPlaylist();
                break;
            }
            case "Latin": {
                break;
            }
        }
        playMoodPlaylist();

    }

    private void playMoodPlaylist() {
        System.out.println("---MOOD PLAYLISTS---");
        System.out.println("Walk Like A Badass  ");
        System.out.println("Rage Beats  ");
        System.out.println("Arab Mood Booster  ");
        System.out.println("Sunday Stroll");
    }

}
