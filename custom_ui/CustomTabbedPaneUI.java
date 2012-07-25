package custom_ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;
import basic_gui.BasicTabbedPane;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

public class CustomTabbedPaneUI extends WindowsTabbedPaneUI
{
  public CustomTabbedPaneUI()
  {
    super();
  }

  public static ComponentUI createUI(JComponent c)
  {
    return new CustomTabbedPaneUI();
  }

  public Rectangle getTabBounds(JTabbedPane p, int i)
  {
    Rectangle r = super.getTabBounds(p, i);
    r.height = this.calculateTabHeight(p.getTabPlacement(), i, this.getFontMetrics().getHeight());
    r.width = this.calculateTabWidth(p.getTabPlacement(), i, this.getFontMetrics());
    return r;
  }

  protected int calculateTabWidth(int p, int i, FontMetrics fm)
  {
    if(getTabbedPane().isTabWidthFixed())
    {
      return getTabbedPane().getFixedTabWidth();
    }
    else
    {
      Insets tabInsets = getTabInsets(p, i);
      int width = tabInsets.left + tabInsets.right + 3;

      View v = getTextViewForTab(i);
      if(v != null)
      {
        // html
        width += (int) v.getPreferredSpan(View.X_AXIS);
      }
      else
      {
        // plain text
        String title = tabPane.getTitleAt(i);
        width += SwingUtilities2.stringWidth(tabPane, fm, title);
      }
      return width + 20; //20 = GAP
    }
  }

  protected void paintFocusIndicator(Graphics g, int p, Rectangle[] rects, int i, Rectangle iconRect,
      Rectangle textRect, boolean s)
  {
  }

  protected BasicTabbedPane getTabbedPane()
  {
    return (BasicTabbedPane) tabPane;
  }
}
