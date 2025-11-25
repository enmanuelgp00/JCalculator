package calculator.panel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
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
		JLabel label = new JLabel("0");
		label.setHorizontalAlignment( SwingConstants.RIGHT );
		label.setFont( font );
		add( label, gbc ); 
			
		gbc.gridy = 1;
		JLabel result = new JLabel("0");                     
		result.setHorizontalAlignment( SwingConstants.RIGHT );
		result.setFont( font );
		add( result, gbc );

		gbc.gridy = 2;		
		gbc.gridwidth = 1;
		
		for ( String buttonstr : buttonStrs) {
			JButton button = new JButton( buttonstr );
			button.setFont( font );
			button.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent event ) {  
						switch( button.getText() ) {
							case "©":
								label.setText("0");
							break;
							case backspaceChar:
								String content = label.getText().substring(0, label.getText().length() - 1 );
								if ( content.equals("")) {
									content = "0";
								}
								label.setText(content);
							break;
							default:
								content = label.getText();
								if ( content.equals("0")) {
									content = "";
								}
								label.setText( content + button.getText() );
							break;
							
						}
						try {
							Calculator.Evaluation.Content content = new Calculator.Evaluation.Content( label.getText() );
							result.setText(calculator.compute( content ));
						} catch( Exception e ) {
							result.setText("0");
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