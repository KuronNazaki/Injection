import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VaccineManager implements FileConnection<Vaccine> {
	private static final String FILE_NAME = "vaccine.txt";
	private static VaccineManager instance = null;

	private Scanner scanner = null;
	private List<Vaccine> vaccines = new ArrayList<>();

	private VaccineManager() {
		getFromFile();
	}

	public static VaccineManager getInstance() {
		if (instance == null) {
			instance = new VaccineManager();
		}

		return instance;
	}

	public boolean isExisted(String id) {
		if (vaccines.isEmpty()) {
			return false;
		}

		for (Vaccine vaccine : vaccines) {
			if (vaccine.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public Vaccine find(String id) {
		return vaccines.isEmpty() ? null
				: vaccines.stream().filter(vaccine -> vaccine.getId().equals(id)).findFirst().get();
	}

	@Override
	public void open(boolean isReadMode) throws IOException {
		if (isReadMode) {
			scanner = new Scanner(Paths.get(FILE_NAME), "UTF-8");
		}
	}

	@Override
	public void close() throws IOException {
		if (scanner != null) {
			scanner.close();
		}
	}

	public List<Vaccine> getFromFile() {
		boolean isReadMode = true;

		try {
			open(isReadMode);

			if (vaccines.size() != 0) {
				vaccines.clear();
			}
			while (scanner.hasNext()) {
				vaccines.add(create(scanner.nextLine()));
			}

			close();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to connect to file");
		}
		return vaccines;
	}

	@Override
	public Vaccine create(String data) {
		String[] tokens = data.split(";");
		Vaccine vaccine = new Vaccine(tokens[0], tokens[1]);
		return vaccine;
	}

	public void printList() {
		printList(vaccines);
	}

	public void printList(List<Vaccine> vaccines) {
		System.out.println("+----Vaccine List-----+");
		System.out.format("|%5s|%15s|\n", "ID", "Name");
		System.out.println("+---------------------+");
		vaccines.stream().forEach(VaccineManager::printFormattedItem);
		System.out.println("+---------------------+");
	}

	private static void printFormattedItem(Vaccine vaccine) {
		System.out.format("|%5s|%15s|\n", vaccine.getId(), vaccine.getName());
	}
}
