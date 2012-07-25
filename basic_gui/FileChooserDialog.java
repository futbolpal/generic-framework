package basic_gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import launcher.Launcher;
import framework.FrameworkProperties;


public class FileChooserDialog
{
  public enum Type
  {
    OPEN,
    SAVE;
  }

  private static JFileChooser chooser_;

  private boolean selected_;

  public FileChooserDialog()
  {
    super();

    String path = FrameworkProperties.instance().getProperty("framework.last-directory",System.getProperty("user.home"));

    chooser_ = new JFileChooser();
    chooser_.setCurrentDirectory(new File(path));

  }

  /**
   * Set the file filter
   *
   * @param ff
   */
  public void setFileFilter(FileFilter ff)
  {
    chooser_.setFileFilter(ff);
  }

  /**
   * Show the dialog
   *
   * @param type -
   *          Type of dialog
   * @param directories_only -
   *          Show only directories
   */
  public void showDialog(Type type, boolean directories_only)
  {
    if(directories_only)
    {
      chooser_.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    switch(type)
    {
    case SAVE:
      selected_ = (chooser_.showSaveDialog(Launcher.instance().getFramework().getWindow()) == JFileChooser.APPROVE_OPTION);
      break;
    case OPEN:
      selected_ = (chooser_.showOpenDialog(Launcher.instance().getFramework().getWindow()) == JFileChooser.APPROVE_OPTION);
      break;
    }
    File f = chooser_.getSelectedFile();
    if(f != null)
    {
      FrameworkProperties.instance().setProperty("framework.last-directory",f.getAbsolutePath());
    }

  }

  /**
   * Returns the file selected from the dialog
   *
   * @return File selected from dialog
   */
  public File getSelectedFile()
  {
    return chooser_.getSelectedFile();
  }

  /**
   * Determines whether a file was selected or the user cancelled
   *
   * @return whether a file was selected
   */
  public boolean isFileSelected()
  {
    return selected_;
  }

  /**
   * Sets the default selected file for the dialog
   *
   * @param f -
   *          the file to select
   */
  public void setFilename(String f)
  {
    chooser_.setSelectedFile(new File(f));
  }
}
