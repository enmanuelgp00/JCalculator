package calculator;

import calculator.frame.CalculatorFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Scanner;

public class Calculator {
	String input;
	Evaluation.Content evaluationInput;
	static Constant[] CONSTANTS = new Constant[] {
		new Constant('π', Math.PI ),
		new Constant('e', Math.E )
		
	};
	
	static Operator[] OPERATORS = new Operator[] {
		new Operator('√') {
			
			
			@Override
			double evaluate() {
				return root( getParams()[0], getParams()[1]);
			}
			private double root( double a, double b ) {
				double tolerance = 0.0001;
				double scale = 1;
				double r = 1;
				if ( r > b ) {
					r = tolerance;
					scale = tolerance;
				}
				double root;
				while ( ( root = Math.pow( r, a )) < b ) {  
					if ( ( Math.pow(r + scale, a ) ) > b ) {
						r += tolerance;
					} else {
						r += scale;					
					}
				}
				return  r;	
			}
		},
		new Operator('^') {
			@Override
			double evaluate() {
				
				return Math.pow( getParams()[0], getParams()[1]);
			}
		},
		new Operator('*') {
			@Override
			double evaluate() {
				
				return getParams()[0] * getParams()[1];
			}
		}, 
		new Operator('/') {
			@Override
			double evaluate() {
				
				return getParams()[0] / getParams()[1];
			}
		},
		new Operator('%') {
			@Override
			double evaluate() {
				
				return getParams()[0] % getParams()[1];
			}
		},
		new Operator('-') {
			@Override
			double evaluate() {
				
				return getParams()[0] - getParams()[1];
			}
		},
		new Operator('+') {
			@Override
			double evaluate() {
				
				return getParams()[0] + getParams()[1];
			}
		}
	};
	
	Evaluation.Content evaluactionInput;

	Calculator() {
		
	}
	
	public void setEvaluationInput( String input ) {
		try {
			evaluationInput = new Evaluation.Content( input );
		} catch( Exception e ) {
			e.printStackTrace();
		}	
	}

	public static String removeDotIfJustZero( String num ) {
		try { 
			double val = Double.parseDouble(num);
			String s = String.format("%.3f", val );
			System.out.println(s);
			int rightZeroCount = 0;
			for ( int i = s.length() - 1; i > 0; i-- ) {
				if ( s.charAt(i) == '0' || s.charAt(i) == '.') {
					rightZeroCount++;
					if ( s.charAt(i) == '.') {
						break;
					}
				} else {
					break;
				}
			} 
			if ( rightZeroCount != 0 ) {
				return s.substring(0, s.length() - rightZeroCount );			
			}
			return s;
			
		} catch( Exception e) { }
		return num;
	}
	
	public String compute( Evaluation.Content evaluationInput ) {
		if ( evaluationInput == null ) {
			return null;
		}         
		Evaluation evaluation = new Evaluation( evaluationInput );  
		evaluation.content().print();
		if ( evaluation.hasParentesis() ) {
			Evaluation.Content valuesInside = evaluation.getEvaluationInsideParentesis();
			evaluation.replaceEvaluationInsideParentesis( compute( valuesInside ) );
		}
		if ( evaluation.content().values.size() > 1 ) {     
			return compute( evaluation.nextAlgorithm() );
		}
		return evaluation.content().values.get(0);
	}
	
	
	public class Evaluation {
		Content input;
		
		Evaluation( Content evaluationInput ) {
			this.input = evaluationInput;	
		}
		
		public boolean hasParentesis() {
			return (input.values.contains("(") && input.values.contains(")"));
		}
		
		public Content nextAlgorithm() {
			for ( Operator op : OPERATORS ) {
				String symbol = String.valueOf(op.symbol);
				for ( int i = 0; i < input.values.size(); i++ ) {
					if ( symbol.equals(input.values.get(i)) ) {
						try {        											
							op.setParams(
								Double.parseDouble(input.values.get(i - 1)), 
								Double.parseDouble(input.values.get(i + 1))
							);
							
							input.values.set(i - 1, String.valueOf(op.evaluate()));
							input.values.remove(i);                
							input.values.remove(i);
							return input;
						} catch ( Exception e ) {}
											
					}
				}
			}
			return input;
		}
		
		public Evaluation.Content getEvaluationInsideParentesis() { 
			if ( hasParentesis() ) {
			
				char ch;
				int v = 1;
				int matchParentesisIndex;
				
				String content = input.values.stream().collect( Collectors.joining(""));
				for ( int i = content.indexOf("(") + 1; i < content.length(); i++  ) {
					ch = content.charAt(i);
					if ( ch == '(') {
						v++;
					} else if ( ch == ')') {
						v--;
					}
					if ( v == 0 ) {
						matchParentesisIndex = i;
						String inside = content.substring( content.indexOf("(") + 1, matchParentesisIndex );
						try {
							return new Evaluation.Content( inside );
						} catch( Exception e ) {	}
						break;
					}
					
				}
				
				                         
			}
			return null;
		
		}
		
