package calculator.frame;

import calculator.Calculator;
import calculator.panel.MainPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorFrame extends JFrame {
	public CalculatorFrame( Calculator calculator ) {
		super();
		//removeTitleBar
		setUndecorated( true );
		setTitle("JCalculator");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		MainPanel mainPanel = new MainPanel(calculator);
		JPanel titlebar = new JPanel( new FlowLayout(FlowLayout.RIGHT)); 
		MouseAdapter mouseAdapter = new MouseAdapter() {
			Point initialPosMouse;
			
			@Override
			public void mousePressed( MouseEvent event) {
				System.out.println(event.getX());         
				System.out.println(event.getY());
				initialPosMouse = event.getPoint();
				
			}
			@Override
			public void mouseDragged( MouseEvent event ) {
				int x = ( event.getX() - initialPosMouse.x ) + CalculatorFrame.this.getLocation().x ;
				int y = ( event.getY() - initialPosMouse.y ) + CalculatorFrame.this.getLocation().y ;
				Point newPosWindow = new Point(x , y);
				CalculatorFrame.this.setLocation(newPosWindow);
			}
		};
		addMouseMotionListener( mouseAdapter );
		addMouseListener( mouseAdapter );
		//titlebar.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1 ));
		titlebar.setBackground( mainPanel.getBackground( ) );
		JButton closebutton = new JButton("X") {
			{      
				setContentAreaFilled(false);
				setFocusPainted(false);
				setBorderPainted(false);
				setOpaque(false);
			}
			@Override
			public void paintComponent( Graphics g ) {
				Graphics2D g2d = (Graphics2D) g.create(); 
				g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				if ( getModel().isPressed() ) {
					g2d.setColor( new Color( 200, 0, 0 ));
					setForeground( Color.BLACK );
				} else if ( getModel().isRollover() ) {
					g2d.setColor( Color.RED );
				} else {
					g2d.setColor( mainPanel.getBackground());
					setForeground( Color.WHITE );
				}
				g2d.fillRect(0, 0, getWidth(), getHeight() );
				super.paintComponent(g2d);
				g2d.dispose();
			}
		};
		closebutton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				System.exit(0);
			}
		});
		titlebar.add(closebutton);
		add( titlebar, BorderLayout.NORTH );
		add( mainPanel, BorderLayout.CENTER ); 
		pack();
		setResizable( false );
		setVisible( true );
		
	}
	
}