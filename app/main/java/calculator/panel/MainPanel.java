package calculator.panel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
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
	JLabel result;
	JTextField textfield;
	int caretPosition;
	int WIDTH = 3;
	final String backspaceChar = "«";
	final String STANBY = " ";
	
	
	public MainPanel( Calculator calculator ) {
		super(  new GridBagLayout());
		Font font = new Font("Courier New", Font.PLAIN, 24);
		Border padding = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		setBorder( padding );
		this.calculator = calculator;
		
		String[] buttonStrs = new String[] {
			"(",")","√","^",
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
		textfield = new JTextField() {
			{
				//setOpaque(false);
				//setCaretColor( Color.BLACK );
				setBackground(null);
				setBorder(null);
			}
		};
		
		DefaultCaret caret = new DefaultCaret() {
			{
				setBlinkRate(600);
				setUpdatePolicy( DefaultCaret.ALWAYS_UPDATE);
			}
			@Override
			public void focusLost( FocusEvent e ) {
			
			}
		};
		
		textfield.setCaret( caret );
		textfield.setHorizontalAlignment( SwingConstants.RIGHT );
		textfield.setFont( font );
		add( textfield, gbc ); 
			
		gbc.gridy = 1;
		result = new JLabel(" "); 		
		result.setHorizontalAlignment( SwingConstants.RIGHT );
		result.setFont( font );
		add( result, gbc );

		gbc.gridy = 2;		
		gbc.gridwidth = 1;
		
		textfield.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased( KeyEvent event ) {
			
				try {
					Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( textfield.getText() );
					result.setText(calculator.compute( content ));
				} catch( Exception e ) {
					result.setText(STANBY);
				}	
			}
		});
		
		
		
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
							case backspaceChar:
								String content = textfield.getText();
								StringBuilder newcontent = new StringBuilder();
								int index = textfield.getCaretPosition() - 1;
								int start = textfield.getSelectionStart();
								int end = textfield.getSelectionEnd();
								
								for ( int i = 0; i < content.length(); i++) {
									if ( start != end ) {
										if ( i < start || i > end ) {  
											newcontent.append( content.charAt(i));											
										}
									} else {									
										if ( i != index ) {
											newcontent.append( content.charAt(i));
										}
									}
								}
								textfield.setText(newcontent.toString());
								if ( start != end ) {
									if ( start > -1 ) {
										textfield.setCaretPosition( start );									
									}
								} else {

									if ( index > -1 ) {
										textfield.setCaretPosition(index);								
									}								
								}
							break;
							default:
								write( button.getText().charAt(0) );
							break;
							
						}
						showResult();
						
				}
			});
			
			add(  button , gbc );
			if ( gbc.gridx++ == WIDTH ) {
				gbc.gridx = 0;
				gbc.gridy++;
			}
		}
	}
	boolean isParentesis( char ch )  {
		return ch == '(' || ch == ')';
	}
	void write( char ch ) {
		if ( Character.isDigit(ch) || Calculator.isOperator(ch) ||  isParentesis( ch ) || ch == '.') {
			try {
				if ( textfield.getSelectionStart() != textfield.getSelectionEnd() ) {
					textfield.replaceSelection( String.valueOf(ch));
				} else {
					textfield.getDocument().insertString( textfield.getCaretPosition(), String.valueOf(ch), null );							
				}
			} catch ( Exception e ) { }
		}
	}
	void showResult() {
		
		try {
			Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( textfield.getText() );
			result.setText(  Calculator.removeDotIfJustZero(calculator.compute( content )));
		} catch( Exception e ) {
			result.setText(STANBY);
		}
	}
	
}