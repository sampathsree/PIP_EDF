package realtime;

//import java.io.BufferedInputStream;
import java.io.File;
//import java.io.FileInputStream;
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

import realtime.Active_Jobs;
import realtime.Tasks;

public class Main {

	public static void main(String[] argv) {

		ArrayList<Tasks> tasks = new ArrayList<Tasks>();
		// ArrayList<Resource> Res = new ArrayList<Resource>();
		int timeline = 0, num_tasks = 0;
		// int[] next_arrival;

		Scanner s;
		try {
			// BufferedInputStream s=new BufferedInputStream(new
			// FileInputStream(new File("a.txt"));
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
					start_time.add(m);
					total_time.add(l);
				}
				tasks.add(new Tasks(temp, phase, period, deadline, execution,
						act_dead,0, x, res_used, start_time, total_time));
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// int small = 0;
		// int small_index;
		// // initialize next arrival
		// next_arrival = new int[num_tasks];
		// for (int i = 0; i < num_tasks; i++) {
		// next_arrival[i] = tasks.get(i).phase;
		// }

		// for (int i = 0; i < num_tasks; i++) {
		// System.out.println("next arrival[" + i + "] = " + next_arrival[i]);
		// System.out.println("period[" + i + "] = " + tasks.get(i).period);
		// System.out.println("phase[" + i + "] = " + tasks.get(i).phase);
		// System.out
		// .println("deadline[" + i + "] = " + tasks.get(i).deadline);
		// System.out.println("execution time[" + i + "] = "
		// + tasks.get(i).execution);
		// }

		// ArrayList<Active_Jobs> active = new ArrayList<Active_Jobs>();

		// for every second until last second
		for (int timeinstance = 0; timeinstance <= timeline; timeinstance++) {

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
							// The task with actual deadline more than current
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

			do {
				whileflag = false;
				ifflag = false;
				
				// Save the tasks which have the resource
				String[] task_lock = new String[num_resources];
				resourcecount = new String[num_resources];
				int res_num = 0;
				rcount = 0;
				for (res_num = 0; res_num < num_resources; res_num++) {

					// Check if current execution instance is between the start
					// time and total time of resource
					// if not in this execution instance then skip for next
					// resource
					// System.out.println(runningtasks.get(0).curr_exe_instance+"f"+runningtasks.get(0).start_time.get(0));
					if (runningtasks.get(priority_queue_index[0]).curr_exe_instance >= runningtasks
							.get(priority_queue_index[0]).start_time
							.get(res_num)
							&& (runningtasks.get(priority_queue_index[0]).curr_exe_instance - 
									runningtasks.get(priority_queue_index[0]).start_time.get(res_num))
									< runningtasks.get(priority_queue_index[0]).total_time
									.get(res_num)) {

						// Check if resource is available, if not available
						// change the note the task which has it, and go to
						// next resource
						
						int x=0;
						for (int i = 0; i < tasks.size(); i++) {
							if (tasks.get(i).name.equalsIgnoreCase(priority_queue[0])) {
								x=i;
							}
						}
						
						if (runningtasks.get(priority_queue_index[0]).res_used
								.get(res_num).lockedBy == null
								|| tasks.get(x).res_used
										.get(res_num).lockedBy == runningtasks
										.get(priority_queue_index[0]).name) {
							System.out.println("Check Resource Available"+tasks
									.get(2).res_used
									.get(res_num).lockedBy);
							chk_resource_usage = true;
							rcount++;
							resourcecount[res_num] = tasks
									.get(x).res_used
									.get(res_num).Resource_name;

							// Assign resource to this task
							// This is assigned after while loop as, then only
							// we will get priority job
							// runningtasks.get(0).res_used.get(res_num).lockedBy
							// = priority_queue[0];
						} else {
							task_lock[res_num] = runningtasks
									.get(priority_queue_index[0]).res_used
									.get(res_num).lockedBy;
							System.out.println(runningtasks
									.get(priority_queue_index[0]).res_used
									.get(res_num).lockedBy);
							ifflag = true;
							whileflag = true;
						}
					} else if (runningtasks.size() > 1
							&& runningtasks.get(priority_queue_index[0]).act_dead == runningtasks
									.get(priority_queue_index[1]).act_dead) {
						whileflag = true;
						// move first element to last

						// TODO
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
						num_resources = runningtasks.get(priority_queue_index[0]).num_resources;
						//res_num = 0;
					}
				}
				// while condition checking
				// whilecheck = task_lock.length;
				// Now Check task_lock size, if >0 then change the priority to
				// that task name and again check for lock
				if (ifflag) {
					chk_preemption = true;
					System.out.println("Preemted");
					String current_task = priority_queue[0];
					for (int i = 1; i < runningtasks.size(); i++) {
						if (priority_queue[i] == task_lock[0]) {
							priority_queue[i] = current_task;
							priority_queue[0] = task_lock[0];
						}
					}
					// String[] priority_queue2 = new
					// String[runningtasks.size()];
					// updating priority queue
					// for (int i = 0; i < runningtasks.size(); i++) {
					// if (i != task_lock.length) {
					// priority_queue2[i] = priority_queue[i];
					// } else {
					// priority_queue2[i] = current_task;
					// }
					// }
					// priority_queue = priority_queue2;

					// for(int i=1;i<priority_queue.length;i++){
					// priority_queue[i-1] = priority_queue[i];
					// }
					// priority_queue[priority_queue.length-1] = current_task;
					priority_queue_index = new int[runningtasks.size()];
					for (int j = 0; j < priority_queue.length; j++) {
						for (int i = 0; i < runningtasks.size(); i++) {
							if (runningtasks.get(i).name
									.equalsIgnoreCase(priority_queue[j])) {
								priority_queue_index[j] = i;
							}
						}
					}
					num_resources = runningtasks.get(priority_queue_index[0]).num_resources;
				}

			} while (whileflag);

			// Now we have


			int x=0;
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i).name.equalsIgnoreCase(priority_queue[0])) {
					x=i;
				}
			}
			if(chk_resource_usage){
			for (int res_num = 0; res_num < rcount; res_num++) {
				tasks.get(x).res_used.get(res_num).lockedBy = tasks.get(x).name;
				System.out.println("Getting Resource"+ tasks.get(x).res_used.get(res_num).Resource_name);
			}}
