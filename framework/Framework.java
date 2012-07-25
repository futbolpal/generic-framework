package framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.UIManager;

import launcher.Launcher;

import framework.tasks.Task;
import framework.tasks.TaskPool;
import framework.ui.SplashScreenConfig;
import framework.ui.Window;

public abstract class Framework {
	protected ClassLoader loader_;

	protected ArrayList<FrameworkListener> listeners_;

	protected Framework() {
		loader_ = this.getClass().getClassLoader();
		listeners_ = new ArrayList<FrameworkListener>();
	}

	public void addListener(FrameworkListener l) {
		listeners_.add(l);
	}

	public void removeListener(FrameworkListener l) {
		listeners_.remove(l);
	}

	protected void fireFrameworkOpening() {
		Task t = new Task("Opening...") {
			public void run() {
				Iterator<FrameworkListener> itr = listeners_.iterator();
				while (itr.hasNext()) {
					itr.next().frameworkOpening();
				}
			}

		};
		TaskPool.instance().addTask(t);
	}

	protected void fireFrameworkOpened() {
		Task t = new Task("Opened") {
			public void run() {
				Iterator<FrameworkListener> itr = listeners_.iterator();
				while (itr.hasNext()) {
					itr.next().frameworkOpened();
				}
			}

		};
		TaskPool.instance().addTask(t);
	}

	protected void fireFrameworkClosing() {
	}

	public void launch() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModuleSystemLoader.instance().load();
	}

	public void preLaunch() {
		this.fireFrameworkOpening();
	}

	public void postLaunch() {
		this.fireFrameworkOpened();
	}

	public void firstLaunch() {

	}

	public boolean isFirstLaunch() {
		return false;
	}

	public String getJavaVersion() {
		return "1.6";
	}

	public Window getWindow() {

		return Window.instance();
	}

	public void loadJars(URL[] urls) {
		loader_ = URLClassLoader.newInstance(urls, loader_);
	}

	public ClassLoader getClassLoader() {
		return loader_;
	}

	public SplashScreenConfig getSplashConfig() {
		return new SplashScreenConfig(null, new ImageIcon(this.getClass()
				.getResource("ui/resources/splash.png")), false);
	}

	public void close() {
		this.getWindow().save();
		if (ModuleSystemLoader.instance().close())
			System.exit(0);

	}

	public abstract String getName();

	public abstract String getVersion();
}