package basic_gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;

public abstract class BasicWizard extends BasicDialog {
	private LinkedList<BasicWizardFrame> frames_;

	private BasicWizardFrame current_frame_;

	private int frame_index_;

	private JButton back_;

	private JButton next_;

	private JButton cancel_;

	private boolean valid_;

	private AbstractAction cancel_action_;

	public BasicWizard(String name) {
		super(name, 500, 500);
		frames_ = new LinkedList<BasicWizardFrame>();
		frame_index_ = 0;
		back_ = new JButton("<< Back");
		back_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BasicWizardFrame current = frames_.get(frame_index_);
				if (current.getBackAction() != null) {
					current.getBackAction().actionPerformed(null);
				}
				if (valid())
					back();
				valid_ = true;
			}
		});
		next_ = new JButton("Next >>");
		next_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BasicWizardFrame current = frames_.get(frame_index_);
				if (current.getNextAction() != null) {
					current.getNextAction().actionPerformed(null);
				}
				if (valid()) {
					if (hasMoreFrames()) {
						next();
					} else {
						dispose();
					}

				}
				valid_ = true;
			}
		});
		cancel_ = new JButton("Cancel");
		cancel_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cancel_action_ != null) {
					cancel_action_.actionPerformed(null);
				}
				dispose();
			}
		});
		this.addButton(cancel_, JComponent.RIGHT_ALIGNMENT, false);
		this.addButton(next_, JComponent.RIGHT_ALIGNMENT, true);
		this.addButton(back_, JComponent.RIGHT_ALIGNMENT, false);
	}

	public void setCancelAction(AbstractAction a) {
		cancel_action_ = a;
	}

	public void setValid(boolean flag) {
		valid_ = flag;
	}

	public boolean valid() {
		return valid_;
	}

	public void next() {
		frame_index_++;
		showFrameAtIndex();
	}

	public void back() {
		frame_index_--;
		showFrameAtIndex();
	}

	public void skipToFrame(int i) {
		frame_index_ = i;
		showFrameAtIndex();
	}

	public boolean hasMoreFrames() {
		return frame_index_ != frames_.size() - 1;
	}

	protected void showFrameAtIndex() {
		BasicWizardFrame frame = frames_.get(frame_index_);
		this.setMainPanel(frame);
		if (frame.getInstructions() != null) {
			this.setInstructions(frame.getInstructions(), frame
					.getInstructionsIcon());
		} else {
			this.removeInstructions();
		}
		if (!this.hasMoreFrames()) {
			next_.setText("Finish");
		} else {
			next_.setText("Next >>");
		}
		back_.setEnabled(this.frame_index_ != 0);
		valid_ = true;
		this.repaint();
		this.validate();
	}

	public void addFrame(BasicWizardFrame d) {
		frames_.add(d);
	}

	public int getFrameIndex() {
		return frame_index_;
	}

	public int getFrameCount() {
		return frames_.size();
	}

	public void display() {
		frame_index_ = 0;
		showFrameAtIndex();
		super.display();
	}
}
