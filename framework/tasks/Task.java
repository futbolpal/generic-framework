package framework.tasks;

import java.io.Serializable;

public abstract class Task implements Runnable, Serializable {
	private String name_;

	protected int progress_;

	protected int length_;

	private String note_;

	private boolean done_;

	protected double average_time_;

	public Task(String name) {
		this(name, -1);
		this.done_ = false;
	}

	public Task(String name, int length) {
		name_ = name;
		length_ = length;
	}

	public int getProgress() {
		return progress_;
	}

	public double getAverageTime() {
		return average_time_;
	}

	public int getLength() {
		return length_;
	}

	public String getName() {
		return name_;
	}

	public String getNote() {
		return note_;
	}

	public boolean isIndeterminate() {
		return length_ < 0;
	}

	public boolean isDone() {
		if (length_ > 0) {
			return done_ || !(progress_ < length_);
		} else
			return done_;
	}

	public void setProgress(int i) {
		progress_ = i;
	}

	public void setNote(String n) {
		note_ = n;
	}

	public void setName(String n) {
		name_ = n;
	}

	public void setLength(int i) {
		length_ = i;
	}

	public void setDone(boolean flag) {
		this.done_ = flag;
	}

	public void done() {
	}

	public void init()
	{
		this.done_ = false;
		this.progress_ = 0;
	}

	public void kill() {
		this.setLength(Integer.MAX_VALUE);
		this.setDone(true);
	}

}
