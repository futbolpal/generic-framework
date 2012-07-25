package basic_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import launcher.Launcher;

public abstract class BasicDialog extends JDialog {

	private JLabel instructions_;

	private JComponent main_panel_;

	private JPanel decision_panel_;

	private JPanel left_buttons_;

	private JPanel right_buttons_;

	protected int width_;

	protected int height_;

	private static final int MARGIN = 12;

	/**
	 * Constructs a dialog with a specified title, width and height
	 * 
	 * @param title -
	 *            Text in title bar
	 * @param w -
	 *            Width of dialog
	 * @param h -
	 *            Height of dialog
	 */
	public BasicDialog(String title, int w, int h) {
		this(title, w, h, null, null, null);
	}

	/**
	 * Constructs a dialog with a specified title, width, height, and
	 * instructions
	 * 
	 * @param title -
	 *            Text in title bar
	 * @param w -
	 *            Width of dialog
	 * @param h -
	 *            Height of dialog
	 * @param instructions -
	 *            Instructions for dialog
	 */
	public BasicDialog(String title, int w, int h, String instructions) {
		this(title, w, h, instructions, null, null);
	}

	/**
	 * Constructs a dialog with a specified title, width, height, and
	 * instructions
	 * 
	 * @param title -
	 *            Text in title bar
	 * @param w -
	 *            Width of dialog
	 * @param h -
	 *            Height of dialog
	 * @param instructions -
	 *            Instructions for dialog
	 */
	public BasicDialog(String title, int w, int h, String instructions,
			Image icon) {
		this(title, w, h, instructions, icon, null);
	}

	/**
	 * Constructs a dialog with a specified title, width, height, and an image
	 * in the left panel
	 * 
	 * @param title -
	 *            Text in title bar
	 * @param w -
	 *            Width of dialog
	 * @param h -
	 *            Height of dialog
	 * @param left_image -
	 *            image for the dialog. Image should have a height equal to h -
	 *            100. Image width should be less than half of the width.
	 */
	public BasicDialog(String title, int w, int h, Image left_image) {
		this(title, w, h, null, null, left_image);
	}

	/**
	 * Constructs a dialog with a specified title, width, height, instructions
	 * and an image
	 * 
	 * @param title -
	 *            Text in title bar
	 * @param w -
	 *            Width of dialog
	 * @param h -
	 *            Height of dialog
	 * @param instructions -
	 *            Instructions
	 * @param icon -
	 *            Icon to place in instruction panel
	 */
	private BasicDialog(String title, int w, int h, String instructions,
			Image icon, Image side_image) {
		super(Launcher.instance().getFramework().getWindow());

		width_ = w;
		height_ = h;

		if (instructions != null) {
			this.setInstructions(instructions, icon);
		}

		decision_panel_ = new JPanel(new GridLayout(1, 2));
		right_buttons_ = new JPanel(new SpringLayout());
		right_buttons_.setPreferredSize(new Dimension(width_ / 2, 50));
		left_buttons_ = new JPanel(new SpringLayout());
		left_buttons_.setPreferredSize(new Dimension(width_ / 2, 50));
		decision_panel_.add(left_buttons_);
		decision_panel_.add(right_buttons_);
		decision_panel_.setPreferredSize(new Dimension(width_, 50));
		decision_panel_.setMaximumSize(new Dimension(width_, 50));
		// decision_panel_.setBorder(new EtchedBorder());

		main_panel_ = new JPanel();
		main_panel_.setMaximumSize(new Dimension(width_, height_));

		if (side_image != null) {
			JLabel img_lbl = new JLabel(new ImageIcon(side_image));
			img_lbl.setPreferredSize(new Dimension(side_image.getWidth(null),
					side_image.getHeight(null)));
			this.addComponent(img_lbl, 0, 0);
		}

		this.setTitle(title);
		this.setResizable(false);
		this.setModal(true);
		this.setLayout(new BorderLayout());
		this.setSize(width_, height_);
		this.add(main_panel_, BorderLayout.CENTER);
		this.add(decision_panel_, BorderLayout.SOUTH);
	}

	protected void setInstructions(String instructions, Image icon) {
		if (instructions_ != null) {
			this.remove(instructions_);
		}
		instructions_ = new JLabel();
		int row_count = instructions.split("<BR>").length;
		instructions = "<HTML><B>" + instructions + "</B></HTML>";
		int height = 15 * row_count + 50;
		if (icon != null) {
			icon = icon.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			instructions_.setIcon(new ImageIcon(icon));
			instructions_.setIconTextGap(5);
		}
		instructions_.setBorder(new EmptyBorder(0, 5, 0, 0));
		instructions_.setText(instructions);
		instructions_.setOpaque(true);
		instructions_.setBackground(Color.WHITE);
		instructions_.setPreferredSize(new Dimension(width_, height));
		instructions_.setMaximumSize(new Dimension(width_, height));
		instructions_.setMinimumSize(new Dimension(width_, height));
		instructions_.setHorizontalAlignment(JLabel.LEFT);
		this.add(instructions_, BorderLayout.NORTH);
	}

