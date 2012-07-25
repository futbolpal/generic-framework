package framework.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import framework.tasks.TaskPool;

public class TaskPoolMonitor extends JPanel implements ActionListener {

	private Timer timer_;

	private JProgressBar bar_view_;

	private JLabel size_view_;

	private JLabel time_view_;

	private JLabel text_view_;

	private JComponent current_;

	private int view_;

	public TaskPoolMonitor() {
		bar_view_ = new JProgressBar();
		size_view_ = new JLabel();
		time_view_ = new JLabel();
		text_view_ = new JLabel();

		view_ = -1;

		timer_ = new Timer(100, this);

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2)
					changeView();
				else {
					// TODO: Show a dialog?
				}
			}
		});
		this.addContainerListener(new ContainerListener() {

			public void componentAdded(ContainerEvent e) {
				timer_.start();

			}

			public void componentRemoved(ContainerEvent e) {
				timer_.stop();
			}
		});
		final JButton show_tasks = new JButton("^");
		show_tasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * final int width = TaskPoolMonitor.this.getWidth(); final
				 * JPanel task_view = new JPanel(); final JPopupMenu viewer =
				 * new JPopupMenu(); task_view.setLayout(new
				 * BoxLayout(task_view, BoxLayout.Y_AXIS));
				 * task_view.setOpaque(false); Timer t = new Timer(100, new
				 * ActionListener() { public void actionPerformed(ActionEvent e) {
				 * task_view.removeAll(); if (viewer.isVisible() &&
				 * TaskPool.instance().getTaskCount() > 0) { ArrayList<Task>
				 * tasks = TaskPool.instance() .getTasks(); for (Task t : tasks) {
				 * TaskMonitor tm = new TaskMonitor(t); tm.setPreferredSize(new
				 * Dimension(width - 25, 25)); task_view.add(tm, 0); }
				 * viewer.add(task_view); viewer.pack(); viewer.revalidate();
				 * viewer.show(show_tasks, -viewer.getWidth(), -viewer
				 * .getHeight()); } else { ((Timer) e.getSource()).stop();
				 * viewer.setVisible(false); } } }); t.start();
				 * viewer.show(show_tasks, 0, 0);
				 */
			}

		});
		this.setLayout(new BorderLayout());
		this.add(show_tasks, BorderLayout.EAST);
		this.changeView();
		this.setPreferredSize(new Dimension(300, 25));
	}

	public void changeView() {
		view_++;
		if (current_ != null) {
			this.remove(current_);
		}
		switch (view_) {
		case 0:
			current_ = bar_view_;
			break;
		case 1:
			current_ = size_view_;
			break;
		case 2:
			current_ = time_view_;
			break;
		case 3:
			current_ = text_view_;
			break;
		default:
			view_ = -1;
			changeView();
			break;
		}
		this.add(current_, BorderLayout.CENTER);
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		int tasks = TaskPool.instance().getTaskCount();
		double progress = TaskPool.instance().calculatePercentDone() * 100.0;
		double average = TaskPool.instance().calculateAverageTime();

		bar_view_.setIndeterminate(progress < 0);
		bar_view_.setValue((int) progress);
		bar_view_.setMaximum(100);
		bar_view_.setStringPainted(true);

		size_view_.setText("Tasks: " + tasks);

		if (progress > 0 && average > 0) {
			double total_time = average * 100;
			double time_spent = average * progress;
			double time_remaining = total_time - time_spent;
			String progress_str = String.valueOf(time_remaining / 6000);
			if (progress_str.length() > 5) {
				progress_str = new String(progress_str.substring(0, 5));
			}
			time_view_.setText("Time Remaining: " + progress_str + " minutes");
		} else if (tasks > 0) {
			time_view_.setText("Cannot make time estimate");
		} else {
			time_view_.setText("No tasks running");
		}

		String progress_str = String.valueOf(progress);
		if (progress_str.length() > 5) {
			progress_str = new String(progress_str.substring(0, 5));
		}
		text_view_.setText("Tasks: " + TaskPool.instance().getTaskCount()
				+ " Progress: "
				+ ((progress >= 0) ? progress_str + "%" : "Unknown"));

	}
}
