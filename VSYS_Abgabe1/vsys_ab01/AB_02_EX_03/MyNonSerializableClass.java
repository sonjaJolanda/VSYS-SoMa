package AB_02_EX_03;

public class MyNonSerializableClass {
	private int id;

	MyNonSerializableClass() {
		id=5678;
	}

	public String toString() {
		return "id: "+id;
	}
}
	