import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
	private Scanner userScanner = null;
	private InjectionManager injections = null;
	private StudentManager students = null;
	private VaccineManager vaccines = null;

	private App() {
		userScanner = new Scanner(System.in);
		injections = InjectionManager.getInstance();
		students = StudentManager.getInstance();
		vaccines = VaccineManager.getInstance();
	}

	public static void main(String[] args) {
		App app = new App();
		app.view();
	}

	private void view() {
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
				choice = Integer.parseInt(userScanner.nextLine());
			} catch (NumberFormatException e) {
				System.err.println("ERROR: Invalid number format");
				continue;
			}
			System.out.println();

			switch (choice) {
				case 1:
					injections.printList();
					break;
				case 2:
					do {
						this.add();
						do {
							System.out.print("Do you want to add another injection (Y/N)? ");
							wantToContinue = userScanner.nextLine();
							if (!wantToContinue.matches("[YyNn]") || Utility.isEmptyString(wantToContinue)) {
								wantToContinue = null;
							}
						} while (wantToContinue == null);
					} while (wantToContinue.toUpperCase().equals("Y"));
					break;
				case 3:
					this.update();
					break;
				case 4:
					this.remove();
					break;
				case 5:
					this.search();
					break;
				case 6:
					System.out.println("Bye. Thank you for using me UwU");
					break;
				default:
					System.err.println("ERROR: Option is not available");
			}
		} while (choice != 6);

		injections.saveToFile();
		scanner.close();
	}

	private void add() {
		String id = null;
		String studentId = null;
		String vaccineId = null;
		String firstPlace = null, secondPlace = null;
		Date firstShot = null, secondShot = null;

		String inputString = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);

		System.out.println("\n------ADD NEW INJECTION------");

		do {
			System.out.print("Enter ID: ");
			id = userScanner.nextLine();
		} while (injections.isExisted(id) || Utility.isEmptyString(id));

		System.out.println();
		students.printList();
		do {
			System.out.print("Enter Student ID (according to above table): ");
			studentId = userScanner.nextLine();
		} while (!students.isExisted(studentId) || injections.isVaccinated(studentId) || Utility.isEmptyString(studentId));

		System.out.println();
		vaccines.printList();
		do {
			System.out.print("Enter Vaccine ID (according to above table): ");
			vaccineId = userScanner.nextLine();
		} while (!vaccines.isExisted(vaccineId) || Utility.isEmptyString(vaccineId));

		System.out.println();
		do {
			try {
				System.out.print("Enter first injection date (dd/mm/yyyy): ");
				inputString = userScanner.nextLine();

				if (Utility.isEmptyString(inputString)) {
					System.out.println("First injection is required");
					continue;
				}
				if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
					System.err.println("ERROR: Invalid date");
					continue;
				}

				firstShot = dateFormat.parse(inputString);
				if (injections.isFutureDate(firstShot)) {
					firstShot = null;
				}
			} catch (ParseException e) {
				System.err.println("ERROR: Invalid date");
			}
		} while (firstShot == null);

		System.out.println();
		do {
			System.out.print("Enter first injection place: ");
			firstPlace = userScanner.nextLine();
		} while (Utility.isEmptyString(firstPlace));

		System.out.println();
		do {
			try {
				System.out.print("Enter second injection date (dd/mm/yyyy) - This field can be ignored: ");
				inputString = userScanner.nextLine();

				if (inputString.equals("")) {
					break;
				}
				if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
					System.err.println("ERROR: Invalid date");
					continue;
				}

				secondShot = dateFormat.parse(inputString);
				if (!injections.isValidSecondDate(firstShot, secondShot)) {
					secondShot = null;
				}
			} catch (ParseException e) {
				System.err.println("ERROR: Invalid date");
			}
		} while (secondShot == null);

		System.out.println();
		System.out.print("Enter second injection place - This field can be ignored: ");
		secondPlace = userScanner.nextLine();
		if (secondPlace.equals("")) {
			secondPlace = null;
		}

		Injection injection = new Injection(id, firstPlace, secondPlace, firstShot, secondShot, studentId, vaccineId);
		injections.add(injection);
		System.out.println("New injection with ID = " + id + " has been added successfully");
	}

	private void update() {
		String id = null;

		System.out.println("------UPDATING INJECTION------");
		do {
			System.out.print("Enter the injection ID: ");
			id = userScanner.nextLine();
		} while (Utility.isEmptyString(id));

		System.out.println();
		Injection injection = null;
		try {
			injection = injections.find(id);
		} catch (NoSuchElementException e) {
			System.out.println("WARNING: Unavailable injection");
			return;
		}
		if (injection == null) {
			System.out.println("WARNING: Injection does not exist");
		} else if (injection.getSecondInjectionDate() != null && injection.getSecondInjectionPlace() != null) {
			System.out.println("This student has got 2 injections");
		} else {
			String inputString = null;
			String secondPlace = null;
			Date secondShot = null;
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			System.out.println();
			do {
				try {
					System.out.print("Enter second injection date (dd/mm/yyyy - 00/00/0000 to quit updating): ");
					inputString = userScanner.nextLine();

					if (inputString.equals("00/00/0000")) {
						return;
					}
					if (Utility.isEmptyString(inputString)) {
						continue;
					}
					if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
						System.err.println("ERROR: Invalid date");
						continue;
					}

					secondShot = dateFormat.parse(inputString);
					if (!injections.isValidSecondDate(injection.getFirstInjectionDate(), secondShot)) {
						secondShot = null;
					}
				} catch (ParseException e) {
					System.err.println("ERROR: Invalid date");
				}
			} while (secondShot == null);

			System.out.println();
			do {
				System.out.print("Enter second injection place: ");
				secondPlace = userScanner.nextLine();
			} while (Utility.isEmptyString(secondPlace));

			int index = injections.indexOf(injection);
			injection.setSecondInjectionDate(secondShot);
			injection.setSecondInjectionPlace(secondPlace);

			injections.update(index, injection);
			injections.printList(injection);
			System.out.println("Injection with ID = " + injection.getId() + " has been updated successfully");
		}
	}

	private void remove() {
		String id = null;
		String wantToContinue = null;

		System.out.println("------REMOVE INJECTION------");
		do {
			System.out.print("Enter injection ID: ");
			id = userScanner.nextLine();
		} while (Utility.isEmptyString(id));

		try {
			Injection injection = injections.find(id);
			do {
				System.out.print("Do you want to remove this injection (Y/N)? ");
				wantToContinue = userScanner.nextLine();
				if (!wantToContinue.matches("[YyNn]") || Utility.isEmptyString(wantToContinue)) {
					wantToContinue = null;
				}
			} while (wantToContinue == null);

			if (wantToContinue.toUpperCase().equals("Y")) {
				injections.remove(injection);
				System.out.println("Injection has been successfully removed");
			} else {
				System.out.println("Oke oke. I don't remove that UwU");
			}
		} catch (NoSuchElementException e) {
			System.out.println("WARNING: Unavailable injection");
			return;
		}

	}

	private void search() {
		String studentId = null;

		System.out.println("------SEARCH INJECTION BY STUDENT ID------");
		do {
			System.out.print("Enter student ID: ");
			studentId = userScanner.nextLine();
		} while (Utility.isEmptyString(studentId));

		try {
			Injection injection = injections.findByStudentId(studentId);
			List<Injection> searchList = new ArrayList<>();
			searchList.add(injection);
			injections.printList(searchList);
		} catch (NoSuchElementException e) {
			System.out.println("WARNING: Unavailable injection with such Student ID");
			return;
		}
	}
}
