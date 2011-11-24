
/*
 * iMath project
 * http://www.matematica.br
 * 
 * iMA
 * 
 * @description Main programa (start applet or application)
 * @see menu/Menu: Left - Right - Up - Down
 * @see view/View: draw rectangle
 * @see action/Action: move rectangle from 'menu.Menu'
 * 
 */
package ima;

import ima.resourceBundle.I18n;

import ima.conf.Configuration;
import ima.menu.Menu;
import ima.action.Action;
import ima.view.View;

import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
//import javax.swing.

import java.util.Vector;

public class Main extends JApplet {

	public static final String
	iMA_version = "0.0",
	LOGO1 = "img/logo-igeom.gif",
	MSG1 = "iMath: http://www.matematica.br";

	public static String
	lang = "en",
	country="BR";

	private static Main program = null;

	private boolean isApplet = false;
	private Menu menuPanel = null;
	private Action action = null;
	private View view = null;

	// private ;
	private JFrame jframe;
	private Component mainComponent;

	private int height;

	// Used in: action.Action
	// public Action getAction () { return action; }
	public Menu getMenu () { return menuPanel; }
	public View getView () { return view; }

	public JPanel getMainPanel () { return panelG; }
	JPanel panelG = null; // general: panelN; panelC

	// 
	public void constructAll () {
		this.panelG = new JPanel(new BorderLayout()); // general: panelN; panelC
		this.view = new View();
		this.action = new Action(this);
		this.menuPanel = new Menu(this, this.action);
		this.action.setMenu(this.menuPanel); // now you can define 'Menu menu' of 'ima.action.Action'

		// +----------------------------------------------------------------+ panelG
		// | +------------------------------------------------------------+ | | + panelN 
		// | | +--------------------------------------------------------+ | | |  | + panelL
		// | | | logo                          http://www.matematica.br | | | |  |
		// | | +--------------------------------------------------------+ | | |  |
		// | | +--------------------------------------------------------+ | | |  | + menuPanel (menu panel)
		// | | | menu                                                   | | | |
		// | | +--------------------------------------------------------+ | | |
		// | +------------------------------------------------------------+ | |
		// | +------------------------------------------------------------+ | | + panelC (panel with canvas) 
		// | |                                                            | | 
		// | |                                                            | | 
		// | +------------------------------------------------------------+ |
		// +----------------------------------------------------------------+
		// JPanel panelG = new JPanel(new BorderLayout()); // general: panelN; panelC

		JPanel panelN = new JPanel(new BorderLayout()); //          panelN: panelL; menuPanel
		JPanel panelL = new JPanel(new BorderLayout()); //                  panelL: Image logo; Label URL
		javax.swing.JComponent panelC = this.view;      //          panelC: JComponent

		JLabel iconLabel = new JLabel(new ImageIcon(LOGO1));
		JLabel textLabel = new JLabel(MSG1);

		// panelC.add(this.view);

		panelL.add(iconLabel, BorderLayout.WEST);
		panelL.add(textLabel, BorderLayout.EAST);
		panelN.add(panelL, BorderLayout.NORTH);
		panelN.add(this.menuPanel, BorderLayout.SOUTH);
		panelG.add(panelN, BorderLayout.NORTH); // panelN: panelL; menuPanel
		panelG.add(panelC, BorderLayout.CENTER);//SOUTH); // panelC: Canvas
		panelG.add(new JPanel(), BorderLayout.SOUTH); // panelC: Canvas

		// 
		if (this.isApplet) {
			// it is an applet: get the applet context
			this.getContentPane().add(panelG);
			String strHeight = this.getParameter("height");
			if (strHeight!=null && strHeight!="") try {
				this.height = java.lang.Integer.parseInt(strHeight); // strHeight.toNumber();
			} catch (Exception e) { this.height = panelG.getHeight() + Configuration.heightC; }
		}
		else {
			// it is an application: create a frame
			JFrame frame = new JFrame();
			frame.setTitle(" .: iMA :. ");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// try to put the application in the center of the screen
			java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			int w = 900; // width
			int h = 650; // height
			if (dim.width < w) w = dim.width - 10;
			if (dim.height < h) h = (int) (dim.height * 0.9);
			frame.setLocation((dim.width - w) / 2 , (dim.height - h) / 2);

			frame.setSize(Configuration.width, Configuration.height);
			// frame.setBounds(0,0,Configuration.width, 500);
			frame.getContentPane().add(panelG);
			// frame.pack(); - resulta erro grave de dimensoes!!!
			frame.setVisible(true); // frame.show();
			this.height = panelG.getHeight() + Configuration.heightC;
		}

		// System.out.println("Main.constructAll: height="+this.height);
	} //  void constructAll()


	// Applet initializations
	public void init () {
		System.out.println(".: iMA: version "+iMA_version+" :.");
		this.isApplet = true;

		// Try HTML parameters: 'name="lang" value="?" and 'name="country" value="?"'                                                                           
		// lang = this.getParameter("lang");                                                                                                                            
		// country = this.getParameter("country");                                                                                                                      
		//  I18n.setLanguage(lang);

		this.constructAll();
	}


	public static void main (String[] args) {
		System.out.println(".: iMA: version "+iMA_version+" :.");

		// Try to define language and country by command line
		I18n.setConfig(args); // define lingua => tem prioridade sobre arquivo de lingua 'igeom.lang'
		I18n.defineBundle(true); // define 'Messages*.properties'

		program = new Main();
		program.isApplet = false;
		program.constructAll();

		// System.out.println("Main.main(...): "+program.getWidth()+","+program.getHeight());

	}

}
