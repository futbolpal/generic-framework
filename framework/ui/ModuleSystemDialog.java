package framework.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.JarFile;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import module_system.ModuleSystem;
import basic_gui.BasicDialog;
import framework.FrameworkProperties;

public class ModuleSystemDialog extends BasicDialog {
	private static final int WIDTH = 600;

	private static final int HEIGHT = 400;

	private JPanel modules_;

	private JScrollPane modules_view_;

	private ModuleView selected_;

	private ModuleSystem system_;

	public ModuleSystemDialog(ModuleSystem ms) {
		super(ms.getName() + " Manager", WIDTH, HEIGHT, null, null);
		system_ = ms;
		modules_ = new JPanel();
		modules_.setBackground(new Color(242, 242, 242));
		modules_.setLayout(new BoxLayout(modules_, BoxLayout.PAGE_AXIS));
		modules_.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selectionChanged(null);
			}
		});
		this.refresh();
		modules_view_ = new JScrollPane(modules_,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getMainPanel().setLayout(new BorderLayout());
		this.getMainPanel().add(modules_view_,BorderLayout.CENTER);

		JCheckBox confirm = new JCheckBox("Confirm Removal", this
				.isConfirmRemoval());
		confirm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				ModuleSystemDialog.this.setConfirmRemoval(((JCheckBox) e
						.getSource()).isSelected());
			}
		});

		JButton install = new JButton("Install...");
		install.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// new ModuleInstallationWizard(false);
			}
		});
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.addButton(confirm, LEFT_ALIGNMENT, false);
		this.addButton(close, RIGHT_ALIGNMENT, false);
		this.addButton(install, RIGHT_ALIGNMENT, false);
	}

	public void refresh() {
		modules_.removeAll();

		//
		// Add all of the modules that are queued for removal
		//
		try {
			// Read the properties file for any more modules, that will be
			// removed at
			// startup.
			ArrayList<String> jar_files = system_.getCleanupEntries();
			for (String file : jar_files) {
				JarFile jar = new JarFile(new File(file));
				final ModuleView v = this.createModuleView(jar);
				v.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						selectionChanged(v);
					}
				});
				v.setRemoveEnabled(false);
				v.setOptionsEnabled(false);
				modules_.add(v);
				JSeparator js = new JSeparator(JSeparator.HORIZONTAL);
				js.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
				modules_.add(js);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// Add all the loaded modules
		//
		for (String m : (Collection<String>) system_.getLoadedByName()) {
			JarFile jar = system_.getJarFor(m);
			final ModuleView v = this.createModuleView(jar);
			v.setAlignmentY(JPanel.TOP_ALIGNMENT);
			v.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					selectionChanged(v);
				}
			});
			boolean removed = false;
			for (Component c : modules_.getComponents()) {
				if (c.equals(v)) {
					removed = true;
				}
			}
			if (removed) {
				continue;
			}
			modules_.add(v);
			JSeparator js = new JSeparator(JSeparator.HORIZONTAL);
			js.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
			modules_.add(js);
		}

		modules_.repaint();
		modules_.revalidate();
	}

	public void selectionChanged(ModuleView new_v) {
		if (selected_ != null) {
			selected_.setSelected(false);
		}
		if (new_v != null) {
			selected_ = new_v;
			selected_.setSelected(true);
		} else {
			selected_ = null;
		}
	}

	public ModuleSystem getSystem() {
		return system_;
	}

	public boolean isConfirmRemoval() {
		return Boolean.valueOf(FrameworkProperties.instance().getProperty(""));
	}

	public void setConfirmRemoval(boolean flag) {
		FrameworkProperties.instance().setProperty(
				"module." + system_.getName() + ".", String.valueOf(flag));
	}

	public ModuleView createModuleView(JarFile jar) {
		return new ModuleView(this, jar);
	}
}