//			for (int i = 0; i < tasks.size(); i++) {
//				if (tasks.get(i).name.equalsIgnoreCase(priority_queue[0])) {
//					tasks.get(i).curr_exe_instance++;
//				}
//			}
			
			System.out.println("Locked by "+ tasks.get(x).res_used.get(0).lockedBy);
			// for(int i = 0; i < priority_queue.length; i++){
			// System.out.println(priority_queue[i]);
			// }
			// System.out.println(priority_queue);
			if (!chk_resource_usage) {
				String c = null;
				c = runningtasks.get(priority_queue_index[0]).name;
				System.out.println(timeinstance + "\t" + c);
			} else {
				System.out.print(timeinstance + "\t" + priority_queue[0]);
				String str = "locks ";
				for (int res_num = 0; res_num < resourcecount.length; res_num++) {
					str = str + resourcecount[res_num] + ",";
				}
				str.substring(0, str.length() - 1);
				System.out.print("\t" + str);
			}
			if (chk_preemption) {
				System.out.println("Preemted"+priority_queue[1]);
			} else {
				System.out.println();
			}
			
			tasks.get(x).curr_exe_instance++;
			if(chk_resource_usage){
			for (int res_num = 0; res_num < rcount; res_num++) {
				{
//					runningtasks.get(priority_queue_index[0]).curr_exe_instance - 
//					runningtasks.get(priority_queue_index[0]).start_time.get(res_num))
//					< runningtasks.get(priority_queue_index[0]).total_time
//					.get(res_num)
					
					
					if (tasks.get(x).curr_exe_instance-tasks.get(x).start_time.get(res_num) == tasks
						.get(x).total_time.get(res_num)) {
					System.out.println("Unlock"+tasks.get(x).res_used
							.get(res_num).lockedBy);
						tasks.get(x).res_used
							.get(res_num).lockedBy = null;
				}
			}
			}
			}
		}

		// // based on t set arrival times
		// for (int i = 0; i < num_tasks; i++) {
		// if (next_arrival[i] == timeinstance) {// Always a task should
		// // start at zero
		// active.add(new Active_Jobs(tasks.get(i).name, i,
		// timeinstance, tasks.get(i).deadline,
		// tasks.get(i).period));
		// next_arrival[i] += tasks.get(i).period;
		// }
		// }
		//
		// if (active.size() != 0) {
		// small = active.get(0).abs_deadline;
		// }
		// small_index = -1;
		//
		// for (int k = 0; k < active.size(); k++) {
		// small_index = 0;
		// for (int i = k + 1; i < active.size(); i++) {
		// // compare absolute deadlines
		// if (active.get(i).abs_deadline < small) {
		// small = active.get(i).abs_deadline;
		// small_index = i;
		// } else if (small == active.get(i).abs_deadline) {
		// if (active.get(i).arrival_time < active
		// .get(small_index).arrival_time) {
		// small = active.get(i).abs_deadline;
		// small_index = i;
		// } else if (active.get(i).arrival_time == active
		// .get(small_index).arrival_time) {
		// if (active.get(i).task_number < active
		// .get(small_index).task_number) {
		// small = active.get(i).abs_deadline;
		// small_index = i;
		// }
		// }
		// }
		// }
		// active.get(small_index).assigned_priority = k + 1;
		// }
		//
		// // job supposed to be executing
		// for (int i = 0; i < active.size(); i++) {
		// if (active.get(i).assigned_priority == 1)
		// small_index = i;
		// }
		//
		// // check if job requires resources
		// int check = active.get(small_index).task_number;
		// int temp = tasks.get(check).res_used.size();
		//
		// int done = active.get(small_index).exe_done;
		//
		// // for every resource, check the start time
		// for (int i = 0; i < temp; i++) {
		// int b = tasks.get(check).start_time.get(i);
		//
		// // if it requires, allocate resource if free or already holding
		// // and lock the resource
		// if (done == b
		// && (tasks.get(check).res_used.get(i).lockedBy == null || tasks
		// .get(check).res_used.get(i).lockedBy == active
		// .get(i))) {
		// tasks.get(check).res_used.get(i).lockedBy = active.get(i);
		// }
		//
		// // if the resource is not free, update the priority of the job
		// // blocking it
		// else {
		// int index = tasks.get(check).res_used.indexOf(tasks
		// .get(check).res_used.get(small_index).lockedBy);
		// active.get(index).current_priority = active
		// .get(small_index).current_priority;
		// }
		// // and execute that job
		//
		// // update the execution status for every job if there is any job
		// // executed
		// if (small_index != -1) {
		// active.get(small_index).exe_done += 1;
		// }
		//
		// // check if the resource has been released, and update the
		// // priority
		// if (active.get(small_index).exe_done == tasks.get(check).total_time
		// .get(i)) {
		// tasks.get(check).res_used.get(i).lockedBy = null;
		// active.get(small_index).current_priority = active
		// .get(small_index).assigned_priority;
		// }
		// }
		//
		// // remove job from ArrayList if it has missed deadline or done with
		// // execution
		// if (active.get(small_index).exe_done ==
		// active.get(small_index).exe_time) {
		// active.remove(small_index);
		// }
		// for (int k = 0; k < active.size(); k++) {
		// if (active.get(k).abs_deadline == timeinstance) {
		// active.remove(k);
		// }
		// }
		//
		// System.out.println("  " + active.get(small_index).task
		// + active.get(small_index).job_id);//
		// tasks.get(small_index).res_used.get(check).Resource_name);
		// }// seconds loop ends here
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