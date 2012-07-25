package basic_gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import framework.FrameworkProperties;

public abstract class BasicPreferencesDialog extends BasicDialog
{
  private HashMap<String, JComponent> preferences_;

  private JButton cancel_;

  private JButton ok_;

  /**
   * Constructs a dialog with a specified title, width and height
   * 
   * @param title -
   *          Text in title bar
   * @param w -
   *          Width of dialog
   * @param h -
   *          Height of dialog
   */
  public BasicPreferencesDialog(String title, int w, int h)
  {
    this(title, w, h, null);
  }

  /**
   * Constructs a dialog with a specified title, width, height, and instructions
   * 
   * @param title -
   *          Text in title bar
   * @param w -
   *          Width of dialog
   * @param h -
   *          Height of dialog
   * @param instructions -
   *          Instructions for dialog
   */
  public BasicPreferencesDialog(String title, int w, int h, String instructions)
  {
    this(title, w, h, instructions, null);
  }

  /**
   * Constructs a Preferences dialog with the specified title, width, height, and imageicon
   * @param t - Title for the dialog
   * @param w - width of the dialog
   * @param h - height of the dialog
   * @param i - instructions for the dialog
   * @param icon - icon for the dialog
   */
  public BasicPreferencesDialog(String t, int w, int h, String i, Image icon)
  {
    super(t, w, h, i, icon);
    preferences_ = new HashMap<String, JComponent>();

    cancel_ = new JButton("Cancel");
    cancel_.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    ok_ = new JButton("OK");
    ok_.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        applyChanges();
        dispose();
      }
    });

    this.addButton(cancel_, RIGHT_ALIGNMENT, false);
    this.addButton(ok_, RIGHT_ALIGNMENT, true);
  }

  /**
   * Iterates registered components and applies changes to the respective
   * property
   * 
   * Supports the following components: - JComboBox - Stores index of item, for
   * enum types. - JTextField - Stores strings. - JCheckBox - Stores booleans.
   * 
   */
  public void applyChanges()
  {
    for(Map.Entry<String, JComponent> p : preferences_.entrySet())
    {
      String key = p.getKey();
      JComponent c = p.getValue();
      if(c instanceof JCheckBox)
      {
        FrameworkProperties.instance().setProperty(key, String.valueOf(((JCheckBox) c).isSelected()));
      }
      else if(c instanceof JTextField)
      {
        FrameworkProperties.instance().setProperty(key, String.valueOf(((JTextField) c).getText()));
      }
      else if(c instanceof JComboBox)
      {
        FrameworkProperties.instance().setProperty(key, String.valueOf(((JComboBox) c).getSelectedIndex()));
      }
    }
  }

  /**
   * This function must be used in order for a framework property to be changed
   * 
   * @param f -
   *          key to be edited
   * @param c -
   *          Editing component
   */
  public final void register(String s, JComponent c)
  {
    preferences_.put(s, c);
  }
}
