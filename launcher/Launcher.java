package launcher;

import framework.Framework;
import framework.ui.SplashScreen;

public class Launcher {
	private static Launcher this_;

	private Framework framework_;

	private boolean is_set_;

	protected Launcher() {
	}

	public void setFramework(Framework f) {
		if (!is_set_) {
			framework_ = f;
			is_set_ = true;
		}
	}

	public Framework getFramework() {
		return framework_;
	}

	public void run() {
		SplashScreen.instance().setConfig(framework_.getSplashConfig());
		SplashScreen.instance().setVisible(true);
		framework_.preLaunch();
		framework_.launch();
		framework_.postLaunch();
		SplashScreen.instance().setVisible(false);
	}

	public static Launcher instance() {
		if (this_ == null) {
			this_ = new Launcher();
		}
		return this_;
	}
}
