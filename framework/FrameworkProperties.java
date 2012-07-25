package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FrameworkProperties extends Properties
{
  private final String PROPERTIES_FILE = System.getProperty("user.dir") + "/properties.dat";

  private static FrameworkProperties this_;

  private static FileOutputStream output_;

  private static FileInputStream input_;

  private FrameworkProperties()
  {
    super();
  }

  public static FrameworkProperties instance()
  {
    if(this_ == null)
    {
      try
      {
        this_ = new FrameworkProperties();
        this_.load();
      }
      catch(IOException e)
      {
        this_ = new FrameworkProperties();
      }
    }
    return this_;
  }

  public String getProperty(String key, String default_value)
  {
    String value = this.getProperty(key);
    if(value == null)
    {
      this.setProperty(key, default_value);

      this.store();

      return default_value;
    }
    return value;
  }

  public Object setProperty(String key, String value)
  {
    Object result = super.setProperty(key, value);
    this.store();
    return result;

  }
  
  public void removeProperty(String key)
  {
	  super.remove(key);
	  this.store();
  }

  public void store()
  {
    try
    {
      output_ = new FileOutputStream(PROPERTIES_FILE);
      super.store(output_, null);
      output_.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  public void load() throws IOException
  {
    input_ = new FileInputStream(PROPERTIES_FILE);
    super.load(input_);
    input_.close();
  }

  public void delete()
  {
    new File(PROPERTIES_FILE).delete();
    this_ = null;
  }
}
