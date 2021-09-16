import java.io.IOException;

public interface FileConnection<T> {
	void open(boolean isReadMode) throws IOException;
	void close() throws IOException;
	T create(String data);
}
