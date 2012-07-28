package module_system;

import javax.swing.ImageIcon;

import basic_gui.BasicDialog;
import framework.FrameworkListener;

public interface Module extends FrameworkListener {

	/**
	 * Returns the name of the module
	 * @return
	 */
	public String getName();

	/**
	 * Return a GUI component representing options for this module
	 * 
	 * @return
	 */
	public BasicDialog getOptionsDialog();

	/**
	 * Check to see if updates exist for this module
	 * 
	 * @return true if an update exists
	 */
	public boolean checkForUpdate();

	public void update();

	/**
	 * An icon representing the module
	 * 
	 * @return
	 */
	public ImageIcon getIcon();

	/**
	 * Perform closing operations
	 * 
	 * @return true if the module closed properly. All modules must close
	 *         properly for the framework to close.
	 */
	public boolean close();

	// public void save();
}
