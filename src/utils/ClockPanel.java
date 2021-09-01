package utils;

import java.awt.Graphics;

import javax.swing.JPanel;

import aesthetic.Dial;

public class ClockPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Dial[] dials;
	
	public ClockPanel(Dial[] dials) {
		this.dials = dials;
		
		for(Dial d: dials) {
			this.add(d);
		}
		
	}
	
	public void paint(Graphics g) {
		
		super.paintComponent(g);
		
		for(Dial d: dials) {
			d.paintComponent(g);
		}
	}
	
}


