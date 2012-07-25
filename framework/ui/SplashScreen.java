package framework.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import launcher.Launcher;

public class SplashScreen extends JWindow implements MouseListener {
	private static SplashScreen this_;

	private JPanel loading_;

	private String status_;

	private JLabel main_;

	private ImageIcon nullicon_ = new ImageIcon(this.getClass().getResource(
			"resources/default_icon.png"));

	private SplashScreenConfig config_;

	protected SplashScreen() {
		this.addMouseListener(this);
		loading_ = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loading_.setPreferredSize(new Dimension(-1, 35));
		loading_.setBackground(Color.BLACK);
	}

	public void setConfig(SplashScreenConfig c) {
		config_ = c;

		// unpack the imageicon
		main_ = new JLabel(c.getImageIcon());
		this.validate();

	}

	public void addLoadIcon(String name, ImageIcon icon) {
		if (icon == null)
			icon = nullicon_;
		icon = new ImageIcon(icon.getImage().getScaledInstance(25, 25,
				Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		label.setOpaque(false);
		loading_.add(label);
		loading_.validate();
		status_ = name;
		this.repaint();
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g);

		// paint title
		{
			String name = Launcher.instance().getFramework().getName();
			String version = Launcher.instance().getFramework().getVersion();
			g2d.setFont(config_.getFont().deriveFont(35f));
			g2d.setColor(Color.WHITE);
			FontMetrics fm = g2d.getFontMetrics();
			Rectangle2D name_bounds = fm.getStringBounds(name, g2d);
			g2d.drawString(name, 20, 50);
			if (config_.isVersionShown()) {
				Rectangle2D version_bounds = fm.getStringBounds(version, g2d);
				g2d.drawLine(20, 55, this.getWidth() - 20, 55);
				g2d.setFont(config_.getFont().deriveFont(20f));
				g2d.drawString(version, this.getWidth() - 20
						- (int) version_bounds.getWidth(), 75);
			}
		}

		if (loading_.getComponentCount() > 0) {
			Component c = loading_
					.getComponent(loading_.getComponentCount() - 1);
			float alpha = .3f;
			if (status_ != null) {
				g2d.setFont(config_.getFont().deriveFont(12f));
				FontMetrics fm = g2d.getFontMetrics();
				Rectangle2D r = fm.getStringBounds(status_, g2d);
				double x = c.getX();
				if (r.getWidth() + x > this.getWidth()) {
					x = this.getWidth() - r.getWidth();
				}
				g2d.setColor(Color.WHITE);
				g2d.drawString(status_, (int) x, (int) (loading_.getY() - 2));
			}
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, 16f,
					Color.BLACK, true));
			g2d.fillRoundRect(c.getX() + loading_.getX() - 2, c.getY()
					+ loading_.getY() - 2, c.getWidth() + 4, c.getHeight() + 4,
					5, 5);
			g2d.setColor(Color.BLACK);
			g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, 90F,
					Color.BLACK, true));
			g2d.drawRoundRect(c.getX() + loading_.getX() - 2, c.getY()
					+ loading_.getY() - 2, c.getWidth() + 4, c.getHeight() + 4,
					5, 5);
		}
	}

	public void setVisible(boolean flag) {
		if (flag) {
			this.add(main_, BorderLayout.CENTER);
			this.add(loading_, BorderLayout.SOUTH);
			this.pack();
			this.setLocationRelativeTo(this);
			super.setVisible(true);
		} else {
			super.setVisible(false);
			super.dispose();
		}
	}

	public static SplashScreen instance() {
		if (this_ == null) {
			this_ = new SplashScreen();
		}
		return this_;
	}

	public void mouseClicked(MouseEvent arg0) {
		this.setVisible(false);
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
