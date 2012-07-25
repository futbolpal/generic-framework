package module_system;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;

import launcher.Launcher;
import basic_gui.BasicDialog;
import framework.FileSystem;
import framework.FrameworkProperties;
import framework.ObjectFactory;
import framework.ui.ModuleSystemDialog;
import framework.ui.SplashScreen;

public abstract class ModuleSystem<T extends Module> implements Module {
	private TreeMap<String, JarFile> jar_index_;

	private TreeMap<JarFile, T> instance_index_;

	protected ModuleSystem() {
		Launcher.instance().getFramework().addListener(this);
		jar_index_ = new TreeMap<String, JarFile>();
		instance_index_ = new TreeMap<JarFile, T>(new Comparator<JarFile>() {
			public int compare(JarFile j1, JarFile j2) {
				String s1 = getName(j1).trim();
				String s2 = getName(j2).trim();
				return s1.compareTo(s2);
			}
		});
	}

	private static String getModuleAttribute(JarFile jar, String attr) {
		try {
			return jar.getManifest().getMainAttributes().getValue(attr);
		} catch (IOException e) {
			return null;
		}
	}

	public static String getClass(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-class");
	}

	public static String getInstaller(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-installer");
	}

	public static String getName(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-name");
	}

	public static String getDescription(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-description");
	}

	public static String getVersion(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-version");
	}

	public static String getIcon(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-icon");
	}

	public static String getType(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-type");
	}

	public static String getDeveloper(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "module-developer");
	}

	public static String getRequiredJRE(JarFile jar) {
		return ModuleSystem.getModuleAttribute(jar, "required-jre-version");
	}

	public boolean validate(JarFile jar) {
		String name = ModuleSystem.getName(jar);
		String main = ModuleSystem.getClass(jar);
		String type = ModuleSystem.getType(jar);
		return !(name == null || main == null)
				&& type.equals(this.getChildType());
	}

	private int getCleanupCount() {
		return new Integer(FrameworkProperties.instance().getProperty(
				"module.cleanup.queue-size", "0"));
	}

	public void addCleanupEntry(JarFile jar) {
		String path = jar.getName();
		int count = this.getCleanupCount();
		FrameworkProperties.instance().setProperty(
				"module.cleanup.entry." + count, path);
		count++;
		FrameworkProperties.instance().setProperty("module.cleanup.queue-size",
				String.valueOf(count));
	}

	public void removeCleanupEntry(JarFile jar) {
		int count = this.getCleanupCount();
		count--;
		FrameworkProperties.instance().setProperty("module.cleanup.queue-size",
				String.valueOf(count));
	}

	public ArrayList<String> getCleanupEntries() {
		int count = this.getCleanupCount();
		ArrayList<String> entries = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			entries.add(FrameworkProperties.instance().getProperty(
					"module.cleanup.entry." + i));
		}
		return entries;
	}

	private void performCleanup() {
		try {
			ArrayList<String> entries = this.getCleanupEntries();
			for (String s : entries) {
				JarFile jar = new JarFile(s);
				this.performCleanup(jar);

				//
				// Remove the cleanup entry
				//
				this.removeCleanupEntry(jar);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void performCleanup(JarFile jar) {
		try {
			//
			// Run Installation Script
			//
			String install_file = ModuleSystem.getInstaller(jar);
			if (install_file != null) {
				this.runUninstallScript(jar);
			}

			//
			// Close the jar
			//
			jar.close();

			//
			// Delete it
			//
			new File(jar.getName()).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isEmpty() {
		return jar_index_.isEmpty();
	}

	public JarFile getJarFor(String s) {
		return jar_index_.get(s);
	}

	public Collection<String> getLoadedByName() {
		return jar_index_.keySet();
	}

	public Collection<JarFile> getLoadedByJar() {
		return jar_index_.values();
	}

	public boolean close() {
		for (T module : instance_index_.values()) {
			System.out.println("Closing: " + module.getClass());
			if (!module.close())
				return false;
		}
		return true;
	}

	public void load() {
		//
		// Preload
		//
		this.performCleanup();

		//
		// Get all jar files in the lib directory
		//
		File[] jars = new File(this.getDirectory()).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".jar");
			}

		});
		if (jars == null) {
			return;
		}

		//
		// Convert the files to URLS and store jar information
		//
		URL[] urls = new URL[jars.length];
		for (int i = 0; i < urls.length; i++) {
			try {
				urls[i] = jars[i].toURI().toURL();
				JarFile jar = new JarFile(jars[i]);
				if (validate(jar)) {
					String classname = ModuleSystem.getClass(jar);
					jar_index_.put(classname, jar);
				} else {
					System.out.println("Invalid jar " + jar.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//
		// Load jars
		//
		Launcher.instance().getFramework().loadJars(urls);

		//
		// Create module instances
		//
		for (JarFile jar : this.getLoadedByJar()) {
			System.out.println("Starting " + jar.getName());
			String class_name = ModuleSystem.getClass(jar);
			T p = (T) ObjectFactory.getInstance(class_name);
			if (!(p instanceof ModuleSystem)) {
				SplashScreen.instance().addLoadIcon(ModuleSystem.getName(jar),
						(ImageIcon) p.getIcon());
			}
			this.putChild(jar, p);
		}

	}

	public void install(JarFile jar) {
		try {
			//
			// Run Installation Script
			//
			String install_file = ModuleSystem.getInstaller(jar);
			if (install_file != null) {
				this.runInstallScript(jar);
			}

			//
			// Copy the jar itself
			//
			FileSystem.copyFile(new FileInputStream(new File(jar.getName())),
					new File(this.getDirectory()
							+ new File(jar.getName()).getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uninstall(JarFile jar) {
		//
		// add to cleaup queue.
		//
		this.addCleanupEntry(jar);
	}

	public BasicDialog getOptionsDialog() {
		return new ModuleSystemDialog(this);
	}

	protected void putChild(JarFile key, T m) {

		instance_index_.put(key, m);
	}

	public Module getChild(Object key) {
		return instance_index_.get(key);
	}

	public Collection<T> getChildren() {
		return instance_index_.values();
	}

	public ImageIcon getIcon() {
		return null;
	}

	public void frameworkOpening() {
	}

	public void frameworkOpened() {

	}

	public abstract String getChildType();

	public abstract String getDirectory();

	public abstract boolean isHidden();

	public abstract void runInstallScript(JarFile jar);

	public abstract void runUninstallScript(JarFile jar);

}
