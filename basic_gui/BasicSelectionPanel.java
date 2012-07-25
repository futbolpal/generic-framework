package basic_gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

/**
 * Creates a selection panel with two JLists. The left JList is the available
 * selection and the right JList is the selected items. Two buttons are used to
 * move elements between the two lists.
 */

public class BasicSelectionPanel<E> extends JPanel
{
  private JList options_;

  private DefaultListModel options_model_;

  private JScrollPane options_view_;

  private JList selected_;

  private DefaultListModel selected_model_;

  private JScrollPane selected_view_;

  private JButton add_;

  private JButton remove_;

  private SpringLayout layout_;

  private int side_margin_;

  private int bottom_margin_;

  /**
   * Creates a selection panel with two JLists. The left JList is the available
   * selection and the right JList is the selected items.
   * 
   * @param options -
   *          Options for the option list
   * @param selected -
   *          Any previously selected items to be put in the selection list
   * @param w -
   *          width of the panel
   * @param h -
   *          height of the panel
   * @param replication -
   *          If true, when an item is added as selected to the selected list it
   *          is <I>not</I> removed from the options list. That is, it can be
   *          added multiple times. Otherwise, the item is removed from the
   *          options list.
   */
  public BasicSelectionPanel(String title, Collection<E> options, Collection<E> selected, int w, int h,
      final boolean replication)
  {
    if(options == null)
    {
      throw new NullPointerException();
    }
    this.setPreferredSize(new Dimension(w, h));
    this.setMaximumSize(new Dimension(w, h));
    this.setMinimumSize(new Dimension(w, h));
    this.setLayout((layout_ = new SpringLayout()));
    if(title != null)
    {
      this.setBorder(new TitledBorder(title));
      bottom_margin_ = 30;
      side_margin_ = 5;
    }

    options_model_ = new DefaultListModel();
    for(E o : options)
    {
      if(replication)
      {
        options_model_.addElement(o);
      }
      else
      {
        if(selected == null || !selected.contains(o))
        {
          options_model_.addElement(o);
        }
      }
    }
    options_ = new JList(options_model_);
    options_view_ = new JScrollPane(options_);
    options_view_.setPreferredSize(new Dimension((int) (.4 * w), h - bottom_margin_));
    options_view_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    options_view_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    layout_.putConstraint(SpringLayout.NORTH, options_view_, 0, SpringLayout.NORTH, this);
    layout_.putConstraint(SpringLayout.WEST, options_view_, side_margin_, SpringLayout.WEST, this);
    this.add(options_view_);

    selected_model_ = new DefaultListModel();
    if(selected != null)
    {
      for(E s : selected)
      {
        selected_model_.addElement(s);
      }
    }
    selected_ = new JList(selected_model_);
    selected_view_ = new JScrollPane(selected_);
    selected_view_.setPreferredSize(new Dimension((int) (.4 * w), h - bottom_margin_));
    selected_view_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    selected_view_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    layout_.putConstraint(SpringLayout.NORTH, selected_view_, 0, SpringLayout.NORTH, this);
    layout_.putConstraint(SpringLayout.EAST, selected_view_, -side_margin_, SpringLayout.EAST, this);
    this.add(selected_view_);

    add_ = new JButton(">>");
    add_.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        E selected = (E) options_.getSelectedValue();
        if(selected == null)
          return;
        selected_model_.addElement(selected);
        if(!replication)
        {
          options_model_.removeElement(selected);
        }
      }
    });
    layout_.putConstraint(SpringLayout.SOUTH, add_, ((h - bottom_margin_) / 2) - 5, SpringLayout.NORTH, this);
    layout_.putConstraint(SpringLayout.WEST, add_, (w - (2 * side_margin_)) / 2 - add_.getPreferredSize().width / 2,
                          SpringLayout.WEST, this);
    this.add(add_);

    remove_ = new JButton("<<");
    remove_.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        E selected = (E) selected_.getSelectedValue();
        if(selected == null)
          return;
        selected_model_.removeElement(selected);
        if(!replication)
        {
          options_model_.addElement(selected);
        }
      }
    });
    layout_.putConstraint(SpringLayout.NORTH, remove_, ((h - bottom_margin_) / 2) + 5, SpringLayout.NORTH, this);
    layout_.putConstraint(SpringLayout.WEST, remove_, (w - (2 * side_margin_)) / 2 - remove_.getPreferredSize().width
      / 2, SpringLayout.WEST, this);
    this.add(remove_);
  }

  public ArrayList<E> getSelectedItems()
  {
    ArrayList<E> temp = new ArrayList<E>();
    for(int i = 0; i < selected_.getModel().getSize(); i++)
    {
      temp.add((E) selected_.getModel().getElementAt(i));
    }
    return temp;
  }
}
