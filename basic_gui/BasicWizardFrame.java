package basic_gui;

import java.awt.Image;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

public class BasicWizardFrame extends JPanel {
	private AbstractAction next_action_;

	private AbstractAction back_action_;

	private String instructions_;

	private Image icon_;

	public BasicWizardFrame() {
	}

	public void setBackAction(AbstractAction a) {
		back_action_ = a;
	}

	public void setNextAction(AbstractAction a) {
		next_action_ = a;
	}

	public void setInstructionsIcon(Image i) {
		icon_ = i;
	}

	public void setInstructions(String instructions) {
		instructions_ = instructions;
	}

	public String getInstructions() {
		return instructions_;
	}

	public Image getInstructionsIcon() {
		return icon_;
	}

	public AbstractAction getNextAction() {
		return next_action_;
	}

	public AbstractAction getBackAction() {
		return back_action_;
	};
}
