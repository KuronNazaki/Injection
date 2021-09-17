import java.util.Date;

public class Injection {
	private String id;
	private String firstInjectionPlace;
	private String secondInjectionPlace;
	private Date firstInjectionDate;
	private Date secondInjectionDate;
	private String studentId;
	private String vaccineId;

	public Injection() {

	}

	public Injection(String id, String firstInjectionPlace, String secondInjectionPlace, Date firstInjectionDate,
			Date secondInjectionDate, String studentId, String vaccineId) {
		this.id = id;
		this.firstInjectionPlace = firstInjectionPlace;
		this.secondInjectionPlace = secondInjectionPlace;
		this.firstInjectionDate = firstInjectionDate;
		this.secondInjectionDate = secondInjectionDate;
		this.studentId = studentId;
		this.vaccineId = vaccineId;
	}

	public String getId() {
		return id;
	}

	public String getFirstInjectionPlace() {
		return firstInjectionPlace;
	}

	public void setFirstInjectionPlace(String firstInjectionPlace) {
		this.firstInjectionPlace = firstInjectionPlace;
	}

	public String getSecondInjectionPlace() {
		return secondInjectionPlace;
	}

	public void setSecondInjectionPlace(String secondInjectionPlace) {
		this.secondInjectionPlace = secondInjectionPlace;
	}

	public Date getFirstInjectionDate() {
		return firstInjectionDate;
	}

	public void setFirstInjectionDate(Date firstInjectionDate) {
		this.firstInjectionDate = firstInjectionDate;
	}

	public Date getSecondInjectionDate() {
		return secondInjectionDate;
	}

	public void setSecondInjectionDate(Date secondInjectionDate) {
		this.secondInjectionDate = secondInjectionDate;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(String vaccineId) {
		this.vaccineId = vaccineId;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", firstInjectionPlace=" + firstInjectionPlace
				+ ", secondInjectionPlace=" + secondInjectionPlace + ", firstInjectionDate=" + firstInjectionDate
				+ ", secondInjectionDate=" + secondInjectionDate + ", studentId=" + studentId + ", vaccineId=" + vaccineId
				+ "]";
	}

	public String toWritableString() {
		return id + "|" + firstInjectionPlace + "|" + secondInjectionPlace + "|"
				+ Utility.toSimpleDateString(firstInjectionDate) + "|" + Utility.toSimpleDateString(secondInjectionDate) + "|"
				+ studentId + "|" + vaccineId;
	}
}
