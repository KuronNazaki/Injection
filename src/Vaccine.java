public class Vaccine {
	private String id;
	private String name;

	public Vaccine() {

	} 

	public Vaccine(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", name=" + name + "]";
	}
}
