package framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FileSystem
{
  public static String getInstallDirectory()
  {
    return System.getProperty("user.dir") + "/";
  }
  /**
   * Copy a file to a directory
   * 
   * @param f -
   *          File to be copied
   * @param dst -
   *          Desination directory
   * @return Whether the operation was successful
   */
  public static boolean copyFile(InputStream in, File dst)
  {
    try
    {
      OutputStream out = new FileOutputStream(dst);

      // Transfer bytes from in to out
      byte[] buf = new byte[1024];
      int len;
      while((len = in.read(buf)) > 0)
      {
        out.write(buf, 0, len);
      }
      out.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Returns an abbreviated file name
   * 
   * @param filename -
   *          file to be shortened
   * @return abbreviated version
   */
  public static String getShortenedFilename(String filename)
  {
    String shortened_name;
    if(filename.length() > 25)
    {
      ArrayList<String> pieces = new ArrayList<String>();
      int start = 0;
      for(int end = 0; end < filename.length(); end++)
      {
        char c = filename.charAt(end);
        if(c == '\\')
        {
          pieces.add(filename.substring(start, end));
          start = end;
        }
      }
      pieces.add(filename.substring(start, filename.length()));
      shortened_name = pieces.get(0);
      for(int i = 1; i < pieces.size() - 1; i++)
      {
        if(i == 1)
        {
          shortened_name += "\\...";
        }
        else if(i < pieces.size() - 1 && pieces.get(i + 1).endsWith(".rpr"))
        {
          shortened_name += pieces.get(i);
        }
      }
      shortened_name += (pieces.get(pieces.size() - 1));
    }
    else
    {
      shortened_name = filename;
    }
    if(shortened_name.length() > 25)
    {
      shortened_name = shortened_name.substring(0, 25) + "...";
    }
    return shortened_name;
  }

  /**
   * Updates recent list to include a new file
   * 
   * @param most_recent -
   *          file most recently accessed
   */
  public static void updateRecentList(String most_recent)
  {
    RecentList.instance().addRecent(most_recent);
  }

  /**
   * Returns the recent file list
   * 
   * @return recent file list
   */
  public static LinkedList<String> getRecentList()
  {
    return RecentList.instance().getList();
  }

  private static class RecentList
  {
    private static RecentList this_;

    private static final int SIZE = 4;

    private LinkedList<String> list_;

    private FrameworkProperties props_;

    /**
     * Constructs a new recent file list
     * 
     */
    private RecentList()
    {
      list_ = new LinkedList<String>();
      props_ = FrameworkProperties.instance();
      loadFromPropertiesFile();
    }

    /**
     * Returns the list of recent files
     * 
     * @return list of recent files
     */
    public LinkedList<String> getList()
    {
      return list_;
    }

    /**
     * Adds a file to the 'Recent File' list
     * 
     * @param file -
     *          Path of the file recently accessed
     */
    public void addRecent(String file)
    {
      if(file.startsWith("null"))
        return;
      int index = list_.indexOf(file);
      if(index > -1)
      {
        list_.remove(index);
      }
      if(list_.size() >= SIZE)
      {
        list_.removeLast();
      }
      list_.addFirst(file);
      dumpToPropertiesFile();
    }

    private void loadFromPropertiesFile()
    {
      for(int i = SIZE; i > 0; i--)
      {
        String value = props_.getProperty("RECENT_" + i, "null");
        list_.addFirst(value);
      }
    }

    private void dumpToPropertiesFile()
    {
      Iterator itr = list_.iterator();
      for(int i = 1; itr.hasNext(); i++)
      {
        String d = (String) itr.next();
        props_.setProperty("RECENT_" + i, (d == null) ? "null" : d);
      }
    }

    public static RecentList instance()
    {
      if(this_ == null)
      {
        this_ = new RecentList();
      }
      return this_;
    }
  }

}
