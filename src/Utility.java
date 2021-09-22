import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utility {
	public static String toSimpleDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date).toString();
	}

	public static boolean isEmptyString(String data) {
		if (data.trim().equals("")) {
			System.err.println("ERROR: String can't be empty");
			return true;
		} else {
			return false;
		}
	}

	public static long getDaysBetween(Date date1, Date date2) {
		long difference = date1.compareTo(date2) < 0 ? date2.getTime() - date1.getTime()
				: date1.getTime() - date2.getTime();
		return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
	}
}
