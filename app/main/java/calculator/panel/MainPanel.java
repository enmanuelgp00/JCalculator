package calculator.panel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;    
import java.awt.geom.RoundRectangle2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import calculator.Calculator;

public class MainPanel extends JPanel {
	Calculator calculator;
	public MainPanel( Calculator calculator ) {
		super(  new GridBagLayout());
		Font font = new Font("Courier New", Font.PLAIN, 20);
		Border padding = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		setBorder( padding );
		int WIDTH = 3;
		final String backspaceChar = "«";
		final String STANBY = "<";
		this.calculator = calculator;
		
		String[] buttonStrs = new String[] {
			"©","(",")","^",
			"1","2","3","*",
			"4","5","6","/",
			"7","8","9","-",
			backspaceChar,"0",".","+"
		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets( 2, 2, 2, 2 );
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = WIDTH + 1;
		JLabel label = new JLabel(STANBY);
		label.setHorizontalAlignment( SwingConstants.RIGHT );
		label.setFont( font );
		add( label, gbc ); 
			
		gbc.gridy = 1;
		JLabel result = new JLabel(STANBY);                     
		result.setHorizontalAlignment( SwingConstants.RIGHT );
		result.setFont( font );
		add( result, gbc );

		gbc.gridy = 2;		
		gbc.gridwidth = 1;
		
		for ( String buttonstr : buttonStrs) {
			JButton button = new JButton( buttonstr ) {
				int radiusCorners = 7;
				{
					setContentAreaFilled(false);
					setFocusPainted(false);
					setBorderPainted(false);
					setOpaque(false);
				}
				@Override
				public void paintComponent( Graphics g ) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					//fill the roundede rectangle
					if ( getModel().isPressed()) {
						g2.setColor( getBackground().darker());
					} else if ( getModel().isRollover()) {
						g2.setColor(getBackground().brighter());
					} else {
						g2.setColor(getBackground());
					}
					g2.fillRoundRect( 0, 0, getWidth(), getHeight(), /*cornersRadius*/radiusCorners, radiusCorners);
					
					super.paintComponent(g2);
					g2.dispose();
				}
				/*
				@Override
				protected void paintBorder( Graphics g ) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(getForeground());
					g2.drawRoundRect(0, 0, getWidth(), getHeight(), radiusCorners, radiusCorners );
					g2.dispose();
					
				} 
				
				@Override
				public boolean contains( int x, int y ) {
					return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radiusCorners, radiusCorners ).contains( x, y );
				}
				*/
				/*
				ellise button
				@Override
				public void paintComponent( Graphics g ) {
					if (getModel().isArmed()) {
						g.setColor(Color.lightGray);
					} else {
						g.setColor(getBackground());
					}
					g.fillOval(0, 0, getSize().width - 1, getSize().height - 1 );
					super.paintComponent(g);
				}
				@Override
				public void paintBorder( Graphics g ) {
					g.setColor( getForeground());
					g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
				}
				@Override
				public boolean contains( int x, int y) {
					if (getBounds() == null ) return false;
					return new Ellipse2D.Float(0, 0, getWidth(), getHeight()).contains(x , y);
				}
				*/
			};
			button.setFont( font );
			button.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent event ) {  
						switch( button.getText() ) {
							case "©":
								label.setText(STANBY);
							break;
							case backspaceChar:
								String content = label.getText().substring(0, label.getText().length() - 1 );
								if ( content.equals("")) {
									content = STANBY;
								}
								label.setText(content);
							break;
							default:
								content = label.getText();
								if ( content.equals(STANBY)) {
									content = "";
								}
								label.setText( content + button.getText() );
							break;
							
						}
						try {
							Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( label.getText() );
							result.setText(calculator.compute( content ));
						} catch( Exception e ) {
							result.setText(STANBY);
						}
						
				}
			});
			add(  button , gbc );
			if ( gbc.gridx++ == WIDTH ) {
				gbc.gridx = 0;
				gbc.gridy++;
			}
		}
	}
	
}