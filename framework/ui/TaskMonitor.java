package framework.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import framework.tasks.Task;

public class TaskMonitor extends JPanel implements ActionListener {
	private Timer timer_;

	private Task task_;

	private JProgressBar progress_;

	public TaskMonitor(Task t) {
		task_ = t;
		timer_ = new Timer(100, this);
		progress_ = new JProgressBar();
		progress_.setStringPainted(true);
		this.setLayout(new BorderLayout());
		this.add(progress_, BorderLayout.CENTER);
	}

	public void setTask(Task t) {
		task_ = t;
	}

	public Task getTask() {
		return task_;
	}

	public void actionPerformed(ActionEvent e) {
		if (task_.getNote() != null) {
			progress_.setString(task_.getNote());
		}
		progress_.setIndeterminate(task_.isIndeterminate());
		progress_.setValue(task_.getProgress());
		progress_.setMaximum(task_.getLength());
	}

	public void showNote(boolean flag)
	{
		progress_.setStringPainted(flag);
	}

	public void done()
	{
		progress_.setValue(progress_.getMaximum());
		timer_.stop();
	}

	public void start() {
		timer_.start();
	}

	public void stop()
	{
		timer_.stop();
	}
}
