package advisor;

import java.util.List;
import java.util.Scanner;

public class View {
    private final int itemPerPage;
    private int maxPages;

    public View(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public <T> void display(List<T> itemsList) {

        Scanner scanner = new Scanner(System.in);
        String request;
        this.maxPages = (int) Math.ceil(itemsList.size() / (double) itemPerPage);
        int currentPage = 1;
        int currentItem = 0;

        printPage(itemsList, currentItem, currentPage);

        do {

            request = scanner.nextLine();

            if (request.equals("next")) {
                if (currentPage == maxPages) {
                    System.out.println("No more pages.");
                } else {
                    currentPage++;
                    currentItem += itemPerPage;
                    printPage(itemsList, currentItem, currentPage);
                }
            } else if (request.equals("prev")) {
                if (currentPage == 1) {
                    System.out.println("No more pages.");
                } else {
                    currentPage--;
                    currentItem -= itemPerPage;
                    printPage(itemsList, currentItem, currentPage);
                }
            }
        } while (!request.equals("exit"));
    }

    private <T> void printPage(List<T> itemsList, int currentItem, int currentPage) {
        itemsList.stream()
                .skip(currentItem)
                .limit(itemPerPage)
                .forEach(System.out::println);

        System.out.printf("---PAGE %d OF %d---\n", currentPage, maxPages);
    }
}
