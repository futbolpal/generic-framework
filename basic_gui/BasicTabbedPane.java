package basic_gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;

import custom_ui.CustomTabbedPaneUI;

public class BasicTabbedPane extends JTabbedPane
{
  private enum ChangeType
  {
    ADDED,
    REMOVED;
  }

  private int tab_width_;

  private JPopupMenu tab_menu_;
  
  private JPopupMenu tab_area_menu_;
  
  private boolean confirm_tab_close_;
  
  private String confirm_tab_close_message_;
  
  private ArrayList<BasicTabbedPaneListener> listeners_;
  
  private ArrayList<HiddenTab> hidden_tabs_;

  public BasicTabbedPane()
  {
    hidden_tabs_ = new ArrayList<HiddenTab>();
    confirm_tab_close_ = true;
    confirm_tab_close_message_ = "Are you sure you want to close the tab?";
    tab_width_ = -1;
    listeners_ = new ArrayList<BasicTabbedPaneListener>();
    this.setTabPlacement(TOP);
    this.setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    this.setUI(new CustomTabbedPaneUI());
    this.addMouseListener(new MouseAdapter()
    {
      public void mouseReleased(MouseEvent e)
      {
        BasicTabbedPane this_ = BasicTabbedPane.this;
        if(this_.getTabCount() == 0)
        {
          tabAreaClicked(e);      
          return;
        }
        if(this_.getSelectedIndex() < 0)
        {
          return;
        }
        TabbedPaneUI ui = (TabbedPaneUI) this_.getUI();
        Rectangle tab_bounds = ui.getTabBounds(this_, this_.getSelectedIndex());
        Point mouse = e.getPoint();
        if(tab_bounds.contains(mouse))
        {
          tabClicked(e);
        }
        else if(this_.getTabPlacement() == TOP && mouse.y < tab_bounds.getHeight())
        {
          tabAreaClicked(e);
        }
        else if(this_.getTabPlacement() == BOTTOM && mouse.y > tab_bounds.getY())
        {
          tabAreaClicked(e);
        }
        else if(this_.getTabPlacement() == LEFT && mouse.x < tab_bounds.getWidth())
        {
          tabAreaClicked(e);
        }
        else if(this_.getTabPlacement() == RIGHT && mouse.x >  tab_bounds.getX())
        {
          tabAreaClicked(e);
        }
      }
    });
  }

  protected void tabClicked(MouseEvent e)
  {
    if(e.isPopupTrigger())
    {
      showTabPopupMenu(e.getPoint());
    }
  }
  
  protected void tabAreaClicked(MouseEvent e)
  {
    if(e.isPopupTrigger())
    {
      showTabAreaPopupMenu(e.getPoint());
    }
  }

  protected void showTabPopupMenu(Point p)
  {
    if(tab_menu_ == null)
      return;

    tab_menu_.show(this, p.x, p.y);
  }
  
  protected void showTabAreaPopupMenu(Point p)
  {
    if(tab_area_menu_ == null)
      return;
    tab_area_menu_.show(this, p.x, p.y);    
  }

  public void setTabPopupMenu(JPopupMenu m)
  {
    tab_menu_ = m;
  }
  
  public void setTabAreaPopupMenu(JPopupMenu m)
  {
    tab_area_menu_ = m;
  }

  public void setFixedTabWidth(int w)
  {
    tab_width_ = w;
  }

  public int getFixedTabWidth()
  {
    return tab_width_;
  }
  
  public int getHiddenTabCount()
  {
    return hidden_tabs_.size();
  }

  public boolean isTabWidthFixed()
  {
    return !(tab_width_ < 0);
  }

