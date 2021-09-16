import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InjectionManager implements FileConnection<Injection> {
	private static final String FILE_NAME = "injection.txt";
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
			injection = new Injection(tokens[0], tokens[1], tokens[2], dateFormat.parse(tokens[3]), dateFormat.parse(tokens[4]), tokens[5], tokens[6]);
		} catch (ParseException e) {
			System.err.println("ERROR: Failed to parse object");
		}

		return injection;
	}
}
