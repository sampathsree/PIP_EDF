package rtes;

//import java.io.BufferedInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import rtes.Active_Jobs;
import rtes.Tasks;

public class Main {

	public static void main(String[] argv) {

		ArrayList<Tasks> tasks = new ArrayList<Tasks>();
		// ArrayList<Resource> Res = new ArrayList<Resource>();
		int timeline = 0, n = 0;
		int[] next_arrival;

		Scanner s;
		try {
			// BufferedInputStream s=new BufferedInputStream(new
			// FileInputStream(new File("a.txt"));
			s = new Scanner(new File("a.txt"));

			timeline = s.nextInt();
			n = s.nextInt();

			// read input from file

			while (s.hasNext()) {
				// System.out.println(s.next());
				int i = 0;
				String temp = s.next();
				int phase = s.nextInt();
				int period = s.nextInt();
				int execution = s.nextInt();
				int deadline = s.nextInt();
				tasks.add(new Tasks(temp, phase, period, deadline, execution));

				if (s.hasNextInt()) { // check if next token is an int
					int x = s.nextInt();
					for (int j = 0; j < x; j++) {
						String a = s.next();
						int m = s.nextInt();
						int l = s.nextInt();
						tasks.get(i).res_used.add(new Resource(a));
						tasks.get(i).start_time.add(m);
						tasks.get(i).total_time.add(l);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int small = 0;
		int small_index;
		// initialize next arrival
		next_arrival = new int[n];
		for (int i = 0; i < n; i++) {
			next_arrival[i] = tasks.get(i).phase;
		}

		for (int i = 0; i < n; i++) {
			System.out.println("next arrival[" + i + "] = " + next_arrival[i]);
			System.out.println("period[" + i + "] = " + tasks.get(i).period);
			System.out.println("phase[" + i + "] = " + tasks.get(i).phase);
			System.out
					.println("deadline[" + i + "] = " + tasks.get(i).deadline);
			System.out.println("execution time[" + i + "] = "
					+ tasks.get(i).execution);
		}

		ArrayList<Active_Jobs> active = new ArrayList<Active_Jobs>();
		// for every second until last second
		for (int timeinstance = 0; timeinstance <= timeline; timeinstance++) {

			// based on t set arrival times
			for (int i = 0; i < n; i++) {
				if (next_arrival[i] == timeinstance) {// Always a task should start at zero
					active.add(new Active_Jobs(tasks.get(i).name, i, timeinstance, tasks
							.get(i).deadline, tasks.get(i).period));
					next_arrival[i] += tasks.get(i).period;
				}
			}

			if (active.size() != 0) {
				small = active.get(0).abs_deadline;
			}
			small_index = -1;

			for (int k = 0; k < active.size(); k++) {
				small_index = 0;
				for (int i = k + 1; i < active.size(); i++) {
					// compare absolute deadlines
					if (active.get(i).abs_deadline < small) {
						small = active.get(i).abs_deadline;
						small_index = i;
					} else if (small == active.get(i).abs_deadline) {
						if (active.get(i).arrival_time < active
								.get(small_index).arrival_time) {
							small = active.get(i).abs_deadline;
							small_index = i;
						} else if (active.get(i).arrival_time == active
								.get(small_index).arrival_time) {
							if (active.get(i).task_number < active
									.get(small_index).task_number) {
								small = active.get(i).abs_deadline;
								small_index = i;
							}
						}
					}
				}
				active.get(small_index).assigned_priority = k + 1;
			}

			// job supposed to be executing
			for (int i = 0; i < active.size(); i++) {
				if (active.get(i).assigned_priority == 1)
					small_index = i;
			}

			// check if job requires resources
			int check = active.get(small_index).task_number;
			int temp = tasks.get(check).res_used.size();

			int done = active.get(small_index).exe_done;

			// for every resource, check the start time
			for (int i = 0; i < temp; i++) {
				int b = tasks.get(check).start_time.get(i);

				// if it requires, allocate resource if free or already holding
				// and lock the resource
				if (done == b
						&& (tasks.get(check).res_used.get(i).lockedBy == null || tasks
								.get(check).res_used.get(i).lockedBy == active
								.get(i))) {
					tasks.get(check).res_used.get(i).lockedBy = active.get(i);
				}

				// if the resource is not free, update the priority of the job
				// blocking it
				else {
					int index = tasks.get(check).res_used.indexOf(tasks
							.get(check).res_used.get(small_index).lockedBy);
					active.get(index).current_priority = active
							.get(small_index).current_priority;
				}
				// and execute that job

				// update the execution status for every job if there is any job
				// executed
				if (small_index != -1) {
					active.get(small_index).exe_done += 1;
				}

				// check if the resource has been released, and update the
				// priority
				if (active.get(small_index).exe_done == tasks.get(check).total_time
						.get(i)) {
					tasks.get(check).res_used.get(i).lockedBy = null;
					active.get(small_index).current_priority = active
							.get(small_index).assigned_priority;
				}
			}

			// remove job from ArrayList if it has missed deadline or done with
			// execution
			if (active.get(small_index).exe_done == active.get(small_index).exe_time) {
				active.remove(small_index);
			}
			for (int k = 0; k < active.size(); k++) {
				if (active.get(k).abs_deadline == timeinstance) {
					active.remove(k);
				}
			}

			System.out.println("  " + active.get(small_index).task
					+ active.get(small_index).job_id);// tasks.get(small_index).res_used.get(check).Resource_name);
		}// seconds loop ends here
	}// main method ends
}// main class ends