  public void addTab(String title, Icon icon, Component component, String tip, int index)
  {
    if(index < 0)
      index = this.getTabCount();
    
    super.insertTab(title, icon, component, tip, index);
    final JPanel tab = new JPanel();
    SpringLayout layout = new SpringLayout();
    Rectangle r = this.getUI().getTabBounds(this, index);
    tab.setPreferredSize(new Dimension(r.getSize()));
    tab.setLayout(layout);
    tab.setOpaque(false);

    int placement = this.getTabPlacement();

    JButton close = new JButton();
    close.setIcon(new ImageIcon(this.getClass().getResource("resources/tab_close_icon.png")));
    close.setRolloverIcon(new ImageIcon(this.getClass().getResource("resources/tab_close_icon_over.png")));
    close.setPressedIcon(new ImageIcon(this.getClass().getResource("resources/tab_close_icon_pressed.png")));
    close.setContentAreaFilled(false);
    close.setFocusPainted(false);
    close.setPreferredSize(new Dimension(12, 12));
    close.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if(confirm_tab_close_)
        {
          int r = JOptionPane.showConfirmDialog(BasicTabbedPane.this,confirm_tab_close_message_,"Tab Closing",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
          if(r == JOptionPane.YES_OPTION)
          {
            BasicTabbedPane this_ = BasicTabbedPane.this;
            int index = this_.indexOfTabComponent(tab);
            this_.removeTabAt(index);
            this_.fireChangeEvent(ChangeType.REMOVED, index);      
          }
        }
      }
    });
    layout.putConstraint(SpringLayout.NORTH, close, 2, SpringLayout.NORTH, tab);
    if(placement == LEFT)
    {
      layout.putConstraint(SpringLayout.WEST, close, 5, SpringLayout.WEST, tab);
    }
    else
    {
      layout.putConstraint(SpringLayout.EAST, close, -5, SpringLayout.EAST, tab);
    }
    tab.add(close);

    JLabel label = new JLabel(title);
    layout.putConstraint(SpringLayout.NORTH, label, 2, SpringLayout.NORTH, tab);
    if(placement == LEFT)
    {
      layout.putConstraint(SpringLayout.EAST, label, -5, SpringLayout.EAST, tab);
    }
    else
    {
      layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, tab);
    }
    tab.add(label);

    this.setTabComponentAt(index, tab);
    this.fireChangeEvent(ChangeType.ADDED,index);    
  }
  
  public void addTab(String title, Icon icon, Component component, String tip)
  {
    this.addTab(title,icon,component,tip,-1);
  }
  
  public void confirmTabClosing(boolean flag)
  {
    this.confirm_tab_close_ = flag;
  }
  
  public void setConfirmTabClosingMessage(String s)
  {
    this.confirm_tab_close_message_ = s;
  }

  public void addTab(String title, Icon icon, Component component)
  {
    this.addTab(title, icon, component, null);
  }

  public void addTab(String title, Component component)
  {
    this.addTab(title, null, component);
  }
  
  public void addListener(BasicTabbedPaneListener l)
  {
    listeners_.add(l);
  }
  
  public void setTabVisible(int i, boolean flag)
  {
    if(flag)
    {
      HiddenTab t = hidden_tabs_.remove(hidden_tabs_.indexOf(new HiddenTab(i,null,null,null,null)));
      this.addTab(t.title_,t.icon_,t.panel_,t.tip_,t.index_);      
    }
    else
    {
      hidden_tabs_.add(new HiddenTab(i, this.getTitleAt(i),this.getComponentAt(i),this.getToolTipTextAt(i), this.getIconAt(i)));
      this.removeTabAt(i);
    }
  }
  
  public void setTabVisible(String title, boolean flag)
  {
    int i = hidden_tabs_.indexOf(new HiddenTab(-1,title,null,null,null));
    
    this.setTabVisible(i,flag);
  }
  
  public ArrayList<String> getHiddenTabNames()
  {
    ArrayList<String> names = new ArrayList<String>();
    for(HiddenTab t:hidden_tabs_)
    {
      names.add(t.title_);
    }
    return names;
  }
  
  public void removeListener(BasicTabbedPaneListener l)
  {
    listeners_.remove(l);
  }
  
  public void fireChangeEvent(ChangeType t, int i)
  {
    switch(t)
    {
      case REMOVED:
        for(BasicTabbedPaneListener l:listeners_)
        {
          l.tabRemoved(i);
        }
        break;
      case ADDED:
        for(BasicTabbedPaneListener l:listeners_)
        {
          l.tabAdded(i);
        }
        break;
    }
  }
  
  private class HiddenTab
  {
    private int index_;
    private String title_;
    private Component panel_;   
    private String tip_;
    private Icon icon_;
    
    public HiddenTab(int i , String t, Component p, String tip, Icon icon)
    {
      index_ = i;
      title_ = t;
      panel_ = p;
      tip_ = tip;
      icon_ = icon;
    }
    
    public boolean equals(Object o)
    {
      if(o instanceof HiddenTab)
      {
        return (((HiddenTab)o).index_ == index_)
        || (((HiddenTab)o).title_.equals(title_));
      }
      return false;      
    }
    
    public String toString()
    {
      return title_;
    }
  }

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e)
    {

    }
    JFrame f = new JFrame();

    JPopupMenu menu = new JPopupMenu();
    menu.setLightWeightPopupEnabled(true);
    menu.add(new JMenuItem("TEST"));

    BasicTabbedPane p1 = new BasicTabbedPane();
    p1.addTab("Test", new JPanel());
    p1.addTab("Test2", new JPanel());
    p1.setTabPopupMenu(menu);
    p1.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        System.out.println(((BasicTabbedPane)e.getSource()).getSelectedIndex());
      }
    });

    final BasicTabbedPane p2 = new BasicTabbedPane();
    p2.setTabPlacement(BasicTabbedPane.LEFT);
    p2.addTab("Test", new JPanel());
    p2.addTab("Test2", new JPanel());

    BasicTabbedPane p3 = new BasicTabbedPane();
    p3.setTabPlacement(BasicTabbedPane.RIGHT);
    p3.setFixedTabWidth(100);
    p3.addTab("Test", new JPanel());
    p3.addTab("Test2", new JPanel());

    BasicTabbedPane p4 = new BasicTabbedPane();
    p4.setFixedTabWidth(100);
    p4.setTabPlacement(BasicTabbedPane.BOTTOM);
    p4.addTab("Test", new JPanel());
    p4.addTab("Test2", new JPanel());
    
    final JToggleButton b = new JToggleButton("HIDER");
    b.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        p2.setTabVisible(0,!b.isSelected());
      }
    });

    f.setLayout(new GridLayout(3, 2));
    f.add(p1);
    f.add(p2);
    f.add(p3);
    f.add(p4);
    f.add(b);
    f.setSize(500, 500);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }
}
