import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String []args) {
        Scanner scanner = new Scanner(System.in);
        LocalDate date = LocalDate.of(scanner.nextInt(), scanner.nextInt(), 1);

        for (int i = 1; i < date.lengthOfMonth(); i++) {
            if (date.plusDays(i).getDayOfWeek().name().equals("MONDAY")) {
                System.out.println(date.plusDays(i));
            }
        }
    }
}
