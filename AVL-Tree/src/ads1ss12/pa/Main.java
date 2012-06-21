
package ads1ss12.pa;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

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
	private static String	fileName	= null;
	
	/** Der abgeschnittene Pfad */
	private static String	choppedFileName;
	
	/** Test flag f&uuml;r Laufzeit Ausgabe */
	private static boolean	test		= false;
	
	/** Debug flag f&uuml;r zus&auml;tzliche Debug Ausgaben */
	private static boolean	debug		= false;
	
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
	 * &Uuml;berpr&uuml;ft die H&ouml;he eines AVL-Knotens.
	 * 
	 * @param root
	 *            Wurzelknoten
	 * @return H&ouml;he des Worzelknoten.
	 * @throws NodeNotBalancedException
	 *             Ausnahme falls ein Knoten nicht balanciert ist
	 */
	protected static int checkHeight(AvlNode root) throws NodeNotBalancedException {
	
		if (root == null) return 0;
		
		int h1 = Main.checkHeight(root.left);
		int h2 = Main.checkHeight(root.right);
		int bal = h2 - h1;
		int h = Math.max(h1, h2) + 1;
		
		if ((bal * bal) > 1) throw new NodeNotBalancedException(root, h1, h2);
		return h;
	}
	
	/**
	 * &Uuml;berpr&uuml;ft die Elternknoten.
	 * 
	 * @param v
	 *            Knoten
	 */
	private static void checkParent(AvlNode v) {
	
		if (v == null) return;
		if (v.left != null) {
			if (v.left.parent != v) {
				Main.bailOut("Elternknoten nicht richtig gesetzt: " + v.key);
				Main.checkParent(v.left);
			}
		}
		if (v.right != null) {
			if (v.right.parent != v) {
				Main.bailOut("Elternknoten nicht richtig gesetzt: " + v.key);
				Main.checkParent(v.right);
			}
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
	 * Liest die Daten einer Testinstanz ein und &uuml;bergibt sie an die
	 * entsprechenden Methoden der AVL-Baum Implementierung.
	 * 
	 * <p>
	 * Wenn auf der Kommandozeile die Option <code>-d</code> angegeben wird, werden s&auml;mtliche Strings, die an
	 * {@link Main#printDebug(String)} &uuml;bergeben werden, ausgegeben.
	 * </p>
	 * 
	 * <p>
	 * Der erste String in <code>args</code>, der <em>nicht</em> mit <code>-d
	 * </code>, oder <code>-t</code> beginnt, wird als der Pfad zur Datei interpretiert, aus der die Testinstanz
	 * auszulesen ist. Alle nachfolgenden Parameter werden ignoriert. Wird kein Dateiname angegeben, wird die
	 * Testinstanz &uuml;ber {@link System#in} eingelesen.
	 * </p>
	 * 
	 * @param args
	 *            Die von der Kommandozeile &uuml;bergebene Argumente. Die
	 *            Option <code>-d</code> aktiviert debug-Ausgaben &uuml;ber {@link #printDebug(String)}, <code>-t</code>
	 *            gibt
	 *            zus&auml;tzlich Dateiname und Laufzeit aus. Der erste andere
	 *            String wird als Dateiname interpretiert.
	 */
	public static void main(String[] args) {
	
		InputScanner is = Main.processArgs(args);
		
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
			Main.run(is);
			// Security Manager ruecksetzen
			System.setSecurityManager(oldsm);
		}
		catch (SecurityException se) {
			Main.bailOut("Unerlaubter Funktionsaufruf: \"" + se.toString() + "\"");
		}
		catch (NumberFormatException e) {
			Main.bailOut("Falsches Inputformat: \"" + e.toString() + "\"");
		}
		catch (NodeNotBalancedException e) {
			Main.bailOut("Baum nicht balanciert: \"" + e.toString() + "\"");
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
	private static InputScanner openInputFile() {
	
		if (Main.fileName != null) try {
			return new InputScanner(new FileInputStream(Main.fileName));
		}
		catch (NoSuchElementException e) {
			Main.bailOut("\"" + Main.fileName + "\" is empty");
		}
		catch (Exception e) {
			Main.bailOut("could not open \"" + Main.fileName + "\" for reading");
		}
		
		return new InputScanner(System.in);
		
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
	public static void printDebug(String msg) {
	
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
	protected static InputScanner processArgs(String[] args) {
	
		for (String a : args) {
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
	 * Startet einen Testfall und f&uuml;gt Knoten in den AVL-Baum ein bzw.
	 * l&ouml;scht Knoten aus dem AVL-Baum.
	 * 
	 * @param is
	 *            Input Scanner
	 * @throws Exception
	 *             Signalisiert eine Ausnahme
	 */
	protected static void run(InputScanner is) throws Exception {
	
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		long offs = end - start;
		
		Main.chopFileName();
		
		AvlTree avl = new AvlTree();
		Set<Integer> set = new TreeSet<Integer>();
		
		start = System.currentTimeMillis();
		while (is.nextValue()) {
			int val = is.getValue();
			
			switch (is.getType()) {
				case INSERT:
					Main.printDebug("Insert: " + val);
					set.add(val);
					avl.insert(val);
					break;
				case REMOVE:
					Main.printDebug("Remove: " + val);
					set.remove(val);
					avl.remove(val);
					break;
				default:
					Main.bailOut("Unbekannter Eingabetyp: " + is.getType().toString());
			}
			
			// Ueberpruefe Balance
			Main.checkHeight(avl.root);
		}
		
		end = System.currentTimeMillis();
		
		// Inorder
		ArrayList<AvlNode> inorder = avl.inorder();
		
		// Ueberpruefe Anzahl der Knoten
		int n = inorder.size();
		
		if (n != set.size()) {
			Main.bailOut("Anzahl der Knoten nicht korrekt: " + n + " (Sollwert: " + set.size() + ")");
		}
		
		// Ueberpruefe Elternknoten
		if ((avl.root != null) && (avl.root.parent != null)) {
			Main.bailOut("Elternknoten der Wurzel ist nicht richtig gesetzt: " + avl.root.key);
		}
		Main.checkParent(avl.root);
		
		// Ueberpruefe Sortierung
		ArrayList<Integer> parents = new ArrayList<Integer>(n);
		if (n > 0) {
			// Elternknoten speichern
			parents.add(0, inorder.get(0).parent == null ? null : inorder.get(0).parent.key);
		}
		for (int i = 1; i < n; i++) {
			AvlNode v = inorder.get(i);
			if (inorder.get(i - 1).key >= v.key) {
				Main.bailOut("Knoten nicht sortiert: " + inorder.get(i - 1).key + " vs. " + v.key + " (Position: "
						+ (i - 1) + " vs. " + i + ")");
			}
			// Ueberpruefe Key (Blattknoten!)
			if (!set.contains(v.key)) {
				Main.bailOut("Falscher Knoten im Baum: " + v.key);
			}
			// Elternknoten speichern
			parents.add(i, v.parent == null ? null : v.parent.key);
		}
		
		// Ergebnis ausgeben
		StringBuffer msg = new StringBuffer(Main.test ? Main.choppedFileName + ": " : "");
		
		long sum = end - start - offs;
		
		msg.append(parents.toString());
		
		if (Main.test) msg.append(", Zeit: " + sum + " ms");
		
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
