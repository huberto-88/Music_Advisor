package advisor;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1 && args[0].contains("-access")) {
            Authorization.SERVER = args[1];
        }
        new App().startApp();
    }
}