		public void replaceEvaluationInsideParentesis( String val ) {
			String[] fragments = new String[3];
			char ch;
			int v = 1;
			int matchParentesisIndex;
				
			String content = input.values.stream().collect( Collectors.joining(""));
			for ( int i = content.indexOf("(") + 1; i < content.length(); i++  ) {
				ch = content.charAt(i);
					
				if ( ch == '(') {
					v++;
				} else if ( ch == ')') {
					v--;
				}
				if ( v == 0 ) {
					matchParentesisIndex = i;
					fragments[0] = content.substring( 0, content.indexOf("(") );
					fragments[1] = val;
					fragments[2] = content.substring( matchParentesisIndex + 1);
					
					break;
				}
			}
			
					
			if ( fragments[0] != null && fragments[0].length() > 0 ) {
				if ( Character.isDigit(fragments[0].charAt(fragments[0].length() - 1 ))) {
					fragments[0] = fragments[0] + "*";
				}
			}
			
			if ( fragments[2] != null && fragments[2].length() > 0  ) {
				if ( Character.isDigit(fragments[2].charAt(0))) {
					fragments[1] = fragments[1] + "*";
				}
			}
			
			try {
				this.input = new Content(Arrays.<String>asList(fragments).stream().collect(Collectors.joining("")));			
			} catch(Exception e) {}
		}
		
		public Content content() {
			return input;
		}
		
		public static class Content {
		ArrayList<String> values = new ArrayList<>();
		public Content ( String input ) throws Exception {
			
			char ch;
			
			StringBuilder token = new StringBuilder();
			int firstParentesisIndex = input.indexOf("(");
			int v = 0;
			int lastParentesisIndex = -1;
			for ( int i = 0; i < input.length(); i++ ) {
				
				ch = input.charAt(i);
				
				if ( i >= firstParentesisIndex ) {
					if ( ch == '(') {
						v++;
					} else if ( ch == ')') {
						if ( firstParentesisIndex == -1 ) {
							v++;
						}
						if ( --v == 0 && lastParentesisIndex == -1 ) { 
							lastParentesisIndex = i;
						}
					}              
				}
				
				if ( Character.isDigit(ch) || ch == '.' ) {
					token.append(ch);                     
					if ( i == input.length() - 1 ) {
						values.add(token.toString());
					}
					
				} else if ( Calculator.isConstantChar(ch)) {
					if ( token.length() > 0 ) {
						values.add(token.toString());
					}
					token.setLength(0);
					values.add("(");
					values.add(String.valueOf(Calculator.getConstant(ch)));	
					values.add(")");
				} else if ( isOperator(ch)) {
					if ( i == input.length() - 1 ) {
						throw new Exception() { };
					}  else {
						if (token.length() > 0) {
							try {
								Double.parseDouble( token.toString() );
								values.add(token.toString()); 
							} catch( Exception e ) {}
							
							token.setLength(0);
						}
						
						if ( !isOperator(values.getLast().charAt(0))) {	
							
							values.add(String.valueOf(ch));						
						} else { 
							throw new Exception() { };
							
						}
					}
					
					
				} else if ( ch != ' ' ) {
					if ( ch == '(' || ch == ')') {
						if(token.length() > 0) { 
							values.add(token.toString());
							token.setLength(0);
						}
						values.add( String.valueOf(ch));
					} else {
						throw new Exception() {};
					}
				}
			}
			
			if ( firstParentesisIndex != -1 && lastParentesisIndex == -1 ) { 
				throw new Exception(); 
			}
			
			if ( firstParentesisIndex == -1 && lastParentesisIndex != -1 ) { 
				throw new Exception();
			}
			
			if ( v != 0 ) {  
				throw new Exception();				
			}
			
		}
		
		void print() {
			System.out.println("");
			for ( String s : values ) {
				System.out.print("["+removeDotIfJustZero(s)+"]");
			}
		}
		
		
		
		}
	}
	
	
	public static boolean isOperator( char ch ) {
		for ( Operator op : OPERATORS ) {
			if ( ch == op.symbol ) {
				return true;
			}
		}
		return false;
	}
	
	static abstract class Operator {
		final char symbol;
		double[] params = new double[] { 0, 0 };
		
		Operator( char ch ) {
			symbol = ch;
		}
		
		void setParams( double v0, double v1 ) {
			params[0] = v0;
			params[1] = v1;
		}
		
		double[] getParams() {
			return params;
		}
		
		abstract double evaluate();
		
	}
	static private class Constant {
		final char name;
		final double value;
		
		Constant( char name, double value ) {
			  this.name = name;
			  this.value = value;
		}
	}
	
	public static double getConstant( char ch ) {
		for ( Constant c : CONSTANTS ) {
			if (c.name == ch ) {
				return c.value;
			}
		}
		return 0;
	}
	
	public static boolean isConstantChar( char ch )  {
		for ( Constant c : CONSTANTS ) {
			if ( ch == c.name ) {
				return true;
			}
		}
		return false;
	}
	
	public static void main( String[] args ) {
		new CalculatorFrame( new Calculator() );
	}
}