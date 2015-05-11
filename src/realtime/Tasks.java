package realtime;

import java.util.ArrayList;

public class Tasks {
	String name;
	int deadline;
	int period;
	int execution;
	int phase;
	int num_resources;
	int act_dead;
	int curr_exe_instance;
	ArrayList<Resource> res_used = new ArrayList<Resource>();
	ArrayList<Integer> start_time = new ArrayList<Integer>();
	ArrayList<Integer> total_time = new ArrayList<Integer>();

	// Constructor
	public Tasks(String a, int b, int c, int d, int e, int actdead, int exetime, int res,ArrayList<Resource> res_used2,
			ArrayList<Integer> start_time2,ArrayList<Integer> total_time2) {
		deadline = d;
		phase = b;
		period = c;
		execution = e;
		name = a;
		act_dead = actdead;
		curr_exe_instance = exetime;
		num_resources = res;
		res_used = res_used2;
		start_time = start_time2;
		total_time = total_time2;
	}
}
