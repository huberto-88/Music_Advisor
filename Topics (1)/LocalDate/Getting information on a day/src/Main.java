import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        LocalDate date = LocalDate.parse(new Scanner(System.in).next());
        System.out.printf("%d %d", date.getDayOfYear(), date.getDayOfMonth());
    }
}