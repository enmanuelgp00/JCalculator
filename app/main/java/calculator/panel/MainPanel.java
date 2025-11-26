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
	JTextField inputField;
	Font buttonFont = new Font("Courier New", Font.PLAIN, 24);
	Font outputFont = buttonFont.deriveFont( buttonFont.getSize() + 10.0f);
	Font resultFont = outputFont.deriveFont( Font.BOLD );
	Color backgroundColor = new Color(32, 32, 32 );
	Color primaryColor = Color.WHITE;
	Color secondaryColor = new Color( 255, 128, 192);                    
	Color ternaryColor = new Color( 128, 128, 192 );//(233, 22, 181);
	int caretPosition;
	int WIDTH = 4;
	final String backspaceChar = "«";
	final String cleanChar = "C";
	final String STANBY = " ";
	
	
	public MainPanel( Calculator calculator ) {
		super(  new GridBagLayout());
		Dimension buttonDimension = new Dimension( 60, 50 );
		setBackground( backgroundColor );
		Border padding = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		setBorder( padding );
		this.calculator = calculator;
		
		String[] buttonStrs = new String[] {
			"(",")","%","√","^",
			"1","2","3","/","*",
			"4","5","6","-","+",
			"7","8","9","e","π",
  backspaceChar,"0",".","ₓ",cleanChar
		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets( 1, 1, 1, 1 );
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = WIDTH + 1;
		inputField = new JTextField() {
			{
				//setOpaque(false);
				setCaretColor( secondaryColor );
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
		inputField.setForeground( primaryColor );
		inputField.setCaret( caret );
		inputField.setHorizontalAlignment( SwingConstants.RIGHT );
		inputField.setFont( outputFont );
		add( inputField, gbc );
		inputField.setPreferredSize( inputField.getPreferredSize() ); 
			
		gbc.gridy = 1;
		result = new JLabel(" "); 
		result.setForeground( secondaryColor );		
		result.setHorizontalAlignment( SwingConstants.RIGHT );
		result.setFont( resultFont );
		add( result, gbc );
		result.setPreferredSize( result.getPreferredSize() );

		gbc.gridy = 2;		
		gbc.gridwidth = 1;
		
		inputField.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased( KeyEvent event ) {
			
				try {
					Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( inputField.getText() );
					showResult();
				} catch( Exception e ) {
					result.setText(STANBY);
				}	
			}
		});
		
		
		
		for ( String buttonstr : buttonStrs) {
			JButton button = new JButton( buttonstr ) {
				int radiusCorners = 7;
				{
					setForeground( primaryColor );
					setBackground( backgroundColor );
					setPreferredSize( buttonDimension );
					setContentAreaFilled(false);
					setFocusPainted(false);
					setBorderPainted(false);
					setOpaque(false); // allow color change for g2
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
						/*
						if ( getText().equals(backspaceChar)) {
							GradientPaint gradient = new GradientPaint(
								0, 0, primaryColor,
								getWidth(), getHeight(), secondaryColor.darker()
							);
							g2.setPaint(gradient);
						} else {
						*/
							g2.setColor(getBackground());						
						//}
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
			button.setFont( buttonFont );
			button.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent event ) {  
						switch( button.getText() ) {
							case cleanChar:
								inputField.setText("");
							break;
							case backspaceChar:
								String content = inputField.getText();
								StringBuilder newcontent = new StringBuilder();
								int index = inputField.getCaretPosition() - 1;
								int start = inputField.getSelectionStart();
								int end = inputField.getSelectionEnd();
								
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
								adjustTextInputInside( newcontent.toString(), inputField.getFont());
								inputField.setText(newcontent.toString());
								if ( start != end ) {
									if ( start > -1 ) {
										inputField.setCaretPosition( start );									
									}
								} else {

									if ( index > -1 ) {
										inputField.setCaretPosition(index);								
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
			if ( buttonstr.equals(cleanChar)) {   
				button.setBackground(ternaryColor);
			
			} else if ( buttonstr.equals(backspaceChar)) {
				button.setBackground(secondaryColor);
			} else if ( Character.isDigit(buttonstr.charAt(0)) || buttonstr.equals(backspaceChar) || buttonstr.equals(".") ){
				Color origincolor = button.getBackground();
				Color newcolor = new Color( origincolor.getRed() + 31, origincolor.getGreen() + 31, origincolor.getBlue() + 37 );
				button.setBackground( newcolor );
			} else {
				Color origincolor = button.getBackground();
				Color newcolor = new Color( origincolor.getRed() + 16, origincolor.getGreen() + 16, origincolor.getBlue() + 16 );
				button.setBackground( newcolor );
			}
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
		if ( Character.isDigit(ch) || Calculator.isOperator(ch) ||  isParentesis( ch ) || ch == '.' || Calculator.isConstantChar( ch )) {
			try {
				
				
				if ( inputField.getSelectionStart() != inputField.getSelectionEnd() ) {
					
					inputField.replaceSelection( String.valueOf(ch) );
				} else {
					adjustTextInputInside( inputField.getText() + String.valueOf(ch), inputField.getFont());
					inputField.getDocument().insertString( inputField.getCaretPosition(), String.valueOf(ch), null );							
				}
			} catch ( Exception e ) { }
		}
	}
	
	void adjustTextInputInside( String text, Font fontTest ) {
		FontMetrics fontMetrics = inputField.getFontMetrics( fontTest );
		int textWidth = fontMetrics.stringWidth( text );
		int fieldWidth = inputField.getWidth();
		if ( textWidth >= fieldWidth ) {  
			adjustTextInputInside( text, fontTest.deriveFont( fontTest.getSize() - 5.0f ) );
		}  else  {
			Font newtestf = fontTest.deriveFont( fontTest.getSize() + 5.0f );
			fontMetrics = inputField.getFontMetrics( newtestf );
			textWidth = fontMetrics.stringWidth( text );
			
			if ( textWidth < fieldWidth && newtestf.getSize() < outputFont.getSize()) {
				adjustTextInputInside( text, newtestf );
			} else {
				inputField.setFont(fontTest);				
			}
		} 
	}
	
	void showResult() {
		
		try {
			Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( inputField.getText() );
			result.setText( Calculator.removeDotIfJustZero(calculator.compute( content )));
		} catch( Exception e ) {
			result.setText(STANBY);
		}
	}
	
}