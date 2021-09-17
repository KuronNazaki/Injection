import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InjectionManager implements FileConnection<Injection> {
	private static final String FILE_NAME = "injection.txt";
	private static final long MIN_DIFFERENCE_WEEKS = 4;
	private static final long MAX_DIFFERENCE_WEEKS = 12;
	private static InjectionManager instance = null;

	private Scanner scanner = null;
	private FileWriter fileWriter = null;
	private BufferedWriter bufferedWriter = null;
	private PrintWriter printWriter = null;
	private List<Injection> injections = new ArrayList<>();

	private InjectionManager() {

	}

	public static InjectionManager getInstance() {
		if (instance == null) {
			instance = new InjectionManager();
		}

		return instance;
	}

	public boolean isExisted(String id) {
		if (injections.isEmpty()) {
			return false;
		}

		for (Injection injection : injections) {
			if (injection.getId().equals(id)) {
				System.out.println("WARNING: ID is not available");
				return true;
			}
		}

		return false;
	}

	public int indexOf(Injection injection) {
		return injections.indexOf(injection);
	}

	public Injection find(String id) {
		return injections.isEmpty() ? null
				: injections.stream().filter(injection -> injection.getId().equals(id)).findFirst().get();
	}

	public List<Injection> findAll(String studentId) {
		return injections.isEmpty() ? null
				: injections.stream().filter(injection -> injection.getStudentId().equals(studentId))
						.collect(Collectors.toList());
	}

	public boolean isFutureDate(Date injectionDate) {
		Date now = new Date();
		if (injectionDate.compareTo(now) > 0) {
			System.out.println("WARNING: You can't get the vaccine in the future");
			return true;
		}
		return false;
	}

	public boolean isValidSecondDate(Date firstDate, Date secondDate) {
		if (isFutureDate(secondDate)) {
			return false;
		}

		long difference = Utility.getDaysBetween(firstDate, secondDate);
		if (difference < (MIN_DIFFERENCE_WEEKS * 7) || difference > (MAX_DIFFERENCE_WEEKS * 7)) {
			System.out.println("WARNING: The second injection must be between 4 and 12 weeks after the first one");
			return false;
		}

		return true;
	}

	public boolean isVaccinated(String studentId) {
		return injections.stream().anyMatch(injection -> injection.getStudentId().equals(studentId));
	}

	public boolean isValidVaccine(String firstVaccineId, String secondVaccineId) {
		return firstVaccineId.equals(secondVaccineId);
	}

	public void add(Injection injection) {
		injections.add(injection);
	}

	public void remove(Injection injection) {
		injections.remove(injection);
	}

	public void update(int index, Injection injection) {
		injections.set(index, injection);
	}

	@Override
	public void open(boolean isReadMode) throws IOException {
		if (isReadMode) {
			scanner = new Scanner(Paths.get(FILE_NAME), "UTF-8");
		} else {
			fileWriter = new FileWriter(FILE_NAME, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufferedWriter);
		}
	}

	@Override
	public void close() throws IOException {
		if (scanner != null) {
			scanner.close();
		} else if (printWriter != null && bufferedWriter != null && fileWriter != null) {
			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();
		}
	}

	public List<Injection> getFromFile() {
		boolean isReadMode = true;

		try {
			open(isReadMode);

			if (injections.size() != 0) {
				injections.clear();
			}
			while (scanner.hasNext()) {
				injections.add(create(scanner.nextLine()));
			}

			close();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to connect to file");
		}
		return null;
	}

	public void saveToFile() {
		boolean isReadMode = false;
		try {
			open(isReadMode);

			injections.stream().forEach(injection -> printWriter.println(injection.toWritableString()));

			close();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to connect to file");
		}
	}

	@Override
	public Injection create(String data) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String[] tokens = data.split("\\|");
		Injection injection = null;

		try {
			injection = new Injection(tokens[0], tokens[1], tokens[2], dateFormat.parse(tokens[3]),
					dateFormat.parse(tokens[4]), tokens[5], tokens[6]);
		} catch (ParseException e) {
			System.err.println("ERROR: Failed to parse object");
		}

		return injection;
	}

	public void printList() {
		printList(injections);
	}

	public void printList(List<Injection> injections) {
		System.out.println(
				"+---------------------------------------------Injection List--------------------------------------------------+");
		System.out.format("|%8s|%15s|%15s|%15s|%15s|%15s|%20s|\n", "ID", "First Date", "First Place", "Second Date",
				"Second Place", "Student ID", "Student Name");
		System.out.println(
				"+-------------------------------------------------------------------------------------------------------------+");
		injections.stream().forEach(InjectionManager::printFormattedItem);
		System.out.println(
				"+-------------------------------------------------------------------------------------------------------------+");
	}

	private static void printFormattedItem(Injection injection) {
		System.out.format("|%8s|%15s|%15s|%15s|%15s|%15s|%20s|\n", injection.getId(),
				Utility.toSimpleDateString(injection.getFirstInjectionDate()), injection.getFirstInjectionPlace(),
				injection.getSecondInjectionDate() == null ? "N/A"
						: Utility.toSimpleDateString(injection.getSecondInjectionDate()),
				injection.getSecondInjectionPlace() == null ? "N/A" : injection.getSecondInjectionPlace(),
				injection.getStudentId(), StudentManager.getInstance().find(injection.getStudentId()).getName());
	}
}
