package rtes;

public class Resource {
	String Resource_name;
	Active_Jobs lockedBy;

	public Resource(String x) {
		Resource_name = x;
	}
}
