package advisor;

import java.io.IOException;
import java.util.Scanner;

public class App {

    private boolean isAuthorised = false;

    private void setAccessAuthorisation(boolean accessForApplication) {
        this.isAuthorised = accessForApplication;
    }

    public boolean getAuthorised() {
        return isAuthorised;
    }

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
                        auth();
                    break;
                }
                case "featured": {
                    featured();
                    break;
                }
                case "new": {
                    newReleases();
                    break;
                }
                case "categories": {
                    categories();
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

    public void newReleases() {
        if (getAuthorised()) {
            System.out.println("---NEW RELEASES---");
            System.out.println("Mountains [Sia, Diplo, Labrinth]");
            System.out.println("Runaway [Lil Peep]");
            System.out.println("The Greatest Show [Panic! At The Disco]");
            System.out.println("All Out Life [Slipknot]");
        } else if (!getAuthorised()) {
            System.out.println("Please, provide access for application.");
        }

    }

    public void featured() {
        if (getAuthorised()) {
            System.out.println("---FEATURED---");
            System.out.println("Mellow Morning");
            System.out.println("Wake Up and Smell the Coffee");
            System.out.println("Monday Motivation");
            System.out.println("Songs to Sing in the Shower");

        } else if (!getAuthorised()) {
            System.out.println("Please, provide access for application.");
        }
    }

    public void categories() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");

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

    public void auth() {
        Authorization auth = new Authorization();
        auth.getAccessCode();
        auth.getToken();
        setAccessAuthorisation(true);
    }
}
