package framework.tasks;

import java.util.ArrayList;

public class OldTaskPool {
	private static OldTaskPool this_;

	private ArrayList<Task> tasks_;

	private OldTaskPool() {
		tasks_ = new ArrayList<Task>();
	}

	public synchronized int getTaskCount() {
		return tasks_.size();
	}

	public Task getCurrentTask() {
		if (tasks_.isEmpty())
			return null;
		return tasks_.get(0);
	}

	public synchronized double calculatePercentDone() {
		int count = 0;
		double average = 0;
		for (Task t : tasks_) {
			if (t.isIndeterminate())
				return -1;
			average = (average * count + ((double) t.getProgress() / (double) t
					.getLength()))
					/ ++count;
		}
		return average;
	}

	public synchronized double calculateAverageTime() {
		int count = 0;
		double average = 0;
		for (Task t : tasks_) {
			if (t.isIndeterminate())
				return -1;
			average = (average * count + t.getAverageTime()) / (++count);
		}
		return average;
	}

	public ArrayList<Task> getTasks() {
		return this.tasks_;
	}

	public synchronized void addTask(Task t) {
		new Thread(t).start();
	}

	public synchronized void removeTask(Task t) {
		tasks_.remove(t);
	}

	public synchronized void clean() {

		ArrayList<Task> removals = new ArrayList<Task>();
		for (Task t : tasks_) {
			if (t.isDone())
				removals.add(t);
		}

		for (Task t : removals) {
			tasks_.remove(t);
		}
		removals = null;
	}

	public static OldTaskPool instance() {
		if (this_ == null)
			this_ = new OldTaskPool();
		return this_;
	}
}
