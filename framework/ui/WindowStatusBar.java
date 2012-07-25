package framework.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class WindowStatusBar extends JToolBar {

	public WindowStatusBar() {
		this.setFloatable(false);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		this.setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
		this.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
		this.setSize(new Dimension(Integer.MAX_VALUE, 30));

	}

	public Component add(Component c) {
		super.add(Box.createHorizontalStrut(5));
		if (this.getComponentCount() > 1) {
			super.add(new JSeparator(JSeparator.VERTICAL));
			super.add(Box.createHorizontalStrut(5));
		}
		return super.add(c);
	}
}
