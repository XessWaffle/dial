package aesthetic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import utils.Animation;

public class Dial extends JComponent implements Animation{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7685049665499733139L;

	public static final int LINEAR_FILL_MODE = 0;
	public static final int TRIG_FILL_MODE = 1;
	public static final int BOUNCE_FILL_MODE = 2;
	public static final int BOUNCE_BACK_FILL_MODE = 3;
	
	public static final int COUNTER_CLOCK_WISE = 0;
	public static final int CLOCK_WISE = 1;
	public static final int FLIP_ROTATION = 3;
	
	public static final int FADE_WHITE = 0;
	public static final int FADE_BLACK = 1;
	
	public static final int CIRCULAR_RESOLUTION = 18000;
	
	private int in_rad, out_rad;
	private double fill_percentage;
	private int[] x, y;
	private Point center;
	private Color color;
	private int num_sec;
	private GeneralPath[] sections;
	private int fill_mode = LINEAR_FILL_MODE, rotation_mode = CLOCK_WISE, color_fade = FADE_BLACK;
	private int flip_condition = 0;
	
	
	public Dial(int in_rad, int out_rad, int c_x, int c_y, int num_sec, Color color) {
		this.setInnerRadius(in_rad);
		this.setOuterRadius(out_rad);
		
		x = new int[CIRCULAR_RESOLUTION * 2];
		y = new int[CIRCULAR_RESOLUTION * 2];
		
		center = new Point(c_x, c_y);
		
		this.setBounds((int)(center.getX() - out_rad), (int)(center.getY() - out_rad), out_rad * 2, out_rad * 2);
		this.setSize(this.getWidth(), this.getHeight());
		
		this.color = color;
		this.num_sec = num_sec;
		
		generatePoints();
		generateSections(CIRCULAR_RESOLUTION, num_sec);
		
	}

