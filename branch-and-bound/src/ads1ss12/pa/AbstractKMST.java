
package ads1ss12.pa;

import java.util.Set;

/**
 * Abstrakte Klasse zum Berechnen eines k-MST mittels Branch-and-Bound.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei der Abgabe werden diese &Auml;nderungen
 * verworfen und es k&ouml;nnte dadurch passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public abstract class AbstractKMST implements Runnable {
	
	public final class BnBSolution {
		
		private int			upperBound	= Integer.MAX_VALUE;
		private Set<Edge>	bestSolution;
		
		public BnBSolution(int newUpperBound, Set<Edge> newSoluton) {
		
			this.upperBound = newUpperBound;
			this.bestSolution = newSoluton;
		}
		
		/**
		 * @return Die Kanten der bisher besten L&ouml;sung
		 */
		public Set<Edge> getBestSolution() {
		
			return this.bestSolution;
		}
		
		/**
		 * @return Die obere Schranke
		 */
		public int getUpperBound() {
		
			return this.upperBound;
		}
		
	}
	
	/** Die bisher beste L&ouml;sung */
	private BnBSolution	sol;
	
	/**
	 * Gibt die bisher beste gefundene L&ouml;sung zur&uuml;ck.
	 * 
	 * @return Die bisher beste gefundene L&ouml;sung.
	 */
	final public BnBSolution getSolution() {
	
		return this.sol;
	}
	
	/**
	 * Diese Methode setzt einen neue (beste) L&ouml;sung.
	 * 
	 * <p>
	 * <strong>ACHTUNG:</strong> die L&ouml;sung wird nur &uuml;bernommen wenn <code>newUpperBound</code> niedriger ist
	 * als {@link #upperBound}.
	 * </p>
	 * 
	 * @param newUpperBound
	 *            neue obere Grenze
	 * @param newSoluton
	 *            neue beste L&ouml;sung
	 * @return Wahr wenn die L&ouml;sung &uuml;bernommen wurde.
	 */
	final public synchronized boolean setSolution(int newUpperBound, Set<Edge> newSoluton) {
	
		if ((this.sol == null) || (newUpperBound < this.sol.getUpperBound())) {
			this.sol = new BnBSolution(newUpperBound, newSoluton);
			return true;
		}
		return false;
	}
	
}
