/**
 * 
 */
package custom_ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.border.TitledBorder;

public class SectionBorder extends TitledBorder
{
  public SectionBorder(String title)
  {
    super(title);
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
  {
    Font f = new Font("TITLE", Font.BOLD, 12);
    FontMetrics fm = c.getFontMetrics(f);

    g.translate(x, y);

    g.setColor(Color.BLACK);
    g.setFont(f);
    g.drawString(this.getTitle(), 5, fm.getHeight());
    g.setColor(Color.GRAY);
    g.drawLine(5, fm.getHeight() + 2, c.getWidth() - 10, fm.getHeight() + 2);
    g.translate(-x, -y);
  }
}