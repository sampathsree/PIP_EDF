package rtes;

public class Active_Jobs {
	int arrival_time;
	String task;
	int task_number;
	int job_id;
	int abs_deadline;
	int exe_done;
	int exe_time;
	int current_priority;
	int assigned_priority;
	boolean res;
	int res_exe;

	public Active_Jobs(String x, int i, int a, int b, int c) {
		arrival_time = a;
		abs_deadline = a + b;
		exe_time = c;
		task_number = i;
		task = x;
		job_id++;
	}

	void setTask(String a) {
		task = a;
	}

	void setJob(int x) {
		x = job_id;
	}

	void setDeadline(int x) {
		x = abs_deadline;
	}

	void setExe(int x) {
		x = exe_done;
	}

	String getTask() {
		return task;
	}

	int getJob() {
		return job_id;
	}

	int getDeadline() {
		return abs_deadline;
	}

	int getExe() {
		return exe_done;
	}
}
