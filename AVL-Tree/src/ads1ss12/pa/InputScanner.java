
package ads1ss12.pa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Diese Klasse wird zum Einlesen der Testinstanzen benutzt.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine Aenderungen in dieser Klasse vor. Bei der Abgabe werden diese Aenderungen verworfen
 * und es koennte dadurch passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public final class InputScanner {
	
	public enum ValueType {
		INVALID, INSERT, REMOVE, FIND
	};
	
	private BufferedReader	input;
	private ValueType		type;
	private int				value;
	private int				numActions;
	private boolean			debugRequested;
	
	public InputScanner() {
	
		this(System.in);
	}
	
	public InputScanner(InputStream in) {
	
		this.input = new BufferedReader(new InputStreamReader(in));
		this.type = ValueType.INVALID;
		this.value = 0;
		this.numActions = -1;
		this.debugRequested = false;
	}
	
	public boolean debugRequested() {
	
		boolean ret = this.debugRequested;
		
		this.debugRequested = false;
		
		return ret;
	}
	
	public int getNumActions() {
	
		return this.numActions;
	}
	
	public ValueType getType() {
	
		return this.type;
	}
	
	public int getValue() throws Exception {
	
		if (this.type == ValueType.INVALID) {
			throw new Exception("Scanner#getValue() called while " + "scanner was in invalid state.");
		}
		
		return this.value;
	}
	
	private String nextLine() throws IOException {
	
		String s = this.input.readLine();
		
		while ("".equals(s))
			s = this.input.readLine();
		
		return s;
	}
	
	public boolean nextValue() throws IOException {
	
		String s = this.nextLine();
		
		while ((s != null) && ((s.charAt(0) == '#') || (this.type == ValueType.INVALID))) {
			if (s.startsWith("#numActions") && (this.numActions < 0)) {
				this.numActions = Integer.parseInt(s.substring(12));
			}
			else
				if (s.startsWith("#insert")) {
					this.type = ValueType.INSERT;
				}
				else
					if (s.startsWith("#remove")) {
						this.type = ValueType.REMOVE;
					}
					else
						if (s.startsWith("#find")) {
							this.type = ValueType.FIND;
						}
						else
							if (s.startsWith("#debug")) {
								this.debugRequested = true;
							}
			
			s = this.nextLine();
		}
		
		if (s == null) {
			this.type = ValueType.INVALID;
			this.value = 0;
			
			return false;
		}
		
		this.value = Integer.parseInt(s);
		
		return true;
	}
	
} // class Scanner
