package realm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import realm.Tasks;

public class Main {

	public static void main(String[] argv) {

		ArrayList<Tasks> tasks = new ArrayList<Tasks>();
		int timeline = 0, num_tasks = 0;
		Map<String, String> resources = new HashMap<String, String>();
		Scanner s;
		try {
			s = new Scanner(new File("a.txt"));

			timeline = s.nextInt();
			num_tasks = s.nextInt();

			// read input from file

			while (s.hasNext()) {
				// System.out.println(s.next());
				int i = 0;
				String temp = s.next();
				int phase = s.nextInt();
				int period = s.nextInt();
				int execution = s.nextInt();
				int deadline = s.nextInt();
				int act_dead = phase + deadline;
				int x = s.nextInt();

				ArrayList<Resource> res_used = new ArrayList<Resource>();
				ArrayList<Integer> start_time = new ArrayList<Integer>();
				ArrayList<Integer> total_time = new ArrayList<Integer>();
				// if (s.hasNextInt()) { // check if next token is an int
				// int x = s.nextInt();
				// tasks.get(i).num_resources = x;
				for (int j = 0; j < x; j++) {
					String a = s.next();
					int m = s.nextInt();
					int l = s.nextInt();
					res_used.add(new Resource(a));
					resources.put(a, null);
					start_time.add(m);
					total_time.add(l);
				}
				tasks.add(new Tasks(temp, phase, period, deadline, execution,
						act_dead, 0, x, res_used, start_time, total_time));
				i++;
			}

			// System.out.println("Resources and Locks: "+resources);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long intime = System.currentTimeMillis();
System.out.println(intime);
		System.out.println("Time\tJob\tActions");
		// for every second until last second
		for (int timeinstance = 0; timeinstance <= timeline; timeinstance++) {

			try {
				ArrayList<Tasks> runningtasks = new ArrayList<Tasks>();
				// Check for arrival
				// locali is a variable for getting active tasks
				for (int locali = 0; locali < num_tasks; locali++) {
					if (tasks.get(locali).phase <= timeinstance) {
						if (tasks.get(locali).curr_exe_instance <= tasks
								.get(locali).execution) {
							if (tasks.get(locali).act_dead > timeinstance) {
								// get the active tasks
								runningtasks.add(tasks.get(locali));
							} else {
								// The task with actual deadline more than
								// current
								// time instance is not executed
							}
						}
					}

				}
				// System.out.println(runningtasks.get(0).curr_exe_instance+"f"+runningtasks.get(0).start_time.get(0));
				// Now that we have come up with list of tasks running, give
				// priority to them.

				// First give priority based on EDF

				String[] priority_queue = new String[runningtasks.size()];
				// Sort the running tasks using insertion sort for EDF task
				priority_queue = insertionsort(runningtasks);

				int[] priority_queue_index = new int[runningtasks.size()];
				// int[] priority_queue_values = new int[runningtasks.size()];

				for (int j = 0; j < priority_queue.length; j++) {
					for (int i = 0; i < runningtasks.size(); i++) {
						if (runningtasks.get(i).name
								.equalsIgnoreCase(priority_queue[j])) {
							priority_queue_index[j] = i;
						}
					}
				}
				// After you get the priority task name, check for resources

				int num_resources = runningtasks.get(priority_queue_index[0]).num_resources;

				int rcount;
				boolean whileflag = false;
				boolean ifflag = false;
				boolean chk_resource_usage = false;
				boolean chk_preemption = false;
				String[] resourcecount = new String[num_resources];
				String preemted_task = null;

				do {
					whileflag = false;
					ifflag = false;

					// Save the tasks which have the resource
					String[] task_lock = new String[num_resources];
					resourcecount = new String[num_resources];
					int res_num = 0;
					rcount = 0;
					for (res_num = 0; res_num < num_resources; res_num++) {

						// Check if current execution instance is between the
						// start
						// time and total time of resource
						// if not in this execution instance then skip for next
						// resource
						// System.out.println(runningtasks.get(0).curr_exe_instance+"f"+runningtasks.get(0).start_time.get(0));
						if (runningtasks.get(priority_queue_index[0]).curr_exe_instance >= runningtasks
								.get(priority_queue_index[0]).start_time
								.get(res_num)
								&& (runningtasks.get(priority_queue_index[0]).curr_exe_instance - runningtasks
										.get(priority_queue_index[0]).start_time
										.get(res_num)) < runningtasks
										.get(priority_queue_index[0]).total_time
										.get(res_num)) {

							// Check if resource is available, if not available
							// change the note the task which has it, and go to
							// next resource

							int x = 0;
							for (int i = 0; i < tasks.size(); i++) {
								if (tasks.get(i).name
										.equalsIgnoreCase(priority_queue[0])) {
									x = i;
								}
							}

							String resource_used = runningtasks
									.get(priority_queue_index[0]).res_used
									.get(res_num).Resource_name;

							if (resources.get(resource_used) == null
									|| resources.get(resource_used) == runningtasks
											.get(priority_queue_index[0]).name) {
								// System.out.println("Resource locked by: "+resources.get(resource_used));
								chk_resource_usage = true;
								rcount++;
								resourcecount[res_num] = resource_used;

								// Assign resource to this task
								// This is assigned after while loop as, then
								// only
								// we will get priority job
								// runningtasks.get(0).res_used.get(res_num).lockedBy
								// = priority_queue[0];
							} else {
								task_lock[res_num] = resources
										.get(resource_used);
								// System.out.println("This fellow has lock over"+resource_used+"= "+task_lock[res_num]);
								ifflag = true;
								whileflag = true;
							}
						} else if (runningtasks.size() > 1
								&& runningtasks.get(priority_queue_index[0]).act_dead == runningtasks
										.get(priority_queue_index[1]).act_dead) {
							whileflag = true;
							// move first element to last

							String current_task = priority_queue[0];
							for (int i = 1; i < priority_queue.length; i++) {
								priority_queue[i - 1] = priority_queue[i];
							}
							priority_queue[priority_queue.length - 1] = current_task;

							priority_queue_index = new int[runningtasks.size()];
							for (int j = 0; j < priority_queue.length; j++) {
								for (int i = 0; i < runningtasks.size(); i++) {
									if (runningtasks.get(i).name
											.equalsIgnoreCase(priority_queue[j])) {
										priority_queue_index[j] = i;
									}
								}
							}
							@SuppressWarnings("unused")
							int i = 0;
							num_resources = runningtasks
									.get(priority_queue_index[0]).num_resources;
							res_num = 0;
							break;
						}
					}
					// while condition checking
					// Now Check task_lock size, if >0 then change the priority
					// to
					// that task name and again check for lock
					if (ifflag) {
						chk_preemption = true;
						// System.out.println("Preemted");
						String current_task = priority_queue[0];
						for (int i = 1; i < runningtasks.size(); i++) {
							if (priority_queue[i] == task_lock[0]) {
								priority_queue[i] = current_task;
								priority_queue[0] = task_lock[0];
							}
						}
						preemted_task = current_task;
						priority_queue_index = new int[runningtasks.size()];
						for (int j = 0; j < priority_queue.length; j++) {
							for (int i = 0; i < runningtasks.size(); i++) {
								if (runningtasks.get(i).name
										.equalsIgnoreCase(priority_queue[j])) {
									priority_queue_index[j] = i;
								}
							}
						}
						num_resources = runningtasks
								.get(priority_queue_index[0]).num_resources;
					}

				} while (whileflag);

				// Now we have

				int x = 0;
				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).name.equalsIgnoreCase(priority_queue[0])) {
						x = i;
					}
				}
				if (chk_resource_usage) {
					for (int res_num = 0; res_num < rcount; res_num++) {
						String resource_used = runningtasks
								.get(priority_queue_index[0]).res_used
								.get(res_num).Resource_name;
						resources.put(resource_used,
								runningtasks.get(priority_queue_index[0]).name);
						// System.out.println("Locking Resource" + resource_used
						// + " by " + resources.get(resource_used));
					}
				}

				if (!chk_resource_usage) {
					String c = null;
					c = runningtasks.get(priority_queue_index[0]).name;
					System.out.print(timeinstance + "\t" + c);
				} else {
					System.out.print(timeinstance + "\t" + priority_queue[0]);
					String str = "locks ";
					for (int res_num = 0; res_num < rcount; res_num++) {
						str = str + resourcecount[res_num] + ",";
					}
					str = str.substring(0, str.length() - 1);
					System.out.print("\t" + str);
				}

				// for (int i = 0; i < runningtasks.size(); i++) {
				// System.out.print(priority_queue[i]);
				// }

				tasks.get(x).curr_exe_instance++;
				if (chk_resource_usage) {
					for (int res_num = 0; res_num < rcount; res_num++) {
						{
							if (tasks.get(x).curr_exe_instance
									- tasks.get(x).start_time.get(res_num) == tasks
										.get(x).total_time.get(res_num)) {
								String resource_used = runningtasks
										.get(priority_queue_index[0]).res_used
										.get(res_num).Resource_name;
								resources.put(resource_used, null);

								System.out.print("\t Unlock " + resource_used);

							}
						}
					}
				}

				if (chk_preemption) {
					System.out.println("\t Current Priority " + preemted_task);
				} else {
					System.out.println();
				}
				

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		long outtime = System.currentTimeMillis();
		System.out.println(outtime);
		System.out.println("Total Time: "+(outtime-intime));
	}// main method ends

	private static String[] insertionsort(ArrayList<Tasks> d_runningtasks) {
		// creating Hashtable for sorting
		Map<String, Integer> tasks_map = new HashMap<String, Integer>();
		for (int i = 0; i < d_runningtasks.size(); i++) {
			tasks_map.put(d_runningtasks.get(i).name,
					d_runningtasks.get(i).act_dead);
		}
		Map<String, Integer> sorted = sortByValues(tasks_map);
		String[] priority_tasks = new String[d_runningtasks.size()];
		int j = 0;
		for (String key : sorted.keySet()) {
			// System.out.println(key);
			if (j < d_runningtasks.size()) {
				priority_tasks[j] = key;
				j++;
			}

		}

		return priority_tasks;
	}

	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(
			Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}// main class ends