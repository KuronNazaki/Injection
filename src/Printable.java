import java.util.List;

public interface Printable<T> {
	void print();
	void print(List<T> list);
}
