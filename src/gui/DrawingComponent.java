package gui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import model.Project;
import model.Field;

import java.util.*;
import java.util.List;
import java.io.*;


@SuppressWarnings("serial")
public class DrawingComponent extends JComponent {

	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;
	private double imageHeight;
	private double imageWidth;
	private Project project;
	private List<Field> fields;

	private ArrayList<DrawingShape> shapes;
	private Font font;
	private BasicStroke stroke;
	
	public DrawingComponent(Image image, Project project, List<Field> fields) {
		this.project = project;
		this.fields = fields;
		w_translateX = 0;
		w_translateY = 0;
		scale = .6;
		
		//setLocation(1500, 1500);
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		
		font = new Font("SansSerif", Font.PLAIN, 72);
		stroke = new BasicStroke(5);
		
		this.setBackground(Color.gray);
		this.setPreferredSize(new Dimension(1000, 600));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(2000, 2000));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
	
		

		imageHeight = image.getHeight(null);
		imageWidth = image.getWidth(null);
		shapes.add(new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null) , image.getHeight(null))));
		shapes.add(new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), new Color(119, 108, 216, 100)));
		createTextShapes();
	}
	
	public void updateImage(Image image) {
		shapes.get(0).setImage(image);
	}
	
	public void updateImageProjectFields(Image image, Project project, List<Field> fields) {
		
		shapes.add(new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null) , image.getHeight(null))));
		shapes.add(new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), new Color(119, 108, 216, 100)));
		this.project = project;
		this.fields = fields;
		repaint();
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
	private void createTextShapes() {
		//String text1 = "Width: " + this.getWidth();
		//shapes.add(new DrawingText(text1, Color.BLACK, new Point2D.Float(200, 200)));
		
		//String text2 = "Height: " + this.getHeight();
		//shapes.add(new DrawingText(text2, Color.BLACK, new Point2D.Float(200, 400)));
	}
	
//	private void updateTextShapes() {
//		for (DrawingShape shape : shapes) {
//			if (shape instanceof DrawingText) {
//				DrawingText textShape = (DrawingText)shape;
//				if (textShape.getText().startsWith("Width:")) {
//					textShape.setText("Width: " + this.getWidth());
//				}
//				else if (textShape.getText().startsWith("Height:")) {
//					textShape.setText("Height: " + this.getHeight());
//				}
//			}
//		}
//	}
	
	private Image loadImage(String imageFile) {
		try {
			Image image = ImageIO.read(new File(imageFile));
			imageHeight = image.getHeight(null);
			imageWidth = image.getWidth(null);
			return image;
		}
		catch (IOException e) {
			return NULL_IMAGE;
		}
	}
	
	public void setScale(double newScale) {
		scale = newScale;
		this.repaint();
	}
	public double getScale() {
		return scale;
	}
	
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.translate(this.getWidth() / 2.0, this.getHeight() / 2.0);
		g2.scale(scale, scale);
		g2.translate(-imageWidth  / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
		
		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			shape.draw(g2);
		}
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth() / 2.0, getHeight() / 2.0);
			transform.scale(scale, scale);
			transform.translate( -imageWidth / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
			
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					hitShape = true;
					break;
				}
			}
			
			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getWheelRotation() < 0 && scale > .3)
				setScale(scale*.9);
			if(e.getWheelRotation() > 0 && scale < 2.5)
				setScale(scale*1.1);
		}	
		
		
		public void mouseClicked(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();			
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth() / 2.0, getHeight() / 2.0);
			transform.scale(scale, scale);
			transform.translate( -imageWidth / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
			
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			//System.out.println(w_X + "," + w_Y);
			
			
			for(Field f : fields) {
				
				if(w_X > f.getXcoord() && w_X < f.getXcoord()+f.getWidth()) {
					
					
					for(int i = 0; i < project.getRecordsPerImage(); i++) {
					
						if(w_Y > (i*project.getRecordHeight()) + project.getFirstYCoord()
								&& w_Y < ((i+1)*project.getRecordHeight()) + project.getFirstYCoord()) {
							shapes.get(1).setRect(new Rectangle2D.Double(
									f.getXcoord(), 
									(project.getFirstYCoord() + i*project.getRecordHeight()), 
									f.getWidth(), 
									project.getRecordHeight()));
						}
					}
				}
			}
			
			
			repaint();
		}
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