	protected void removeInstructions() {
		if (instructions_ != null) {
			this.remove(instructions_);
			instructions_ = null;
		}
	}

	/**
	 * Removes all of the buttons from the decision panel.
	 * 
	 */
	protected void removeAllButtons() {
		right_buttons_.removeAll();
		left_buttons_.removeAll();
		this.validate();
	}

	/**
	 * Function to add a control button to the bottom of a dialog. <BR> -
	 * RIGHT_ALIGNMENT places it to the left of the rightmost component<BR> -
	 * LEFT_ALIGNMENT places it to the right of the leftmost component
	 * 
	 * @param button -
	 *            The button to be placed
	 * @param alignment -
	 *            The location of the button
	 * @param default_button -
	 *            Whether the button is default
	 */
	protected void addButton(JComponent button, float alignment,
			boolean default_button) {
		SpringLayout layout;
		if (alignment == JComponent.RIGHT_ALIGNMENT) {
			layout = (SpringLayout) right_buttons_.getLayout();
			int r_count = right_buttons_.getComponentCount();
			if (r_count > 0) {
				JComponent last_added = (JComponent) right_buttons_
						.getComponent(r_count - 1);
				layout.putConstraint(SpringLayout.EAST, button, -5,
						SpringLayout.WEST, last_added);
			} else {
				layout.putConstraint(SpringLayout.EAST, button, -MARGIN * 2,
						SpringLayout.EAST, right_buttons_);
			}
			layout.putConstraint(SpringLayout.SOUTH, button, -MARGIN,
					SpringLayout.SOUTH, right_buttons_);
			right_buttons_.add(button);
		} else {
			layout = (SpringLayout) left_buttons_.getLayout();
			int l_count = left_buttons_.getComponentCount();
			if (l_count > 0) {
				JComponent last_added = (JComponent) left_buttons_
						.getComponent(l_count - 1);
				layout.putConstraint(SpringLayout.WEST, button, 5,
						SpringLayout.EAST, last_added);
			} else {
				layout.putConstraint(SpringLayout.WEST, button, MARGIN * 2,
						SpringLayout.WEST, left_buttons_);
			}
			layout.putConstraint(SpringLayout.SOUTH, button, -MARGIN,
					SpringLayout.SOUTH, left_buttons_);
			left_buttons_.add(button);
		}
		if (default_button && button instanceof JButton) {
			this.getRootPane().setDefaultButton((JButton) button);
		}
		this.validate();
	}

	/**
	 * Add a component at the specified coordinates relative to the bottom left
	 * corner of the instruction box
	 * 
	 * @param c -
	 *            the component to be added
	 * @param x -
	 *            the x position
	 * @param y -
	 *            the y position
	 */
	protected void addComponent(JComponent c, int x, int y) {
		this.addComponent(c, main_panel_, x, y);
	}

	protected void addComponent(JComponent c, JComponent p, int x, int y) {
		if (p.getLayout() == null || !(p.getLayout() instanceof SpringLayout)) {
			p.setLayout(new SpringLayout());
		}
		SpringLayout layout = (SpringLayout) p.getLayout();
		layout.putConstraint(SpringLayout.WEST, c, x, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH, c, y, SpringLayout.NORTH, p);
		p.add(c);
	}

	/**
	 * Add a label and component pair to the form.
	 * 
	 * @param label
	 * @param c
	 * @param x -
	 *            X Coordinate of C
	 * @param y -
	 *            Y Coordinate of C
	 */
	protected void addFormItem(String label, JComponent c, int x, int y) {
		this.addFormItem(label, c, main_panel_, x, y);
	}

	protected void addFormItem(String label, JComponent c, JComponent p, int x,
			int y) {
		if (p.getLayout() == null || !(p.getLayout() instanceof SpringLayout)) {
			p.setLayout(new SpringLayout());
		}
		SpringLayout layout = (SpringLayout) p.getLayout();
		JLabel l = new JLabel(label);
		layout.putConstraint(SpringLayout.WEST, c, x, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH, c, y, SpringLayout.NORTH, p);
		layout.putConstraint(SpringLayout.EAST, l, -5, SpringLayout.WEST, c);
		layout.putConstraint(SpringLayout.NORTH, l, 3, SpringLayout.NORTH, c);
		p.add(l);
		p.add(c);
	}

	public void display() {
		this.setSize(width_, height_);
		this.setLocationRelativeTo(this);
		this.setVisible(true);

	}

	public void setMainPanel(JComponent panel) {
		this.remove(main_panel_);
		this.main_panel_ = panel;
		this.main_panel_.setBorder(new EtchedBorder() {
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				Color a = getShadowColor(c);
				g.translate(x, y);
				width = width - 1;
				height = height - 1;

				g.setColor(a);
				if (instructions_ != null)
					g.drawLine(1, 1, width - 2, 1); // top edge
				g.drawLine(0, height, width, height); // bottom edge

			}
		});
		this.add(main_panel_, BorderLayout.CENTER);
		this.validate();
	}

	/**
	 * Returns a reference to the main panel
	 * 
	 * @return the main panel
	 */
	public JComponent getMainPanel() {
		return main_panel_;
	}
}
