/*
 * iMath project
 * iMA: http://www.matematica.br/ima 
 * 
 * IMPORTANTE:
 *  1. generalizar o tratamento de atalhos para itens de menu dentro de popups (JMenuItem em JPopupMenu SEM usar JMenuBar)
 *  2. o uso de 'JMenuItem' dentro de 'JPopupMenu' mas SEM usar 'JMenuBar' dificulta tratar globalmente atalhos, vide
 *     truque do 'registerKeyStrokes(...)'
 * 
 */ 

package ima.menu;

import ima.Main;
import ima.resourceBundle.I18n;
import ima.action.Action;
import ima.conf.Configuration;

import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import javax.swing.KeyStroke;

// This applet lets the user move a rectangle by clicking on buttons labeled "Left", "Right", "Up", and "Down".
public class Menu extends JPanel implements ActionListener {

	private Action action = null;

	private Rectangle box;
	private static final int BOX_X = 100;
	private static final int BOX_Y = 100;
	private static final int BOX_WIDTH = 20;
	private static final int BOX_HEIGHT = 30;
	private int boxx, boxy, boxw, boxh;

	private static final ImageIcon
	iconTools = Configuration.iconTools,
	iconToolsEdit = Configuration.iconToolsEdit,
	iconAddBoxA = Configuration.iconAddBoxA,
	iconAddBoxB = Configuration.iconAddBoxB,
	iconAddBoxAB = Configuration.iconAddBoxAB,
	iconToolsExit = Configuration.iconToolsExit,
	
	iconMoves = Configuration.iconAllmoves,
	iconLeft = Configuration.iconLeft,
	iconRight = Configuration.iconRight,
	iconUP = Configuration.iconUp,
	iconDown = Configuration.iconDown,
	iconRotate = Configuration.iconRotate;

	public static final String
	msgTools = "menuPtools",
	msgToolsEdit = "menuStoolsEdit",
	msgToolsExit = "menuStoolsExit",
	msgMoves = "menuPmoves",
	// accessed from 'action.Action.moveOrNot(String)'
	msgLeft = "menuSmovesLeft",
	msgRight = "menuSmovesRight",
	msgUP = "menuSmovesUP",
	msgDown = "menuSmovesDown",
	msgRotate = "menuSrotate",
	msgAddBoxA = "menuSAddBoxA",
	msgAddBoxB  = "menuSAddBoxB",
	msgAddBoxAB  = "menuSAddBoxAB";
	
	
	// To perform movements under shortcuts
	private Main main;
	private JMenuItem itemLeft, itemRight, itemUp, itemDown;

	private JPopupMenu popupMenu; // allows user to select color
	private Vector primaryVec; // vector with each primary button, each one with its secondary list (Vector)
	private Vector secondaryVecPopups; // vector with each secondary list of buttons (as JPopupMenu)
	private ActionListener actionListener = new PopupActionListener(); // Listener to menu item (JMenuItem) of JPopupMenu

	// Tranformar em vetor
	JButton buttonTools, buttonMoves; // botoes primarios
	JPopupMenu secondaryTools, secondaryMoves; // botoes secundarios

	// Perform movement of 'view.View.Box' with ALT + arrow (left, right, up or down)
	public void actionPerformed (ActionEvent ae) {
		// JComponent source = (JComponent)ae.getSource();
		String strComm = ae.getActionCommand();
		// System.out.println("ima.menu.Menu: actionPerformed: "+strComm);//source.getActionCommand()); // source.getLabel());
		if (strComm.equals("left"))
			action.move(-ima.view.View.BOX_WIDTH, 0);
		else if (strComm.equals("right"))
			action.move(ima.view.View.BOX_WIDTH, 0);
		else if (strComm.equals("up"))
			action.move(0, -ima.view.View.BOX_HEIGHT);
		else if (strComm.equals("down"))
			action.move(0, ima.view.View.BOX_HEIGHT);
		// else nothing...
	}