	private void generatePoints() {
		// TODO Auto-generated method stub
		
		double radians_per_point = Math.PI * 2.0/CIRCULAR_RESOLUTION;
		double radian = -Math.PI/2.0;
			
		for(int i = 0; i < CIRCULAR_RESOLUTION; i++) {
			x[i] = (int)(in_rad * Math.cos(radian));
			y[i] = (int)(in_rad * Math.sin(radian));
			
			x[CIRCULAR_RESOLUTION * 2 - 1 - i] = (int)(out_rad * Math.cos(radian));
			y[CIRCULAR_RESOLUTION * 2 - 1 - i] = (int)(out_rad * Math.sin(radian));
			
			System.out.println("(" + x[i] + "," + y[i] + ")");
			
			
			radian += radians_per_point;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		int area_fill = (int) ((fill_percentage/100.0) * sections.length);
			
		if(fill_mode == TRIG_FILL_MODE) {
			area_fill = (int) ((1.0/(1 + Math.exp(-(fill_percentage-50)/10.0))) * sections.length);
		} else if(fill_mode == BOUNCE_FILL_MODE) {
			area_fill = (int) (((10 * Math.sin(fill_percentage * Math.PI/20.0) + fill_percentage)/100.0) * sections.length);	
		} else if(fill_mode == BOUNCE_BACK_FILL_MODE) {
			area_fill = (int) (Math.exp(-((fill_percentage - 50) * (fill_percentage - 50))/400) * sections.length);
		}
		
		//System.out.println("AreaFill: " + area_fill + "\nFillPercentage: " + fill_percentage);
		
		if(area_fill > sections.length) {
			area_fill = sections.length;
		} else if(area_fill < 0) {
			area_fill = 0;
		}
			
		manageRotation(g2d, area_fill);
		
		
	}
	
	private void manageRotation(Graphics2D g2d, int area_fill) {
		float[] rgb = color.getRGBColorComponents(null);
		
		double red_step = (rgb[0])/(area_fill);
		double green_step = (rgb[1])/(area_fill);
		double blue_step = (rgb[2])/(area_fill);
		
		if(color_fade == FADE_WHITE) {
			red_step = (1.0 - rgb[0])/(area_fill);
			green_step = (1.0 - rgb[1])/(area_fill);
			blue_step = (1.0 - rgb[2])/(area_fill);
		}
		
		if(rotation_mode == CLOCK_WISE) {
			for(int i = 0; i < area_fill; i++) {		
				g2d.setColor(new Color(rgb[0], rgb[1], rgb[2]));
				
				if(color_fade == FADE_WHITE) {
					rgb[0] += red_step;
					rgb[1] += green_step;
					rgb[2] += blue_step;
				} else {
					rgb[0] -= red_step;
					rgb[1] -= green_step;
					rgb[2] -= blue_step;				
				}
				
				if(i < sections.length)
					g2d.fill(sections[i]);
				
				/*if(i == area_fill - 1)
					g2d.drawLine((int)sections[i].getCurrentPoint().getX(), (int)sections[i].getCurrentPoint().getY(), (int)center.getX(), (int)center.getY());*/
			}
		} else if(rotation_mode == COUNTER_CLOCK_WISE) {
			for(int i = sections.length - 1; i > sections.length - area_fill; i--) {		
				g2d.setColor(new Color(rgb[0], rgb[1], rgb[2]));
			
				if(color_fade == FADE_WHITE) {
					rgb[0] += red_step;
					rgb[1] += green_step;
					rgb[2] += blue_step;
				} else {
					rgb[0] -= red_step;
					rgb[1] -= green_step;
					rgb[2] -= blue_step;				
				}
				
				if(i < sections.length)
					g2d.fill(sections[i]);
				
				/*if(i == area_fill - 1)
					g2d.drawLine((int)sections[i].getCurrentPoint().getX(), (int)sections[i].getCurrentPoint().getY(), (int)center.getX(), (int)center.getY());*/
			}
		} else if(rotation_mode == FLIP_ROTATION) {
			
			if(fill_percentage < flip_condition) {
				
				for(int i = 0; i < area_fill; i++) {		
					g2d.setColor(new Color(rgb[0], rgb[1], rgb[2]));
				
					if(color_fade == FADE_WHITE) {
						rgb[0] += red_step;
						rgb[1] += green_step;
						rgb[2] += blue_step;
					} else {
						rgb[0] -= red_step;
						rgb[1] -= green_step;
						rgb[2] -= blue_step;				
					}
					
					if(i < sections.length)
						g2d.fill(sections[i]);
					
					/*if(i == area_fill - 1)
						g2d.drawLine((int)sections[i].getCurrentPoint().getX(), (int)sections[i].getCurrentPoint().getY(), (int)center.getX(), (int)center.getY());*/
				}
			} else {
						
				for(int i = sections.length - area_fill + 1; i < sections.length; i++) {		
					g2d.setColor(new Color(rgb[0], rgb[1], rgb[2]));
					
					if(color_fade == FADE_WHITE) {
						rgb[0] += red_step;
						rgb[1] += green_step;
						rgb[2] += blue_step;
					} else {
						rgb[0] -= red_step;
						rgb[1] -= green_step;
						rgb[2] -= blue_step;				
					}
					
					if(i < sections.length)
						g2d.fill(sections[i]);
					
					/*if(i == area_fill - 1)
						g2d.drawLine((int)sections[i].getCurrentPoint().getX(), (int)sections[i].getCurrentPoint().getY(), (int)center.getX(), (int)center.getY());*/
				}
			}
		}
	}

	private void generateSections(int area_fill, int sec) {
		// TODO Auto-generated method stub
		GeneralPath[] subsections = new GeneralPath[sec];
		
		int d_area_fill = (int)(area_fill/(double) sec);
		
		for(int i = 0; i < sec; i++) {
			subsections[i] = new GeneralPath(GeneralPath.WIND_NON_ZERO);
			
			subsections[i].moveTo(x[d_area_fill * i] + center.getX(), y[d_area_fill * i] + center.getY());
			
			for(int j = d_area_fill * i + 1; j < d_area_fill * i + d_area_fill + 1; j++) {
				subsections[i].lineTo(x[j] + center.getX(), y[j] + center.getY());
			}
			
			subsections[i].lineTo(x[CIRCULAR_RESOLUTION * 2 - 1 - d_area_fill * (i + 1)] + center.getX(), y[CIRCULAR_RESOLUTION * 2 - 1 - d_area_fill * (i + 1)] + center.getY());
			
			for(int j = 1; j < d_area_fill; j++) {	
				
				int index = CIRCULAR_RESOLUTION * 2 - d_area_fill * (i + 1) + j;
				
				index = index < CIRCULAR_RESOLUTION * 2 ? index : index - CIRCULAR_RESOLUTION * 2;
				
				subsections[i].lineTo(x[index] + center.getX(), y[index] + center.getY());
			}
			
			subsections[i].closePath();
		}
		
		this.sections = subsections;
		
	}

	/**
	 * @return the in_rad
	 */
	public int getInnerRadius() {
		return in_rad;
	}

	/**
	 * @param in_rad the in_rad to set
	 */
	public void setInnerRadius(int in_rad) {
		this.in_rad = in_rad;
	}

	/**
	 * @return the out_rad
	 */
	public int getOuterRadius() {
		return out_rad;
	}

	/**
	 * @param out_rad the out_rad to set
	 */
	public void setOuterRadius(int out_rad) {
		this.out_rad = out_rad;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	/**
	 * @return the fill_percentage
	 */
	public double getFillPercentage() {
		return fill_percentage;
	}

	/**
	 * @param fill_percentage the fill_percentage to set
	 */
	@Override
	public void setPosition(double fill_percentage) {
		this.fill_percentage = fill_percentage;
	}

	public void setCenter(Point point) {
		// TODO Auto-generated method stub
		this.center = point;
		this.setBounds((int)(center.getX() - out_rad), (int)(center.getY() - out_rad), out_rad * 2, out_rad * 2);
		this.setSize(this.getWidth(), this.getHeight());
		
		this.generateSections(CIRCULAR_RESOLUTION, num_sec);
		
	}

	/**
	 * @return the fill_mode
	 */
	public int getFillMode() {
		return fill_mode;
	}

	/**
	 * @param fill_mode the fill_mode to set
	 */
	public void setFillMode(int fill_mode) {
		this.fill_mode = fill_mode;
	}

	/**
	 * @return the rotation_mode
	 */
	public int getRotationMode() {
		return rotation_mode;
	}

	/**
	 * @param rotation_mode the rotation_mode to set
	 */
	public void setRotationMode(int rotation_mode) {
		this.rotation_mode = rotation_mode;
	}

	/**
	 * @return the flip_condition
	 */
	public int getFlipCondition() {
		return flip_condition;
	}

	/**
	 * @param flip_condition the flip_condition to set
	 */
	public void setFlipCondition(int flip_condition) {
		this.flip_condition = flip_condition;
	}
	
	public void setColorFade(int fade) {
		this.color_fade = fade;
	} 
	
	public int getColorFade() {
		return this.color_fade;
	} 
}
