
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
public class KMST_1 extends AbstractKMST {
	
	private int					k;							// Anzahl der einzufügenden Knoten
	private int					max	= Integer.MAX_VALUE;	// maximales Gewicht
	private ArrayList<Edge>[]	adjList;					// Adijazens Liste
	private int					v;
	// private HashSet<Set<Edge>> n;
	private int					anz	= 0;
	
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
	public KMST_1(Integer numNodes, Integer numEdges, HashSet<Edge> edges, int k) {
	
		this.k = k;
		this.adjList = new ArrayList[numNodes];
		this.v = numNodes;
		for (Edge ed : edges) {
			if (this.adjList[ed.node1] == null) this.adjList[ed.node1] = new ArrayList<Edge>();
			this.adjList[ed.node1].add(ed);
			if (this.adjList[ed.node2] == null) this.adjList[ed.node2] = new ArrayList<Edge>();
			this.adjList[ed.node2].add(ed);
		}
		// this.n = new HashSet<Set<Edge>>();
	}
	
	@SuppressWarnings("unchecked")
	private void baum(BitSet knoten, int gewicht, int tiefe, Set<Edge> se) {
	
		this.anz++;
		// if (!se.isEmpty() && n.contains(se)) return;
		// n.add(se);
		if (tiefe >= this.k) {
			if (this.max > gewicht) {
				this.max = gewicht;
				this.setSolution(gewicht, se);
			}
			return;
			
		}
		
		PriorityQueue<Edge> kanten = new PriorityQueue<Edge>();
		for (int b = 0; b < this.v; b++) {
			if (knoten.get(b)) kanten.addAll(this.adjList[b]);
			
		}
		
		if ((kanten.peek().weight + gewicht) >= this.max) return;
		
		Set<Edge> ka;
		for (Edge e : kanten) {
			boolean n1 = knoten.get(e.node1), n2 = knoten.get(e.node2);
			if ((n1 ^ n2) && ((e.weight + gewicht) < this.max)) {
				(ka = new HashSet<Edge>(se)).add(e);
				BitSet al;
				if (!n1) {
					(al = (BitSet) knoten.clone()).set(e.node1);
					this.baum(al, gewicht + e.weight, tiefe + 1, ka);
				}
				if (!n2) {
					(al = (BitSet) knoten.clone()).set(e.node2);
					this.baum(al, gewicht + e.weight, tiefe + 1, ka);
					
				}
				
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void prim(ArrayList<Edge> al) {
	
		Edge es = (new PriorityQueue<Edge>(al)).peek();
		Set<Edge> se = new HashSet<Edge>();
		BitSet knoten = new BitSet(this.v);
		int gewicht = es.weight;
		if (gewicht >= this.max) {
			return;
		}
		knoten.set(es.node1);
		knoten.set(es.node2);
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
		pq.addAll(this.adjList[es.node1]);
		pq.addAll(this.adjList[es.node2]);
		se.add(es);
		pq.remove(es);
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
		// n.add(se);
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
	
		for (ArrayList<Edge> element : this.adjList) {
			this.prim(element);
		}
		
		for (int i = 0; i < this.v; i++) {
			BitSet al = new BitSet();
			al.set(i);
			this.baum(al, 0, 1, new HashSet<Edge>());
		}
		
		System.out.println("Anzahl: " + this.anz);
	}
}
