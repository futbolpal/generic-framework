package framework.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import launcher.Launcher;
import module_system.Module;
import module_system.ModuleSystem;
import framework.ModuleSystemLoader;

public class WindowMenuBar extends JMenuBar {
	private JMenu file_;

	private JMenu options_;

	private JMenu help_;

	public WindowMenuBar() {
		file_ = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Launcher.instance().getFramework().getWindow().dispose();
			}
		});
		file_.add(exit);
		this.add(file_);

		options_ = new JMenu("Options");
		options_.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuSelected(MenuEvent e) {
				JMenu options = (JMenu) e.getSource();
				int add_index;
				// Clean menu
				for (Component c : options.getMenuComponents()) {
					String name = c.getName();
					if (name != null && name.startsWith("system.")) {
						options.remove(c);
					}
				}
				// Build menu

				JSeparator js = new JSeparator();
				js.setName("system.separator");
				options.add(js, 0);
				for (Module m : ModuleSystemLoader.instance().getChildren()) {
					final ModuleSystem ms = (ModuleSystem) m;
					JMenuItem system = new JMenuItem("Manage "
							+ ms.getChildType() + "s...");
					system.setName("system." + ms.getName());
					system.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ms.getOptionsDialog().display();
						}
					});
					options.add(system, 0);
				}
			}
		});
		this.add(options_);

		help_ = new JMenu("Help");
		this.add(help_);
	}

	public void addMenuItem(JMenu m, JMenuItem i) {
		m.add(i);
	}

	public JMenu getFileMenu() {
		return file_;
	}

	public JMenu getHelpMenu() {
		return help_;
	}

	public JMenu getOptionsMenu() {
		return options_;
	}

	public JMenu getMenuByTitle(String t) {
		for (int i = 0; i < this.getMenuCount(); i++) {
			JMenu m = this.getMenu(i);
			if (m.getText().equals(t)) {
				return m;
			}
		}
		return null;
	}
}
