/*
 * iMath project
 * http://www.matematica.br
 *
 * @description
 * 
 */

package ima.action;

import ima.Main;
import ima.view.View;
import ima.menu.Menu;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

/**
 */
public class Action {

	// 
	private View view = null;
	private Menu menu = null;

	// in: ima.Main
	public void setMenu (Menu menu) {
		this.menu = menu;
	}

	// 
	public Action (Main main) {
		// the rectangle that the paint method draws
		this.view = main.getView();
		// this.menu = main.getMenu(); - here it is empty..., let Main define later
	}

	public void treatButtonAction (JButton primaryButton, ActionEvent event) {
		JButton button = (JButton) event.getSource();
		if (primaryButton==menu.getButton(0)) { // menu.getButton(1) = buttonTools
			System.out.println("Action.treatButtonAction: 0");
		}
		else if (primaryButton==menu.getButton(1)) { // menu.getButton(1) = buttonTools
			System.out.println("Action.treatButtonAction: 1");
		}
		else
			System.out.println("Action.treatButtonAction: not 0, nor 1");
	}


	// Action from listener at 'ima.Menu'
	public boolean moveOrNot (String str) {
		if (str==null || str=="")
			return false; // double security
		if (str.equals(Menu.msgLeft)) {
			view.boxTranslate(-View.BOX_WIDTH, 0);
			return true;
		}
		else if (str.equals(Menu.msgRight)) {
			view.boxTranslate(View.BOX_WIDTH, 0);
			return true;
		}
		else if (str.equals(Menu.msgUP)) {
			view.boxTranslate(0, -View.BOX_HEIGHT);
			return true;
		}
		else if (str.equals(Menu.msgDown)) {
			view.boxTranslate(0, View.BOX_HEIGHT);
			return true;
		}
		else if (str.equals(Menu.msgRotate)) {
			view.boxRotate();
			return true;
		}
		else if (str.startsWith("menuSAddBox")) {
			String types[] = str.split("menuSAddBox");
			System.out.println("asiuhauishaui" + types);
			//view.boxRotate();
			return true;
		}
	
		return false;
	} // boolean moveOrNot(String str)

	/**
    Move box in 'ima.view.View'
    @param 
    @param 
    @return 
	 */
	public void move (final int dx, final int dy) {
		// System.out.println("Action.actionPerformed: "+dx+","+dy);
		view.boxTranslate(dx,dy);
	}

	public void rotate() {
		view.boxRotate();
	}
	
}
