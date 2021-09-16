import java.util.Scanner;

public class App {
	private final static Scanner userScanner = new Scanner(System.in);

	public static void main(String[] args) {
		view();
	}

	private static void view() {
		int choice = 0;
		Scanner scanner = new Scanner(System.in);
		String wantToContinue = null;

		do {
			System.out.println("\n\nWelcome to Injection Management - @2021 by Huynh Hoang Huy SE160046");
			System.out.println("Select the options below: ");
			System.out.println("1. Print the injection list");
			System.out.println("2. Add a new injection");
			System.out.println("3. Update an injection");
			System.out.println("4. Remove an injection");
			System.out.println("5. Search an injection by student ID");
			System.out.println("6. Quit");
			System.out.print("Your choice: ");

			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.err.println("ERROR: Invalid number format");
				continue;
			}
			System.out.println();

			switch (choice) {
				case 1:
					do {
						do {
							System.out.print("Do you want to add another injection (Y/N)? ");
							wantToContinue = scanner.nextLine();
							if (!wantToContinue.matches("[YyNn]")) {
								wantToContinue = null;
							}
						} while (wantToContinue == null);
					} while (wantToContinue.toUpperCase().equals("Y"));
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					System.out.println("Bye. Thank you for using me UwU");
					break;
				default:
					System.err.println("ERROR: Option is not available");
			}
		} while (choice != 6);

		scanner.close();
	}
}
