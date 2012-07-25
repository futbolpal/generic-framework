package framework;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.GregorianCalendar;

public class FrameworkDate
{
  private int m_;

  private int d_;

  private int y_;

  /**
   * Construct a date using "now".
   */
  public FrameworkDate()
  {
    GregorianCalendar gc = new GregorianCalendar();
    m_ = gc.get(MONTH) + 1;
    d_ = gc.get(DAY_OF_MONTH);
    y_ = gc.get(YEAR);
  }

  /**
   * Construct a date object using the given parameters
   * 
   * @param m -
   *          Month
   * @param d -
   *          Day
   * @param y -
   *          Year
   * @throws IllegalArgumentException
   */
  public FrameworkDate(int m, int d, int y) throws IllegalArgumentException
  {
    checkDate(m, d, y);

    m_ = m;
    d_ = d;
    y_ = y;
  }

  /**
   * Construct a date object from a given string. <BR>
   * String should have data in order as month, day, year dividided by the '/'
   * character. <BR>
   * <I>Example:</I> 5/3/1987
   * 
   * @param s
   * @throws IllegalArgumentException
   */
  public FrameworkDate(String s) throws IllegalArgumentException
  {
    if(s == null)
      throw new IllegalArgumentException();
    String[] mdy = s.split("/");
    if(mdy.length != 3)
      throw new IllegalArgumentException();

    checkDate(Integer.parseInt(mdy[0]), Integer.parseInt(mdy[1]), Integer.parseInt(mdy[2]));

    m_ = Integer.parseInt(mdy[0]);
    d_ = Integer.parseInt(mdy[1]);
    y_ = Integer.parseInt(mdy[2]);
  }

  /**
   * Checks the validity of a date.
   * 
   * @param m -
   *          Month
   * @param d -
   *          Day
   * @param y -
   *          Year
   */
  public void checkDate(int m, int d, int y)
  {
    if(m > 12 || y < new GregorianCalendar().get(YEAR) || d < 0 || d > 31)
    {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Creates a string to represent the data object
   */
  public String toString()
  {
    return m_ + "/" + d_ + "/" + y_;
  }
}
