package calculator.frame;

import calculator.Calculator;
import calculator.panel.MainPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CalculatorFrame extends JFrame {
	public CalculatorFrame( Calculator calculator ) {
		super();
		//setSize( 337, 516 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		add( new MainPanel( calculator ) ); 
		pack();
		setVisible( true );
		
	}
}