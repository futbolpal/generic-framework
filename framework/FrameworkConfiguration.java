package framework;

import java.awt.Image;

import basic_gui.BasicDialog;
import basic_gui.BasicPreferencesDialog;
import framework.ui.Window;

public abstract class FrameworkConfiguration
{
  public abstract String getName();
  
  public abstract String getVersion();
  
  public abstract Window getWindow();
  
  public abstract BasicDialog getAboutDialog();

  public abstract boolean hasAboutDialog();

  public abstract BasicPreferencesDialog getPreferencesDialog();

  public abstract Image getIcon();

  public abstract Image getSplash();
}
