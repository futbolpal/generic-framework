package basic_gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JLabel;

public class LinkButton extends JLabel
{
  private boolean underlined_;

  private String text_;

  private Action action_;

  private Color normal_;

  private Color over_;

  public LinkButton(String text)
  {
    this(text, null);
  }

  public LinkButton(String text, Action a)
  {
    super(text);
    text_ = text;
    action_ = a;
    normal_ = Color.BLACK;
    over_ = Color.BLUE;
    this.setEnabled(true);
    this.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {

        if(LinkButton.this.isEnabled())
        {
          action_.actionPerformed(null);
        }
      }

      public void mouseEntered(MouseEvent e)
      {
        if(LinkButton.this.isEnabled())
        {
          LinkButton.this.setForeground(over_);
          LinkButton.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
      }

      public void mouseExited(MouseEvent e)
      {
        if(LinkButton.this.isEnabled())
        {
          LinkButton.this.setForeground(normal_);
          LinkButton.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
      }
    });
  }

  public void setAction(Action a)
  {
    action_ = a;
  }

  public void setEnabled(boolean flag)
  {
    super.setEnabled(flag);
    if(this.isEnabled())
    {
      this.setForeground(normal_);
      this.setUnderlined(true);
    }
    else
    {
      this.setForeground(Color.GRAY);
      this.setUnderlined(false);
    }
  }

  public void setUnderlined(boolean flag)
  {
    if(underlined_ == flag)
      return;
    underlined_ = flag;
    if(underlined_)
    {
      this.setText("<HTML><U>" + text_ + "</U></HTML>");
    }
    else
    {
      this.setText(text_);
    }
  }

  public void setNormalColor(Color c)
  {
    normal_ = c;
    this.setForeground(normal_);
  }

  public void setOverColor(Color c)
  {
    over_ = c;
  }
}
