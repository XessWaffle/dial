package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aesthetic.Dial;
import utils.ClockPanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {

	private JFrame frame;
	private ClockPanel panel;
	private Dial[] random = {new Dial(150, 180, 0, 0, 720, Color.BLUE), new Dial(210,240,0,0,4800, Color.RED), new Dial(240, 270, 0, 0, 96, Color.GREEN), new Dial(140, 150, 0, 0, 100, Color.ORANGE)};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panel.setBounds(0,0,frame.getWidth(), frame.getHeight());
				random[0].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
				random[1].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
				random[2].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
				random[3].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
			}
		});
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setBounds(dim.width/2-700/2, dim.height/2-700/2, 700, 700);
		frame.setUndecorated(true);
		frame.setOpacity(0.7f);
		frame.setBackground(new Color(0,0,0,0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new ClockPanel(random);
		panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		panel.setOpaque(false);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		random[0].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
		random[1].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
		random[2].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
		random[3].setCenter(new Point(panel.getWidth()/2, panel.getHeight()/2));
		
		random[0].setColorFade(Dial.FADE_WHITE);
		random[1].setColorFade(Dial.FADE_WHITE);
		random[2].setColorFade(Dial.FADE_WHITE);
		random[3].setColorFade(Dial.FADE_WHITE);
		
		random[3].setFillMode(Dial.BOUNCE_BACK_FILL_MODE);
		random[3].setRotationMode(Dial.FLIP_ROTATION);
		random[0].setFillMode(Dial.LINEAR_FILL_MODE);
		random[0].setRotationMode(Dial.CLOCK_WISE);
		random[3].setFlipCondition(50);
		
		//random.setBounds(239, 73, 169, 179);
		
		Thread re = new Thread() {
			
			public void run() {
				
				
				double seconds = 0, minutes = 0, hours = 0, ms = 0;
				
				while(true) {
					
					LocalTime time = LocalTime.now();
					
					seconds = ((time.getSecond() * 1e9 + time.getNano())/(60.0 * 1e9)) * 100;
					minutes = (time.getMinute()/60.0) * 100;
					hours = (time.getHour()/24.0) * 100;
					ms = (time.getNano()/1e9) * 100;
					
					random[3].setPosition(ms);
					random[2].setPosition(hours);
					random[1].setPosition(minutes);
					random[0].setPosition(seconds);
					
					//System.out.println("Minutes: " + minutes);
					
					panel.repaint();
					
					try {
						Thread.sleep(10);
						
						/*if(positive) {
							ms += 1;
						} else {
							ms--;
						}
						
						if(ms == 100 || ms == 0) {
							positive = !positive;
						}*/
					
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};
		
		re.start();
		
		
	}
}
