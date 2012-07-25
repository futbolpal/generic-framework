package module_system;

import javax.swing.ImageIcon;

import basic_gui.BasicDialog;
import framework.FrameworkListener;

public interface Module extends FrameworkListener
{
  public String getName();

  public BasicDialog getOptionsDialog();

  public boolean checkForUpdate();

  public void update();

  public ImageIcon getIcon();
  
  public boolean close();
  
  //public void save();
}
