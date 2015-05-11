package rtes;

import java.util.ArrayList;

public class Tasks {
	String name;
	int deadline;
	int period;
	int execution;
	int phase;
	int num_resources;
	ArrayList<Resource> res_used = new ArrayList<Resource>();
	ArrayList<Integer> start_time = new ArrayList<Integer>();
	ArrayList<Integer> total_time = new ArrayList<Integer>();

	// Constructor
	public Tasks(String a, int b, int c, int d, int e) {
		deadline = d;
		phase = b;
		period = c;
		execution = e;
		name = a;
	}
}
