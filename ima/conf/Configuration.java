/*
 * iMath project
 * http://www.matematica.br
 * 
 * iMA
 * 
 * @description Configurations to colors, dimensions of applete or JFrame, and to visual canvas (JComponent)
 * @see Main.java; view.View.java
 * 
 */

package ima.conf;

import java.awt.Color;
import javax.swing.ImageIcon;

public final class Configuration {

	// view.View
	public static final int
	width = 490,
	height = 344, // 210 + 134
	heightC = 210;

	public static final Color 
	canvasFore = Color.black, //
	canvasBack = Color.white; //

	// menu/Menu.java
	public static final ImageIcon
	iconTools = new ImageIcon("ima/img/icon/tools_32.png"),
	iconToolsExit = new ImageIcon("ima/img/icon/exit.png"),
	iconToolsEdit = new ImageIcon("ima/img/icon/edit.png"),
	iconAllmoves = new ImageIcon("ima/img/icon/Allmoves.png"),
	iconLeft = new ImageIcon("ima/img/icon/Left.png"),
	iconRight = new ImageIcon("ima/img/icon/Right.png"),
	iconUp = new ImageIcon("ima/img/icon/Up.png"),
	iconDown = new ImageIcon("ima/img/icon/Down.png"),
	iconRotate = new ImageIcon("ima/img/icon/Down.png"),
	iconAddBoxA = new ImageIcon("ima/img/icon/edit.png"),
	iconAddBoxB = new ImageIcon("ima/img/icon/edit.png"),
	iconAddBoxAB = new ImageIcon("ima/img/icon/edit.png");
}
