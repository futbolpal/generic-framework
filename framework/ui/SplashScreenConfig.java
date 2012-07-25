package framework.ui;

import java.awt.Font;

import javax.swing.ImageIcon;

public class SplashScreenConfig {
	private Font font_;
	private ImageIcon image_;
	private boolean show_version_;
	
	public SplashScreenConfig(Font f, ImageIcon i, boolean show_version) {
		font_ = f;
		image_ = i;
		show_version_ = show_version;
	}

	public Font getFont() {
		return font_;
	}

	public ImageIcon getImageIcon() {
		return image_;
	}
	
	public boolean isVersionShown()
	{
		return show_version_;
	}
}
