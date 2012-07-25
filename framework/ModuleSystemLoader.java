package framework;

import java.util.Hashtable;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;

import module_system.ModuleSystem;
import basic_gui.BasicDialog;
import framework.ui.ModuleSystemDialog;
import framework.ui.SplashScreen;

public final class ModuleSystemLoader extends ModuleSystem<ModuleSystem> {
	private static ModuleSystemLoader this_;

	private ModuleSystemLoader() {
	}

	public void load() {
		SplashScreen.instance().addLoadIcon(
				"Java " + System.getProperty("java.version"),
				new ImageIcon(this.getClass().getResource(
						"ui/resources/java_icon.png")));
		SplashScreen.instance().addLoadIcon(
				"Generic Framework",
				new ImageIcon(this.getClass().getResource(
						"ui/resources/system_icon.png")));
		super.load();
		for (ModuleSystem ms : this.getChildren()) {
			ms.load();
		}
	}

	public String getDirectory() {
		return FileSystem.getInstallDirectory() + "system/";
	}

	public String getSystemType() {
		return "Module System";
	}

	public ModuleSystemDialog getDialog() {
		return null;
	}

	public void runInstallScript(JarFile jar) {
		// do nothing
	}

	public void runUninstallScript(JarFile jar) {
		// do nothing
	}

	public boolean checkForUpdate() {

		return false;
	}

	public void update() {
	}

	public BasicDialog getOptionsDialog() {
		return null;
	}

	public String getName() {
		return "System Module Loader";
	}

	public String getChildType() {
		return "Module System";
	}

	public boolean isHidden() {
		return true;
	}

	public static ModuleSystemLoader instance() {
		if (this_ == null) {
			this_ = new ModuleSystemLoader();
		}
		return this_;
	}

	public ImageIcon getIcon() {
		return null;
	}
}
