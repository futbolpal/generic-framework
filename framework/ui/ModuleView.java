package framework.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.jar.JarFile;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import module_system.Module;
import module_system.ModuleSystem;
import basic_gui.LinkButton;
import framework.Framework;
import framework.ObjectFactory;

public class ModuleView extends JPanel {
	private static final int WIDTH = 570;

	private String name_;

	private ModuleSystemDialog owner_;

	private JarFile jar_;

	private boolean selected_;

	private LinkButton remove_;

	private LinkButton options_;

	public ModuleView(ModuleSystemDialog owner, JarFile jar) {
		owner_ = owner;
		jar_ = jar;

		name_ = ModuleSystem.getName(jar);
		String classname = ModuleSystem.getClass(jar);
		String description = ModuleSystem.getDescription(jar);
		String icon = ModuleSystem.getIcon(jar);
		Module instance = (Module) ObjectFactory.getInstance(classname);

		selected_ = false;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBackground(new Color(242, 242, 242));
		this.setMaximumSize(new Dimension((int) owner.getPreferredSize()
				.getWidth(), 100));
		this.setAlignmentY(TOP_ALIGNMENT);

		ImageIcon img_icon = instance.getIcon();
		if (img_icon == null) {
			img_icon = new ImageIcon(Framework.class
					.getResource("ui/resources/default_icon.png"));
		}
		JLabel icon_label = new JLabel(img_icon);
		icon_label.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		this.add(Box.createHorizontalStrut(10));
		this.add(icon_label);
		this.add(Box.createHorizontalStrut(10));

		if (name_ == null) {
			name_ = "Generic Framework Module";
		}
		if (description == null) {
			description = "This is a module that is compatible with Generic Framework";
		}

		JLabel info = new JLabel("<HTML><B>" + name_ + "</B><BR>" + description
				+ "</HTML>");
		info.setPreferredSize(new Dimension((int) this.getMaximumSize()
				.getWidth() - 175, 100));
		info.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		this.add(info);

		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
		controls.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		controls.setOpaque(false);
		options_ = new LinkButton("Options...", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				options();
			}
		});
		options_.setUnderlined(true);
		options_.setNormalColor(Color.BLUE);
		options_.setAlignmentX(JButton.LEFT_ALIGNMENT);
		options_.setAlignmentY(JButton.CENTER_ALIGNMENT);
		options_.setEnabled(instance.getOptionsDialog() != null);
		remove_ = new LinkButton("Remove", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		remove_.setUnderlined(true);
		remove_.setNormalColor(Color.BLUE);
		remove_.setAlignmentX(JButton.LEFT_ALIGNMENT);
		remove_.setAlignmentY(JButton.CENTER_ALIGNMENT);
		controls.add(options_);
		controls.add(remove_);
		this.add(controls);
	}

	public void setSelected(boolean flag) {
		selected_ = flag;
		repaint();
	}

	public void paintComponent(Graphics g) {
		if (selected_) {
			Graphics2D g2d = (Graphics2D) g;
			Rectangle bounds = getBounds();
			// Set Paint for filling Shape
			Paint gradientPaint = new GradientPaint(0, bounds.height,
					new Color(184, 199, 213), 0, 0, new Color(206, 217, 229));
			g2d.setPaint(gradientPaint);
			g2d.fillRect(0, 0, bounds.width, bounds.height);
		} else {
			super.paintComponent(g);
		}
	}

	public void setRemoveEnabled(boolean flag) {
		remove_.setEnabled(flag);
	}

	public void setOptionsEnabled(boolean flag) {
		options_.setEnabled(flag);
	}

	public void remove() {
		if (owner_.isConfirmRemoval()) {
			int r = JOptionPane.showConfirmDialog(owner_,
					"Are you sure you want to delete " + name_ + "?",
					"Confirm Removal", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (r != JOptionPane.YES_OPTION)
				return;
		}

		owner_.getSystem().uninstall(jar_);
		owner_.refresh();
	}

	public void options() {
		owner_.getSystem().getChild(jar_).getOptionsDialog().display();
	}

	public boolean equals(Object o) {
		if (o instanceof ModuleView) {
			return ((ModuleView) o).name_.equals(this.name_);
		}
		return false;
	}
}
