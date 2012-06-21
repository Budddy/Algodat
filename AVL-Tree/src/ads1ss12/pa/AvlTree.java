
package ads1ss12.pa;

/**
 * AVL-Baum-Klasse die die fehlenden Methoden aus {@link AbstractAvlTree} implementiert.
 * 
 * <p>
 * In dieser Klasse m&uuml;ssen Sie Ihren Code einf&uuml;gen und die Methoden {@link #remove remove()} sowie
 * {@link #rotateLeft rotateLeft()} und {@link #rotateRight rotateRight()} implementieren.
 * </p>
 * 
 * <p>
 * Sie k&ouml;nnen beliebige neue Variablen und Methoden in dieser Klasse hinzuf&uuml;gen. Wichtig ist nur, dass die
 * oben genannten Methoden implementiert werden.
 * </p>
 */
public class AvlTree extends AbstractAvlTree {
	
	/**
	 * Der Default-Konstruktor.
	 * 
	 * Ruft einfach nur den Konstruktor der Oberklasse auf.
	 */
	public AvlTree() {
	
		super();
	}
	
	public int balance(AvlNode n) {
	
		return n == null ? 0 : (this.height(n.right) - this.height(n.left));
	}
	
	public int height(AvlNode n) {
	
		return n == null ? 0 : Math.max(this.height(n.left), this.height(n.right)) + 1;
	}
	
	/**
	 * F&uuml;gt ein Element mit dem Schl&uuml;ssel <code>k</code> ein.
	 * 
	 * <p>
	 * Existiert im AVL-Baum ein Knoten mit Schl&uuml;ssel <code>k</code>, soll <code>insert()</code> einfach nichts
	 * machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Einf&uuml;gen muss sichergestellt sein, dass es sich bei dem resultierenden Baum immer noch um einen
	 * AVL-Baum handelt, und dass {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel der eingef&uuml;gt werden soll.
	 * @see AbstractAvlTree#insert
	 */
	@Override
	public void insert(int k) {
	
		if (this.root == null) this.root = new AvlNode(k);
		
		AvlNode n = this.search(this.root, k);
		if (n.key == k) return;
		
		if (n.key > k) (n.left = new AvlNode(k)).parent = n;
		if (n.key < k) (n.right = new AvlNode(k)).parent = n;
		this.rebalance(n);
		
	}
	
	public AvlNode min(AvlNode n) {
	
		return n.left == null ? n : this.min(n.left);
	}
	
	public void rebalance(AvlNode n) {
	
		if (n == null) return;
		
		if ((this.height(n.right) - this.height(n.left)) == 2) {
			if (this.balance(n.right) >= 0) {
				this.rotateLeft(n);
			}
			else {
				this.rotateRight(n.right);
				this.rotateLeft(n);
			}
		}
		
		if ((this.height(n.right) - this.height(n.left)) == -2) {
			if (this.balance(n.left) <= 0) {
				this.rotateRight(n);
			}
			else {
				this.rotateLeft(n.left);
				this.rotateRight(n);
			}
		}
		
		if (n != null) this.rebalance(n.parent);
	}
	
	/**
	 * Entfernt den Knoten mit Schl&uuml;ssel <code>k</code> falls er existiert.
	 * 
	 * <p>
	 * Existiert im AVL-Baum kein Knoten mit Schl&uuml;ssel <code>k</code>, soll <code>remove()</code> einfach nichts
	 * machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Entfernen muss sichergestellt sein, dass es sich bei dem resultierenden Baum immer noch um einen
	 * AVL-Baum handelt, und dass {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel dessen Knoten entfernt werden soll.
	 * 
	 * @see AbstractAvlTree#root
	 * @see #rotateLeft rotateLeft()
	 * @see #rotateRight rotateRight()
	 */
	@Override
	public void remove(int k) {
	
		AvlNode n = this.search(this.root, k);
		
		if (n == null) return;
		if (n.key != k) return;
		
		if ((n.right == null) && (n.left != null)) {
			n.left.parent = n.parent;
			if (n.parent == null) this.root = n.left.parent;
			else {
				if (n.parent.left == n) n.parent.left = n.left;
				else n.parent.right = n.left;
			}
			this.rebalance(n.parent);
		}
		
		if ((n.left == null) && (n.right != null)) {
			n.right.parent = n.parent;
			if (n.parent == null) this.root = n.right.parent;
			else {
				if (n.parent.left == n) n.parent.left = n.right;
				else n.parent.right = n.right;
			}
			this.rebalance(n.parent);
		}
		
		if ((n.left == null) && (n.right == null)) {
			if (n.parent != null) {
				if (n.parent.right == n) n.parent.right = null;
				else n.parent.left = null;
				this.rebalance(n.parent);
			}
			else this.root = null;
		}
		
		if ((n.left != null) && (n.right != null)) {
			AvlNode m = this.min(n.right);
			
			this.remove(m.key);
			
			m.parent = n.parent;
			
			if (m.parent != null) {
				if (m.parent.right == n) m.parent.right = m;
				else m.parent.left = m;
			}
			else this.root = m;
			
			m.right = n.right;
			m.left = n.left;
			
			if (m.right != null) m.right.parent = m;
			if (m.left != null) m.left.parent = m;
			
			this.rebalance(m.right);
			this.rebalance(m.left);
		}
	}
	
	/**
	 * F&uuml;hrt eine Links-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param u
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	@Override
	public AvlNode rotateLeft(AvlNode u) {
	
		if (u.right == null) return null;
		AvlNode v = u.right;
		v.parent = u.parent;
		
		if (u.parent == null) {
			this.root = v;
		}
		else {
			if ((u.parent.right != null) && (u.parent.right == u)) u.parent.right = v;
			else u.parent.left = v;
		}
		
		u.right = v.left;
		if (u.right != null) u.right.parent = u;
		v.left = u;
		u.parent = v;
		this.root.parent = null;
		
		return v;
	}
	
	/**
	 * F&uuml;hrt eine Rechts-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param u
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	@Override
	public AvlNode rotateRight(AvlNode u) {
	
		if (u.left == null) return null;
		AvlNode v = u.left;
		v.parent = u.parent;
		
		if (u.parent == null) {
			this.root = v;
		}
		else {
			if ((u.parent.left != null) && (u.parent.left == u)) u.parent.left = v;
			else u.parent.right = v;
		}
		
		u.left = v.right;
		if (u.left != null) u.left.parent = u;
		v.right = u;
		u.parent = v;
		this.root.parent = null;
		
		return v;
	}
	
	public AvlNode search(AvlNode n, int k) {
	
		if (n == null) return n;
		if (n.key == k) return n;
		if ((n.key < k) && (n.right != null)) return this.search(n.right, k);
		if ((n.key < k) && (n.right == null)) return n;
		if ((n.key > k) && (n.left != null)) return this.search(n.left, k);
		if ((n.key > k) && (n.left == null)) return n;
		// return n==null ? n : n.key==k ? n : n.key < k && n.right != null ? search(n.right, k) : n.key < k && n.right
		// == null ? n : n.key > k && n.left != null ? search(n.left, k) : n.key > k && n.left == null ? n:n;
		return n;
	}
	
}
