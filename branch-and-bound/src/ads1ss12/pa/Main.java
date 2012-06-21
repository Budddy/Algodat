
package ads1ss12.pa;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Diese Klasse enth&auml;lt nur die {@link #main main()}-Methode zum Starten
 * des Programms, sowie {@link #printDebug(String)} und {@link #printDebug(Object)} zum Ausgeben von Debug Meldungen.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei der Abgabe werden diese &Auml;nderungen
 * verworfen und es k&ouml;nnte dadurch passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public class Main {
	
	/**
	 * Der Name der Datei, aus der die Testinstanz auszulesen ist. Ist <code>
	 * null</code>, wenn von {@link System#in} eingelesen wird.
	 */
	private static String	fileName	= "C:\\Users\\Markus\\SkyDrive\\Dropbox\\uni\\Algodat2\\tests\\input\\0004";
	
	/** Der abgeschnittene Pfad */
	private static String	choppedFileName;
	
	/**
	 * Mit diesem flag kann verhindert werden, dass der Thread nach Ablauf der
	 * Zeit beendet wird.
	 */
	private static boolean	dontStop	= false;
	
	/** Test flag f&uuml;r Laufzeit Ausgabe */
	private static boolean	test		= true;
	
	/** Debug flag f&uuml;r zus&auml;tzliche Debug Ausgaben */
	private static boolean	debug		= false;
	
	/** Die Anzahl der Knoten */
	private static Integer	numNodes;
	
	/** Die Anzahl der Kanten */
	private static Integer	numEdges;
	
	/** Der Wert k */
	private static Integer	k;
	
	/** Der Schwellwert f&uuml;r die gelbe Schranke */
	private static Integer	threshold;
	
	/**
	 * Gibt die Meldung <code>msg</code> aus und beendet das Programm.
	 * 
	 * @param msg
	 *            Die Meldung die ausgegeben werden soll.
	 */
	private static void bailOut(String msg) {
	
		System.out.println();
		System.err.println((Main.test ? Main.choppedFileName + ": " : "") + "ERR " + msg);
		System.exit(1);
	}
	
	/**
	 * Diese Methode &uuml;berpr&uuml;ft ob eine L&ouml;sung einen g&uuml;ltigen Baum mit exakt
	 * k Knoten darstellt.
	 * 
	 * @param solution
	 *            Die L&ouml;sung die &uuml;berpr&uuml;ft werden soll
	 * @param k
	 *            Anzahl der erforderlichen Knoten.
	 */
	private static void checkTree(Set<Edge> solution, int k) {
	
		// check tree
		Map<Integer, Set<Integer>> adjlist = new HashMap<Integer, Set<Integer>>();
		Set<Integer> nodes = new HashSet<Integer>();
		
		// create adjlist
		for (Edge e : solution) {
			nodes.add(e.node1);
			nodes.add(e.node2);
			Set<Integer> s = null;
			if (adjlist.containsKey(e.node1)) {
				s = adjlist.get(e.node1);
			}
			else {
				s = new HashSet<Integer>();
				adjlist.put(e.node1, s);
			}
			s.add(e.node2);
			s = null;
			if (adjlist.containsKey(e.node2)) {
				s = adjlist.get(e.node2);
			}
			else {
				s = new HashSet<Integer>();
				adjlist.put(e.node2, s);
			}
			s.add(e.node1);
		}
		
		// check overall node count
		if (nodes.size() != k) {
			Main.bailOut("Der K-MST enthaelt nicht genau k Knoten!");
		}
		// check reachable nodes
		nodes = new HashSet<Integer>();
		Map<Integer, Integer> predecessor = new HashMap<Integer, Integer>();
		
		// we stored only edges (n1,n2) in the adjlist if n1<n2
		// so if we start at the minimal element we should reach all nodes
		int start = Collections.min(adjlist.keySet());
		nodes.add(start);
		Main.collectNodes(start, adjlist, nodes, predecessor);
		if (nodes.size() != k) {
			Main.bailOut("Der k-MST ist nicht zusammenhaengend!");
		}
	}
	
	/**
	 * Generates a chopped String representation of the filename.
	 */
	private static void chopFileName() {
	
		if (Main.fileName == null) {
			Main.choppedFileName = "System.in";
			return;
		}
		
		int i = Main.fileName.lastIndexOf(File.separatorChar);
		
		if (i > 0) i = Main.fileName.lastIndexOf(File.separatorChar, i - 1);
		if (i == -1) i = 0;
		
		Main.choppedFileName = ((i > 0) ? "..." : "") + Main.fileName.substring(i);
	}
	
	/**
	 * Diese Methode durchwandert den Baum, &uuml;berpr&uuml;ft Kreisfreiheit und sammelt
	 * alle Knoten.
	 * 
	 * @param key
	 *            Der aktuelle Knoten
	 * @param adjlist
	 *            Die Adjazenzliste des Baums
	 * @param nodes
	 *            Die gesammelten Knoten.
	 * @param predecessor
	 *            Liste der Vorg&auml;nger
	 */
	private static void collectNodes(int key, Map<Integer, Set<Integer>> adjlist, Set<Integer> nodes,
			Map<Integer, Integer> predecessor) {
	
		if (adjlist.containsKey(key)) {
			for (Integer c : adjlist.get(key)) {
				if (nodes.contains(c)) {
					if (c.equals(predecessor.get(key))) {
						// this is ok and that key must exists
						continue;
					}
					// loop check
					Main.bailOut("Loesung ist nicht kreisfrei!");
				}
				nodes.add(c);
				predecessor.put(c, key);
				Main.collectNodes(c, adjlist, nodes, predecessor);
			}
		}
		
	}
	
	/**
	 * Liest die Daten einer Testinstanz ein und &uuml;bergibt sie an die
	 * entsprechenden Methoden der k-MST Implementierung.
	 * 
	 * <p>
	 * Wenn auf der Kommandozeile die Option <code>-d</code> angegeben wird, werden s&auml;mtliche Strings, die an
	 * {@link Main#printDebug(String)} &uuml;bergeben werden, ausgegeben.
	 * </p>
	 * 
	 * <p>
	 * Der erste String in <code>args</code>, der <em>nicht</em> mit <code>-d
	 * </code>, <code>-t</code>, oder <code>-s</code> beginnt, wird als der Pfad zur Datei interpretiert, aus der die
	 * Testinstanz auszulesen ist. Alle nachfolgenden Parameter werden ignoriert. Wird kein Dateiname angegeben, wird
	 * die Testinstanz &uuml;ber {@link System#in} eingelesen.
	 * </p>
	 * 
	 * @param args
	 *            Die von der Kommandozeile &uuml;bergebenen Argumente. Die
	 *            Option <code>-d</code> aktiviert debug-Ausgaben &uuml;ber {@link #printDebug(String)}, <code>-t</code>
	 *            gibt
	 *            zus&auml;tzlich Dateiname und Laufzeit aus und <code>-s</code> verhindert, dass Ihr Algorithmus nach
	 *            30 Sekunden beendet
	 *            wird. Der erste andere String wird als Dateiname
	 *            interpretiert.
	 */
	public static void main(String[] args) {
	
		Scanner is = Main.processArgs(args);
		
		SecurityManager oldsm = null;
		try {
			oldsm = System.getSecurityManager();
			SecurityManager sm = new ADS1SecurityManager();
			System.setSecurityManager(sm);
		}
		catch (SecurityException e) {
			Main.bailOut("Error: could not set security manager: " + e);
		}
		
		try {
			Main.run(Main.readInput(is));
			// Security Manager ruecksetzen
			System.setSecurityManager(oldsm);
		}
		catch (SecurityException se) {
			Main.bailOut("Unerlaubter Funktionsaufruf: \"" + se.toString() + "\"");
		}
		catch (NumberFormatException e) {
			Main.bailOut("Falsches Inputformat: \"" + e.toString() + "\"");
		}
		catch (Exception e) {
			e.printStackTrace();
			Main.bailOut("Ausnahme \"" + e.toString() + "\"");
		}
		
	}
	
	/**
	 * &Ouml;ffnet die Eingabedatei und gibt einen {@link Scanner} zur&uuml;ck,
	 * der von ihr liest. Falls kein Dateiname angegeben wurde, wird von {@link System#in} gelesen.
	 * 
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	private static Scanner openInputFile() {
	
		if (Main.fileName != null) try {
			return new Scanner(new FileInputStream(Main.fileName));
		}
		catch (NoSuchElementException e) {
			Main.bailOut("\"" + Main.fileName + "\" is empty");
		}
		catch (Exception e) {
			Main.bailOut("could not open \"" + Main.fileName + "\" for reading");
		}
		
		return new Scanner(System.in);
		
	}
	
	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code> gestartet wurde, wird <code>msg</code>
	 * zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Object das ausgegeben werden soll.
	 */
	public static void printDebug(Object msg) {
	
		Main.printDebug(msg.toString());
	}
	
	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code> gestartet wurde, wird <code>msg</code>
	 * zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Text der ausgegeben werden soll.
	 */
	public static synchronized void printDebug(String msg) {
	
		if (!Main.debug) return;
		
		System.out.println(Main.choppedFileName + ": DBG " + msg);
	}
	
	/**
	 * Interpretiert die Parameter, die dem Programm &uuml;bergeben wurden und
	 * gibt einen {@link Scanner} zur&uuml;ck, der von der Testinstanz liest.
	 * 
	 * @param args
	 *            Die Eingabeparameter
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	protected static Scanner processArgs(String[] args) {
	
		for (String a : args) {
			if (a.equals("-s")) {
				Main.dontStop = true;
			}
			else
				if (a.equals("-t")) {
					Main.test = true;
				}
				else
					if (a.equals("-d")) {
						Main.debug = Main.test = true;
					}
					else {
						Main.fileName = a;
						
						break;
					}
		}
		
		return Main.openInputFile();
	}
	
	/**
	 * Liest einen Testfall vom &uuml;bergebenen {@link Scanner} ein.
	 * 
	 * @param is
	 *            Der {@link Scanner}
	 * @return die Menge der Kanten im gegebenen Graphen
	 * @throws Exception
	 *             falls der Testfall nicht der Spezifikation entspricht.
	 */
	protected static Set<Edge> readInput(Scanner is) throws Exception {
	
		Main.numNodes = Integer.valueOf(is.nextLine());
		Main.numEdges = Integer.valueOf(is.nextLine());
		
		Main.k = Integer.valueOf(is.nextLine());
		if (Main.k <= 0) {
			throw new Exception("k negativ!");
		}
		
		Main.threshold = Integer.valueOf(is.nextLine());
		
		Set<Edge> e = new HashSet<Edge>((int) (Main.numEdges / 0.75));
		
		for (int edge_counter = 0; (is.hasNext()); edge_counter++) {
			
			String val[] = is.nextLine().split(" ");
			if (val.length != 4) {
				throw new Exception("Fehlerhafte Zeile (Kante #" + edge_counter + ")");
			}
			
			int numEdge = Integer.valueOf(val[0]);
			int node1 = Integer.valueOf(val[1]);
			int node2 = Integer.valueOf(val[2]);
			int weight = Integer.valueOf(val[3]);
			
			if (numEdge != edge_counter) {
				throw new Exception("Fehlerhafte Zeile (Kante #" + edge_counter + ")");
			}
			
			if (!((0 <= node1) && (node1 < Main.numNodes))) {
				throw new Exception("Knoten 1 fehlerhaft (Kante #" + edge_counter + ")");
			}
			if (!((0 <= node2) && (node2 < Main.numNodes))) {
				throw new Exception("Knoten 1 fehlerhaft (Kante #" + edge_counter + ")");
			}
			if (weight < 0) {
				throw new Exception("Gewicht negativ (Kante #" + edge_counter + ")");
			}
			
			e.add(new Edge(node1, node2, weight));
		}
		
		if (e.size() != Main.numEdges) {
			throw new Exception("Nicht genuegend Kanten gefunden!");
		}
		
		return e;
	}
	
	/**
	 * Startet Ihre k-MST Implementierung mit einen Testfall und
	 * &uuml;berpr&uuml;ft danach Ihre L&ouml;sung.
	 * 
	 * @param edges
	 *            Kantenmege des Graphen
	 * 
	 * @throws Exception
	 *             Signalisiert eine Ausnahme
	 */
	@SuppressWarnings("deprecation")
	protected static void run(Set<Edge> edges) throws Exception {
	
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		long offs = end - start;
		long timeout = 30000; // 30 seconds
		
		Main.chopFileName();
		
		AbstractKMST kmst = new KMST(Main.numNodes, Main.numEdges, new HashSet<Edge>(edges), Main.k);
		
		// create kmst thread
		Thread thread = new Thread(kmst, "k-MST Thread");
		// start kmst thread
		thread.start();
		
		if (Main.dontStop) thread.join(0);
		else {
			// kill kmst thread after at most timeout millisecs
			thread.join(timeout);
			if (thread.isAlive()) thread.stop();
		}
		
		// store end time
		end = System.currentTimeMillis();
		
		AbstractKMST.BnBSolution sol = kmst.getSolution();
		Set<Edge> solution = sol.getBestSolution();
		int upper_bound = sol.getUpperBound();
		
		// destoy user object
		kmst = null;
		
		// check solution
		
		if (solution == null) {
			Main.bailOut("keine gueltige Loesung!");
		}
		
		// check edges
		if (!edges.containsAll(solution)) {
			Main.bailOut("Loesung enthaelt Kanten die nicht im Ursprungsgraph vorhanden sind!");
		}
		
		// check tree
		Main.checkTree(solution, Main.k);
		
		// check weight
		int weight = 0;
		
		for (Edge e : solution) {
			weight += e.weight;
		}
		
		if (weight != upper_bound) {
			Main.bailOut("Die obere Grenze muss immer gleich der aktuell besten Loesung sein!");
		}
		
		// Ergebnis ausgeben
		StringBuffer msg = new StringBuffer(Main.test ? Main.choppedFileName + ": " : "");
		
		long sum = end - start - offs;
		
		Main.printDebug("Loesung: " + solution);
		if (upper_bound > Main.threshold)
			Main.bailOut("zu schlechte Loesung: Ihr Ergebnis " + upper_bound + " liegt ueber dem Schwellwert ("
					+ Main.threshold + ")");
		
		msg.append("Ihr Wert ist unter dem Schwellwert\n");
		msg.append(upper_bound);
		
		if (Main.test) msg.append(", Zeit: " + (/* sum > 1000 ? sum / 1000 + "s" : */sum + "ms"));
		
		System.out.println();
		System.out.println(msg.toString());
	}
	
	/**
	 * The constructor is private to hide it from JavaDoc.
	 * 
	 */
	private Main() {
	
	}
	
}
