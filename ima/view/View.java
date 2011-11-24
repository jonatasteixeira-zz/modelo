/*
 * iMath project
 * http://www.matematica.br
 * 
 * iMA
 * 
 * @description Visualization canvas (JComponent)
 * @see Main.java
 * 
 */

package ima.view;

import ima.conf.Configuration;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

/**
 */

public class View extends javax.swing.JComponent implements MouseListener {

	private Vector<Rectangle> boxes = new Vector<Rectangle>();

	private static final int BOX_X = 100;
	private static final int BOX_Y = 100;
	public static final int BOX_WIDTH = 20; // accessed from 'action.Action.moveOrNot(String)
	public static final int BOX_HEIGHT = 30; // idem
	private int	width = Configuration.width-10;
	private int height = Configuration.heightC;
	private int last_clicked = 0;
	
	
	// If interested in using double buffering
	java.awt.Image _drawSpace; //DB
	java.awt.Graphics2D _drawSpaceG; //DB

	public View () {
		this.setSize(width, height);
		this.addBox("A");
		this.setForeground(Configuration.canvasFore);
		this.setBackground(Configuration.canvasBack);
		this.addMouseListener(this);	
	}

	public void addBox(String type) {
		this.boxes.add(new Rectangle(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT));
	}

	public void boxTranslate (int dx, int dy) {
		Rectangle box = this.boxes.get(this.last_clicked);
		java.awt.Point position = box.getLocation();
		int dim_dx = this.getWidth(), dim_dy = this.getHeight();
		if (position.x+dx+BOX_WIDTH<=0)
			dx += dim_dx + BOX_WIDTH; // rotate
		else
			if (position.x+dx>dim_dx)
				dx -= dim_dx + BOX_WIDTH; // rotate

		if (position.y+dy+BOX_HEIGHT<=0) {
			dy += dim_dy + BOX_HEIGHT; // rotate
			// System.out.println("--- top");
		}
		else
			if (position.y+dy>dim_dy) {
				dy -= dim_dy + BOX_HEIGHT; // rotate
				// System.out.println("--- down");
			}

		box.translate(dx, dy);
		repaint();
		// System.out.println("view.View.boxTranlate("+dx+","+dy+"): dim=("+dim_dx+","+dim_dy+") in "+position.x+","+position.y);
	}

	public void boxRotate() {
		Rectangle box = this.boxes.get(this.last_clicked);
		int x = box.x;
		int y = box.y;
		int height = box.height;
		int width = box.width;
		
		box.setBounds(y, x, height, width); 
		repaint();
	}
	
	// Paint canvas with drawings
	public void paint (Graphics g) {
		int w = this.getWidth(), h = this.getHeight();

		if (h<height)
			this.setSize(width, height); // porque est� precisando disso????

		Graphics2D g2 = (Graphics2D)g;

		//DB These lines if interested in using double buffering
		w = this.getSize().width; h = this.getSize().height;
		_drawSpace = createImage(w, h);
		_drawSpaceG = (Graphics2D)_drawSpace.getGraphics();
		_drawSpaceG.setColor(java.awt.Color.black);

		_drawSpaceG.setColor(Configuration.canvasBack); // back ground
		_drawSpaceG.fillRect(0,0,w,h);
		_drawSpaceG.setColor(Configuration.canvasFore); // fore ground
		_drawSpaceG.drawRect(0,0,w-1,h-1);
		
		for (int i = 0; i < this.boxes.size(); i++) {
			if (i == this.last_clicked) {
				_drawSpaceG.setColor(java.awt.Color.red);
				_drawSpaceG.draw(this.boxes.get(i));
				_drawSpaceG.setColor(java.awt.Color.black);
			} else {
				_drawSpaceG.draw(this.boxes.get(i));
			}
		}
		
		g2.drawImage(_drawSpace, 0, 0, java.awt.Color.black, this);

	}

	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < this.boxes.size(); i++) {
			if (this.boxes.get(i).contains(e.getPoint().x, e.getPoint().y)) {
				this.last_clicked = i;
				System.out.println("Box selected: " + i);
				return;
			}
		}
	}

	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
}
