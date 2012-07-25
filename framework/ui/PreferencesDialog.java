package framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import basic_gui.BasicPreferencesDialog;
import framework.FrameworkProperties;

/**
 * Framework Specific Class > set Framework Preferences - Change agent
 * information - Set startup dialog show true/false
 */
public class PreferencesDialog extends BasicPreferencesDialog
{
  private static final String TITLE = "Preferences Dialog";

  private static final int WIDTH = 500;

  private static final int HEIGHT = 500;

  private static final String INSTRUCTIONS = "Configure Preferences";

  private JTabbedPane tabs_;

  /**
   * Constructs a PreferenceBasicDialog for the framework
   * 
   */
  public PreferencesDialog()
  {
    super(TITLE, WIDTH, HEIGHT, INSTRUCTIONS);

    tabs_ = new JTabbedPane();
    tabs_.setPreferredSize(new Dimension(this.getMainPanel().getPreferredSize()));

    JCheckBox show_startup_dialog = new JCheckBox("Show Startup Dialog on startup");
    show_startup_dialog.setSelected(Boolean.valueOf(FrameworkProperties.instance()
        .getProperty("framework.startup-dialog.show-at-startup")));
    this.register("framework.startup-dialog.show-at-startup", show_startup_dialog);

    JPanel general = new JPanel();
    this.addComponent(show_startup_dialog, general, 10, 10);
    tabs_.addTab("General", general);

    this.getMainPanel().add(tabs_, BorderLayout.CENTER);
  }

  protected void addTab(String title, Component c)
  {
    tabs_.addTab(title, c);
  }

  protected Component getTab(int i)
  {
    return tabs_.getComponentAt(i);
  }

  protected Component getTab(String title)
  {
    return tabs_.getComponent(tabs_.indexOfTab(title));
  }
}
