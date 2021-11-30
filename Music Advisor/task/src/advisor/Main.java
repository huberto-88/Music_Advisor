package advisor;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1 && args[0].contains("-access")) {
            Authorization.PATH_TO_SERVER = args[1];
        }
        if (args.length > 2 && args[2].contains("-resource")) {
            Authorization.PATH_TO_API = args[3];
        }

        new App().startApp();
    }
}
