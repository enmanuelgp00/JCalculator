package calculator.frame;

import calculator.Calculator;
import calculator.panel.MainPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CalculatorFrame extends JFrame {
	public CalculatorFrame( Calculator calculator ) {
		super();
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		add( new MainPanel( calculator ) ); 
		pack();
		setResizable( false );
		setVisible( true );
		
	}
}