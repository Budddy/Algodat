
package ads1ss12.pa;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Klasse zum Berechnen eines k-MST mittels Branch-and-Bound. Hier sollen Sie
 * Ihre L&ouml;sung implementieren.
 */
public class KMST extends AbstractKMST {
	
	private int					k;							// Anzahl der einzufügenden Knoten
	private int					max	= Integer.MAX_VALUE;	// maximales Gewicht
	private ArrayList<Edge>[]	adjList;					// Adijazens Liste
	private int					v;
	private HashSet<Edge>		edges;
	private int					anz	= 0;
	
	private HashSet<Set<Edge>>	n;
	
	/**
	 * Der Konstruktor. Hier ist die richtige Stelle f&uuml;r die
	 * Initialisierung Ihrer Datenstrukturen.
	 * 
	 * @param numNodes
	 *            Die Anzahl der Knoten
	 * @param numEdges
	 *            Die Anzahl der Kanten
	 * @param edges
	 *            Die Menge der Kanten
	 * @param k
	 *            Die Anzahl der Knoten, die Ihr MST haben soll
	 */
	@SuppressWarnings("unchecked")
	public KMST(Integer numNodes, Integer numEdges, HashSet<Edge> edges, int k) {
	
		this.edges = edges;
		this.k = k;
		this.adjList = new ArrayList[numNodes];
		this.v = numNodes;
		for (Edge ed : edges) {
			if (this.adjList[ed.node1] == null) this.adjList[ed.node1] = new ArrayList<Edge>();
			if (!this.adjList[ed.node1].contains(ed)) this.adjList[ed.node1].add(ed);
			if (this.adjList[ed.node2] == null) this.adjList[ed.node2] = new ArrayList<Edge>();
			if (!this.adjList[ed.node2].contains(ed)) this.adjList[ed.node2].add(ed);
		}
		this.n = new HashSet<Set<Edge>>();
		this.n = new HashSet<Set<Edge>>();
	}
	
	@SuppressWarnings("unchecked")
	private void baum(BitSet knoten, int gewicht, int tiefe, Set<Edge> se, int kn, ArrayList<Edge> kanten) {
	
		this.n.add(se);
		
		if (tiefe >= this.k) {
			
			if (this.max > gewicht) {
				this.max = gewicht;
				this.setSolution(gewicht, se);
			}
			return;
			
		}
		
		kanten.addAll(this.adjList[kn]);
		Set<Edge> ka;
		Edge e;
		int a = kanten.size();
		for (int i = 0; i < a; i++) {
			e = kanten.get(i);
			
			boolean n1 = knoten.get(e.node1), n2 = knoten.get(e.node2);
			if ((n1 ^ n2) && ((e.weight + gewicht) < this.max)) {
				(ka = new HashSet<Edge>(se)).add(e);
				if (!this.n.contains(ka)) {
					BitSet al;
					if (!n1) {
						(al = (BitSet) knoten.clone()).set(e.node1);
						this.anz++;
						this.baum(al, gewicht + e.weight, tiefe + 1, ka, e.node1, kanten);
					}
					if (!n2) {
						(al = (BitSet) knoten.clone()).set(e.node2);
						this.anz++;
						this.baum(al, gewicht + e.weight, tiefe + 1, ka, e.node2, kanten);
						
					}
				}
				
			}
			
		}
		
		int b = this.adjList[kn].size();
		int siz = kanten.size();
		for (int i = 1; i < b; i++)
			kanten.remove(siz - i);
		
	}
	
	@SuppressWarnings("unchecked")
	private void prim(Edge e1) {
	
		Set<Edge> se = new HashSet<Edge>();
		BitSet knoten = new BitSet(this.v);
		int gewicht = e1.weight;
		if (gewicht >= this.max) {
			return;
		}
		knoten.set(e1.node1);
		knoten.set(e1.node2);
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
		pq.addAll(this.adjList[e1.node1]);
		pq.addAll(this.adjList[e1.node2]);
		se.add(e1);
		pq.remove(e1);
		Edge e;
		for (int i = 2; i < this.k; i++) {
			e = pq.poll();
			while (!(knoten.get(e.node1) ^ knoten.get(e.node2))) {
				if ((e = pq.poll()) == null) return;
			}
			if (!knoten.get(e.node1)) {
				knoten.set(e.node1);
				pq.addAll(this.adjList[e.node1]);
			}
			if (!knoten.get(e.node2)) {
				knoten.set(e.node2);
				pq.addAll(this.adjList[e.node2]);
			}
			gewicht += e.weight;
			if (gewicht >= this.max) {
				return;
			}
			
			se.add(e);
		}
		this.n.add(se);
		if (this.max > gewicht) {
			this.max = gewicht;
			this.setSolution(gewicht, se);
			
		}
	}
	
	/**
	 * Diese Methode bekommt vom Framework maximal 30 Sekunden Zeit zur
	 * Verf&uuml;gung gestellt um einen g&uuml;ltigen k-MST zu finden.
	 * 
	 * <p>
	 * F&uuml;gen Sie hier Ihre Implementierung des Branch-and-Bound Algorithmus ein.
	 * </p>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run() {
	
		PriorityQueue<Edge> kan = new PriorityQueue<Edge>(this.edges);
		
		while (kan.peek() != null) {
			this.prim(kan.poll());
		}
		
		BitSet al = new BitSet();
		for (int i = this.v - 1; i >= 0; i--) {
			al.set(i);
			ArrayList<Edge> arl = new ArrayList<Edge>();
			this.baum(al, 0, 1, new HashSet<Edge>(), i, arl);
			al.clear(i);
		}
		
		System.out.println("Anzahl: " + this.anz);
	}
}
