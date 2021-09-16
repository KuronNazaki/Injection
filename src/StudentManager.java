import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManager implements FileConnection<Student> {
	private static final String FILE_NAME = "student.txt";
	private static StudentManager instance = null;

	private Scanner scanner = null;
	private List<Student> students = new ArrayList<>();

	private StudentManager() {

	}

	public static StudentManager getInstance() {
		if (instance == null) {
			instance = new StudentManager();
		}

		return instance;
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

	public List<Student> getFromFile() {
		boolean isReadMode = true;

		try {
			open(isReadMode);
	
			if (students.size() != 0) {
				students.clear();
			}
			while (scanner.hasNext()) {
				students.add(create(scanner.nextLine()));
			}
	
			close();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to connect to file");
		}

		return students;
	}

	@Override
	public Student create(String data) {
		String[] tokens = data.split(";");
		Student student = new Student(tokens[0], tokens[1]);
		return student;
	}
	
}