//		@Override
//		public void componentResized(ComponentEvent e) {
//			updateTextShapes();
//		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	
	/////////////////
	// Drawing Shape
	/////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void setRect(Rectangle2D rect);
		void setImage(Image image);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
		void setColor(Color color);
		Color getColor();
	}


	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		public void setRect(Rectangle2D rect) {
			this.rect = rect;
		}
		
		public void setColor(Color color) {
			this.color = color;
		}
		public Color getColor() {
			return color;
		}
		
		
		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}

		@Override
		public void setImage(Image image) {
			// TODO Auto-generated method stub
			
		}


	}


//	class DrawingLine implements DrawingShape {
//
//		private Line2D line;
//		private Color color;
//		
//		public DrawingLine(Line2D rect, Color color) {
//			this.line = rect;
//			this.color = color;
//		}
//
//		@Override
//		public boolean contains(Graphics2D g2, double x, double y) {
//
//			final double TOLERANCE = 5.0;
//			
//			Point2D p1 = line.getP1();
//			Point2D p2 = line.getP2();
//			Point2D p3 = new Point2D.Double(x, y);
//			
//			double numerator = (p3.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p3.getY() - p1.getY()) * (p2.getY() - p1.getY());
//			double denominator =  p2.distance(p1) * p2.distance(p1);
//			double u = numerator / denominator;
//			
//			if (u >= 0.0 && u <= 1.0) {
//				Point2D pIntersection = new Point2D.Double(	p1.getX() + u * (p2.getX() - p1.getX()),
//															p1.getY() + u * (p2.getY() - p1.getY()));
//				
//				double distance = pIntersection.distance(p3);
//				
//				return (distance <= TOLERANCE);
//			}
//			
//			return false;
//		}
//
//		@Override
//		public void draw(Graphics2D g2) {
//			g2.setColor(color);
//			g2.setStroke(stroke);
//			g2.draw(line);
//		}	
//		
//		@Override
//		public Rectangle2D getBounds(Graphics2D g2) {
//			return line.getBounds2D();
//		}
//	}


	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
		
		public void setImage(Image image) {
			this.image = image;
		}

		@Override
		public void setRect(Rectangle2D rect) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public Color getColor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setColor(Color color) {
			// TODO Auto-generated method stub
			
		}
	}


	public void clear() {
		shapes.clear();
		repaint();
	}

	public void toggleHighlights() {
		
		Color blue = new Color(119, 108, 216, 100);
		Color invisible = new Color(119, 108, 216, 0);
		
		int transparency = shapes.get(1).getColor().getAlpha();
		if(shapes.get(1).getColor().getAlpha() == 100)
			shapes.get(1).setColor(invisible);
		
		//else if(shapes.get(1).getColor().getAlpha() == 255);
		else	
			shapes.get(1).setColor(blue);
			
		repaint();
	}




//	class DrawingText implements DrawingShape {
//
//		private String text;
//		private Color color;
//		private Point2D location;
//		
//		public DrawingText(String text, Color color, Point2D location) {
//			this.text = text;
//			this.color = color;
//			this.location = location;
//		}
//
//		@Override
//		public boolean contains(Graphics2D g2, double x, double y) {
//			Rectangle2D bounds = getBounds(g2);
//			return bounds.contains(x, y);
//		}
//
//		@Override
//		public void draw(Graphics2D g2) {
//			g2.setColor(color);
//			g2.setFont(font);
//			g2.drawString(text, (int)location.getX(), (int)location.getY());
//		}
//		
//		@Override
//		public Rectangle2D getBounds(Graphics2D g2) {
//			FontRenderContext context = g2.getFontRenderContext();
//			Rectangle2D bounds = font.getStringBounds(text, context);
//			bounds.setRect(location.getX(), location.getY() + bounds.getY(), 
//							bounds.getWidth(), bounds.getHeight());
//			return bounds;
//		}
//		
//		public String getText() {
//			return text;
//		}
//		
//		public void setText(String value) {
//			text = value;
//		}
//	}
	
}