	// Perform movement of 'view.View.Box' with ALT + arrow (left, right, up or down)
	// Register the globals shortcuts
	private void registerKeyStrokes () { // actionPerformed() in Menu is performed with 'getActionCommand()': left, right, up or down
		KeyStroke ksLeft = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.ALT_MASK);
		KeyStroke ksRight = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK);
		KeyStroke ksUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK);
		KeyStroke ksDown=KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
		JPanel jPanel = main.getMainPanel();
		// 
		jPanel.registerKeyboardAction(this, "left", ksLeft, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		jPanel.registerKeyboardAction(this, "right", ksRight, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		jPanel.registerKeyboardAction(this, "up", ksUp, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		jPanel.registerKeyboardAction(this, "down", ksDown, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private boolean isMove (String str) {
		if (str==null || str=="")
			return false;
//		if (action.moveOrNot(str))
//			return true; // identifyed move action
		return true;
	}

	// Define ActionListener
	class PopupActionListener implements ActionListener {
		public void actionPerformed (ActionEvent actionEvent) {
			String strAction = actionEvent.getActionCommand();
			System.out.println("Menu.PopupActionListener: Selected: " + strAction + " modifiers: " + actionEvent.getModifiers());
			if (isMove(strAction))
				action.moveOrNot(strAction);
		}
	}

	public JButton getButton (int i) {
		if (i==0) return buttonTools;
		if (i==1) return buttonMoves;
		System.err.println("ima.menu.Menu: getButton("+i+"): ERROR none JButton!!!");
		return null;
	}
	public JPopupMenu getSecondary (int i, int j) {
		if (i==0) { // row 1: tools
			// getComponentAtIndex(int): deprecated...
			return (JPopupMenu) secondaryTools.getComponentAtIndex(j); // secondaryVecPopups.getElementAt(0);
		}
		else
			if (i==1) { // row 2: move
				return (JPopupMenu) secondaryMoves.getComponentAtIndex(j); // secondaryVecPopups.getElementAt(1);
			}
		System.err.println("ima.menu.Menu: getSecondary("+i+"): ERROR none JPopupMenu!!!");
		return null;
	}


	// Tentativa de generalizar a construcao de menus...
	// Nao estah bom!
	//
	// Construct Vector structures to primary and secondary buttons
	// + primary: has 3 elements per item
	// + secondary: has 2 elements per item
	// Out: Vector (each primary with a secondary Vector)
	private static Vector setVectorButtons ( ) {
		Vector vecSec1 = new Vector(), vecSec2 = new Vector();
		Vector primaryVec = new Vector();
		// secondaryVec = new Vector();
		// Primary 1
		// + tools: Edit; Exit
		// primary button
		vecSec1.addElement(iconToolsEdit); // Edit: icon
		vecSec1.addElement(msgToolsEdit); //        string to message and hint
		vecSec1.addElement(iconToolsExit); // Exit: icon
		vecSec1.addElement(msgToolsExit); //        string to message and hint
		vecSec1.addElement(iconAddBoxA); // 
		vecSec1.addElement(msgAddBoxA); //
		vecSec1.addElement(iconAddBoxB); // 
		vecSec1.addElement(msgAddBoxB); //
		vecSec1.addElement(iconAddBoxAB); // 
		vecSec1.addElement(msgAddBoxAB); //
		
		// secondary button
		primaryVec.addElement(iconTools); // 
		primaryVec.addElement(msgTools); //
		primaryVec.addElement(vecSec1); //

		// Primary 2
		// + moves: Left; Right; Up; Down
		// secondary buttons
		vecSec2.addElement(iconLeft); //  Left: icon
		vecSec2.addElement(msgLeft); //        string to message and hint
		vecSec2.addElement(iconRight); // Right: icon
		vecSec2.addElement(msgRight); //       string to message and hint
		vecSec2.addElement(iconUP); //    Up: icon
		vecSec2.addElement(msgUP); //          string to message and hint
		vecSec2.addElement(iconDown); //  Down: icon
		vecSec2.addElement(msgDown); //        string to message and hint
		vecSec2.addElement(iconRotate);
		vecSec2.addElement(msgRotate);
		// primary button
		primaryVec.addElement(iconMoves); // 
		primaryVec.addElement(msgMoves); //
		primaryVec.addElement(vecSec2); //
		return primaryVec;
	} //  static Vector setVectorButtons()

	//CAUTION:
	//  como os 'JMenuItem' que interessam NAO estao sob 'JMenuBar' ele NAO podem ser diretamente acionados via atalhos
	private static JPopupMenu makeSecondary (int ind, final Menu menu, final JButton primaryButton, final Action action, final ActionListener actionListener, String msgPrimary, Vector secondaryVec) {
		primaryButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				Object objEvent = event.getSource();
				if (action==null) System.err.println("ima.menu.Menu: makeSecondary: ERROR action null!!!");
				// popupMenu.show( e.getComponent(), e.getX(), e.getY() );
				if (objEvent == menu.getButton(0)) { // buttonTools
					try {
						menu.secondaryTools.show((java.awt.Component)objEvent, 0, menu.getButton(0).getHeight() );
					}catch(Exception e) { System.err.println("ima.menu.Menu: makeSecondary: ERROR "+e+"\n x "+menu+"\n x "+menu.secondaryTools); }
					action.treatButtonAction(menu.getButton(0), event); // menu.getButton(0) = buttonTools
				}
				else if (objEvent == menu.getButton(1)) { // buttonMoves
					menu.secondaryMoves.show((java.awt.Component)objEvent, 0, menu.getButton(1).getHeight() );
					action.treatButtonAction(menu.getButton(1), event); // menu.getButton(1) = buttonMoves
				}
				else if (objEvent == menu.getSecondary(0,0)) { // buttonTools: secondary list
					// secondaryVecPopups
					System.out.println("ima.menu.Menu: clicado fila 0");
				}
				else if (objEvent == menu.getSecondary(1,0)) { // buttonMoves: secondary list
					System.out.println("ima.menu.Menu: clicado fila 1");
				}
				else System.err.println("ima.menu.Menu: makeSecondary: ERROR no button at all.. \n" + objEvent + "\n" + menu.getButton(0));
				// action.treatButtonAction(primaryButton, event);
			}
		}); //  void actionPerformed(ActionEvent event)

		JPopupMenu popupMenu = new JPopupMenu(); // create pop-up menu (a secondary column menu, under a primary button)
		JMenuItem item;
		ImageIcon icon;
		String msg;
		int size = secondaryVec.size() / 2;
		//DT System.out.println("ima.menu.Menu: makeSecondary: "+msgPrimary);
		for (int i=0; i<size; i++) {
			icon = (ImageIcon) secondaryVec.elementAt(2*i); // icon
			msg = (String) secondaryVec.elementAt(2*i+1); // text
			popupMenu.add(item = new JMenuItem(msg, icon));

			//DT System.out.println(" "+i+": "+msg);
			item.setHorizontalTextPosition(JMenuItem.RIGHT);
			item.setToolTipText(msg+"Hint");

			if (ind==1) { //AG para tentar colocar atalho global...
				// 'primaryButton' will be 'menu.buttonTools'
				// set shortcut to this item - sequence of keys: Left, Right, Up, Down
				// + if need a single key shortcut: item.setMnemonic(KeyEvent.VK_LEFT);
				// + Setting the accelerator: use KeyStroke

				// Define 'globals' in order 'Menu.actionPerformed(...)' gets access to each 'JMenuItem' to use under shortcuts
				// CAUTION: como os 'JMenuItem' que interessam NAO estao sob 'JMenuBar' ele NAO podem ser diretamente acionados via atalhos
				switch (i) {
				case 0: menu.itemLeft = item; // item.setMnemonic(KeyEvent.VK_LEFT);  - como internacionalizar atalho via letra???
				item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.ALT_MASK)); break;
				case 1: menu.itemRight = item; item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK)); break;
				case 2: menu.itemUp = item; item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK)); break;
				case 3: menu.itemDown = item; item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK)); break;
				}	
				// Add listener to menu item (JMenuItem) of JPopupMenu
				item.addActionListener(actionListener);
			} //AG para tentar colocar atalho global...

		}
		if (popupMenu==null) System.out.println("ima.menu.Menu: makeSecondary: ERROR popup null!!!");
		primaryButton.setToolTipText(msgPrimary+"Hint"); // I18n.getString(label+"Hint")
		// primaryButton.add(popupMenu); -- nao pode adicionar explicitamente: ao mostrar usa-se o 'primaryButton' correto!!!
		return popupMenu;
	} // static JPopupMenu makeSecondary(final Menu menu, final JButton primaryButton, final Action action, final ActionListener actionListener, String msgPrimary, Vector secondaryVec)

	//TRUQUE: para conseguir acesso direto (via atalho) as setas, precisa de um JMenuBar
	// javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
	// javax.swing.JMenu jMenu = new javax.swing.JMenu(""); //

	//
	public Menu (Main main, Action action) {
		this.main = main;
		this.action = action;

		//CAUTION:
		//  como os 'JMenuItem' que interessam NAO estao sob 'JMenuBar' ele NAO podem ser diretamente acionados via atalhos

		primaryVec = setVectorButtons(); // Vector structure of primary and secondary buttons
		secondaryVecPopups = new Vector(); // Vector with all secondary list of buttons

		// Listener to menu item (JMenuItem) of JPopupMenu
		this.actionListener = new PopupActionListener();

		JButton button;
		JPopupMenu secondaryMenuPopup;
		ImageIcon icon;
		String msgPrimary;
		Vector secVec;
		int size = primaryVec.size() / 3;
		for (int i=0; i<size; i++) {
			//DT System.out.println("ima.menu.Menu: "+i);
			icon = (ImageIcon) primaryVec.elementAt(3*i); // icon
			msgPrimary = (String) primaryVec.elementAt(3*i+1); // text
			secVec = (Vector) primaryVec.elementAt(3*i+2); // secondary buttons' list
			button = new JButton(icon); // new JButton(msgPrimary,icon); - must put a single Icon, with none name
			button.setToolTipText(msgPrimary+"Hint");
			secondaryMenuPopup = makeSecondary(i, this, button, action, actionListener, msgPrimary, secVec);
			secondaryVecPopups.addElement(secondaryMenuPopup); // add secondary list of buttons (as JPopupMenu)
			this.add(button);
			if (i==0) {
				this.secondaryTools = secondaryMenuPopup;
				this.buttonTools = button;
			}
			else
				if (i==1) {
					this.secondaryMoves = secondaryMenuPopup;
					this.buttonMoves = button;
				}
				else
					System.err.println("ima.menu.Menu: ERROR no defined JButton "+i+"!!!");
		}

		registerKeyStrokes();

	}


	/**
    Makes a button that moves the box.
    Kind of movement defined by: int dx, int dy : (+,0)=>right; (-,0)=>left; (0,+)=>down; (0,-)=>up
    @param label : the label to show on the button
    @param dx : the amount by which to move the box in x-direction when the button is clicked
    @param dy : the amount by which to move the box in y-direction when the button is clicked
    @return the button
	 */
	public JButton makeButton (String label, ImageIcon icon, final int dx, final int dy) {
		JButton button = new JButton(I18n.getString(label), icon);
		button.setToolTipText(I18n.getString(label+"Hint"));
		// button.setActionCommand("cmd...");
		class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				// System.out.println("ima.menu.Menu.actionPerformed: "+dx+","+dy);
				// box.translate(dx, dy);
				// repaint();
				// action.Action.move(dx,dy);
				action.move(dx,dy);
			}
		};

		ButtonListener listener = new ButtonListener();
		button.addActionListener(listener);
		return button;
	}

}
