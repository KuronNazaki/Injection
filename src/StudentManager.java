import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManager implements FileConnection<Student>, Printable<Student> {
	private static final String FILE_NAME = "student.txt";
	private static StudentManager instance = null;

	private Scanner scanner = null;
	private List<Student> students = new ArrayList<>();

	private StudentManager() {
		getFromFile();
	}

	public static StudentManager getInstance() {
		if (instance == null) {
			instance = new StudentManager();
		}

		return instance;
	}


	public boolean isExisted(String id) {
		if (students.isEmpty()) {
			return false;
		}

		for (Student student : students) {
			if (student.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public Student find(String id) {
		return students.isEmpty() ? null
				: students.stream().filter(student -> student.getId().equals(id)).findFirst().get();
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

	public void print() {
		print(students);
	}

	public void print(List<Student> students) {
		System.out.println(
				"+-----------Student List----------+");
		System.out.format("|%12s|%20s|\n", "ID", "Name");
		System.out.println(
				"+---------------------------------+");
		students.stream().forEach(StudentManager::printFormattedItem);
		System.out.println(
				"+---------------------------------+");
	}

	private static void printFormattedItem(Student student) {
		System.out.format("|%12s|%20s|\n", student.getId(), student.getName());
	}
}
