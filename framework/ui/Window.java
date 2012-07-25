package framework.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import launcher.Launcher;
import framework.FrameworkProperties;
import framework.tasks.TaskPool;

public abstract class Window extends JFrame {
	private WindowStatusBar status_;

	protected static Window instance_;

	protected Window() {
		boolean maximized = Boolean.valueOf(FrameworkProperties.instance()
				.getProperty("framework.window.maximized", "true"));
		if (maximized) {
			String osName = System.getProperty("os.name");
			if (osName.equals("Linux")) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screenSize = toolkit.getScreenSize();
				this.setSize(screenSize);
			} else {
				this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		} else {
			int w = Integer.valueOf(FrameworkProperties.instance().getProperty(
					"framework.window.w", "-1"));
			int h = Integer.valueOf(FrameworkProperties.instance().getProperty(
					"framework.window.h", "-1"));
			int x = Integer.valueOf(FrameworkProperties.instance().getProperty(
					"framework.window.x", "-1"));
			int y = Integer.valueOf(FrameworkProperties.instance().getProperty(
					"framework.window.y", "-1"));
			this.setSize(new Dimension(w, h));
			if (x < 0 || y < 0) {
				this.setLocationRelativeTo(this);
			} else {
				this.setLocation(x, y);
			}
		}
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle(Launcher.instance().getFramework().getName());
	}

	/**
	 * Updates the window
	 */
	public void refresh() {
	}

	/**
	 * @return The menu bar
	 */
	public WindowMenuBar getWindowMenuBar() {
		return (WindowMenuBar) this.getJMenuBar();
	}

	public WindowStatusBar getWindowStatusBar() {
		return status_;
	}

	public void setWindowStatusBar(WindowStatusBar w) {
		status_ = w;
		this.add(status_, BorderLayout.SOUTH);
	}

	public boolean hasStatusBar() {
		return status_ != null;
	}

	/**
	 * Saves the window
	 */
	public void save() {
		String x = String.valueOf(this.getX());
		String y = String.valueOf(this.getY());
		String w = String.valueOf(this.getWidth());
		String h = String.valueOf(this.getHeight());
		String m = String
				.valueOf(this.getExtendedState() == JFrame.MAXIMIZED_BOTH);

		FrameworkProperties.instance().setProperty(
				"framework.window.maximized", m);
		FrameworkProperties.instance().setProperty("framework.window.x", x);
		FrameworkProperties.instance().setProperty("framework.window.y", y);
		FrameworkProperties.instance().setProperty("framework.window.w", w);
		FrameworkProperties.instance().setProperty("framework.window.h", h);
	}

	public static Window instance() {
		if (instance_ == null) {
		}
		return instance_;
	}

	public static void setInstance(Window w) {
		instance_ = w;
	}
}