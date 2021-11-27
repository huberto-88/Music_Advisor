import java.time.LocalDate;
import java.util.Scanner;

public class Main {
	public static void main(String []args) {
		Scanner scanner = new Scanner(System.in);
		LocalDate data = LocalDate.ofYearDay(scanner.nextInt(), scanner.nextInt());
		
		System.out.println(!(data.plusDays(1).getMonth().equals(data.getMonth())));
	}
}
