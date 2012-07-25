package help_viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import launcher.Launcher;

public class HelpSystemDialog extends JDialog {
	private static final String TITLE = Launcher.instance().getFramework()
			.getName()
			+ " Help";

	private static final int WIDTH = 700;

	private static final int HEIGHT = 550;

	private static final String INSTRUCTIONS = null;

	private Stack<String> history_;

	private JPanel top_;

	private JPanel side_;

	private JPanel help_;

	private JSplitPane main_;

	private JButton back_;

	private JButton forward_;

	private JButton print_;

	private JLabel search_label_;

	private JTextField search_;

	public HelpSystemDialog() {
		super(Launcher.instance().getFramework().getWindow(), TITLE, true);
		this.setLayout(new BorderLayout());

		top_ = new JPanel(new SpringLayout());
		top_.setPreferredSize(new Dimension(WIDTH, 35));
		top_.setBorder(new EtchedBorder());

		back_ = new JButton();
		back_.setContentAreaFilled(true);
		back_.setOpaque(true);
		back_.setBorderPainted(true);
		back_.setPreferredSize(new Dimension(30, 30));
		back_.setIcon(new ImageIcon(this.getClass().getResource(
				"back_enabled.png")));
		back_.setDisabledIcon(new ImageIcon("back_disabled.png"));

		forward_ = new JButton();
		forward_.setContentAreaFilled(true);
		forward_.setOpaque(true);
		forward_.setPreferredSize(new Dimension(30, 30));
		forward_.setIcon(new ImageIcon(this.getClass().getResource(
				"forward_enabled.png")));
		forward_.setDisabledIcon(new ImageIcon(this.getClass().getResource(
				"forward_disabled.png")));

		print_ = new JButton();
		print_.setContentAreaFilled(true);
		print_.setOpaque(true);
		print_.setPreferredSize(new Dimension(30, 30));
		print_.setIcon(new ImageIcon(this.getClass().getResource("print.png")));

		search_label_ = new JLabel("Search");
		search_ = new JTextField("");
		search_.setPreferredSize(new Dimension(250, 25));

		SpringLayout l = (SpringLayout) top_.getLayout();

		l.putConstraint(SpringLayout.WEST, back_, 3, SpringLayout.WEST, top_);
		l.putConstraint(SpringLayout.WEST, forward_, 3, SpringLayout.EAST,
				back_);

		JSeparator j1 = new JSeparator(JSeparator.VERTICAL);
		j1.setPreferredSize(new Dimension(5, 30));
		l.putConstraint(SpringLayout.WEST, j1, 3, SpringLayout.EAST, forward_);

		l.putConstraint(SpringLayout.WEST, print_, 3, SpringLayout.EAST, j1);

		l
				.putConstraint(SpringLayout.EAST, search_, -3,
						SpringLayout.EAST, top_);
		l.putConstraint(SpringLayout.NORTH, search_, 3, SpringLayout.NORTH,
				top_);

		l.putConstraint(SpringLayout.EAST, search_label_, -5,
				SpringLayout.WEST, search_);
		l.putConstraint(SpringLayout.NORTH, search_label_, 3,
				SpringLayout.NORTH, search_);

		top_.add(back_);
		top_.add(forward_);
		top_.add(j1);
		top_.add(print_);
		top_.add(search_label_);
		top_.add(search_);

		this.add(top_, BorderLayout.NORTH);

		main_ = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		side_ = new JPanel();
		help_ = new JPanel();

		main_.setRightComponent(side_);
		main_.setLeftComponent(help_);
		main_.setDividerLocation((int) (WIDTH * .40));
		this.add(main_, BorderLayout.CENTER);

		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(this);
		this.setVisible(true);
	}
}
