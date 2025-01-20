package DataStructures;


import java.util.Comparator;

/** An implementation of a sorted map using an AVL tree. */
public class AVLMap<K,V> extends AbstractSortedMap<K,V> {
  // To represent the underlying tree structure, we use a specialized
  // subclass of the LinkedBinaryTree class that we name AVLTree
  private AVLTree<K,V> tree = new AVLTree<>();

  /** Constructs an empty map using the natural ordering of keys. */
  public AVLMap() {
    super();						// the AbstractSortedMap constructor
    tree.addRoot(null);				// create a sentinel leaf as root
  }
  /** Constructs an empty map using the given comparator to order keys. */
  public AVLMap(Comparator<K> comp) {
    super(comp);					// the AbstractSortedMap constructor
    tree.addRoot(null);				// create a sentinel leaf as root
  }
  /** Returns the number of entries in the map. */
  public int size( ) {
    return (tree.size( ) - 1) / 2;	// only internal nodes have entries
  }
  /** Utility used when inserting a new entry at a leaf of the tree */
  private void expandExternal(Position<Entry<K,V>> p, Entry<K,V> entry) {
    tree.set(p, entry);				// store new entry at p
    tree.addLeft(p, null);			// add new sentinel leaves as children
    tree.addRight(p, null);
  }

  /** Returns the position in p's subtree having the given key (or else the terminal leaf).*/
  private Position<Entry<K,V>> treeSearch(Position<Entry<K,V>> p, K key) {
    if (tree.isExternal(p))
      return p;                          // key not found; return the final leaf
    int comp = compare(key, p.getElement());
    if (comp == 0)
      return p;                          // key found; return its position
    else if (comp < 0)
      return treeSearch(tree.left(p), key);   // search left subtree
    else
      return treeSearch(tree.right(p), key);  // search right subtree
  }
  /** Returns position with the minimal key in the subtree rooted at p. */
  private Position<Entry<K,V>> treeMin(Position<Entry<K,V>> p) {
    Position<Entry<K,V>> walk = p;
    while (tree.isInternal(walk))
      walk = tree.left(walk);
    return tree.parent(walk);			// we want the parent of the leaf
  }
  /** Returns position with the maximum key in the subtree rooted at p. */
  private Position<Entry<K,V>> treeMax(Position<Entry<K,V>> p) {
    Position<Entry<K,V>> walk = p;
    while (tree.isInternal(walk))
      walk = tree.right(walk);
    return tree.parent(walk);			// we want the parent of the leaf
  }

  /** Returns the value associated with the specified key (or else null).*/
  public V get(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isExternal(p)) return null;         // unsuccessful search
    return p.getElement().getValue();       // match found
  }
  /** Associates the given value with the given key, returning any overridden value. */
  public V put(K key, V value) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Entry<K,V> newEntry = new MapEntry<>(key, value);
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isExternal(p)) {		// key is new
      expandExternal(p, newEntry);
      rebalance(p);
      return null;
    } else {						// replacing existing key
      V old = p.getElement().getValue();
      tree.set(p, newEntry);
      return old;
    }
  }
  /** Removes the entry having key k (if any) and returns its associated value. */
  public V remove(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isExternal(p)) {			// key not found
      return null;
    } else {
      V old = p.getElement().getValue();
      if (tree.isInternal(tree.left(p)) && tree.isInternal(tree.right(p))) { // both children are internal
        Position<Entry<K,V>> replacement = treeMax(tree.left(p));
        tree.set(p, replacement.getElement());
        p = replacement;
      } // now p has at most one child that is an internal node
      Position<Entry<K,V>> leaf = (tree.isExternal(tree.left(p)) ? tree.left(p) : tree.right(p));
      Position<Entry<K,V>> sib = tree.sibling(leaf);
      tree.remove(leaf);
      tree.remove(p);				// sib is promoted in p's place
      if (!tree.isRoot(sib))
        rebalance(tree.parent(sib));
      return old;
    }
  }

  // additional behaviors of the SortedMap interface
  /** Returns the entry having least key (or null if map is empty). */
  public Entry<K,V> firstEntry() {
    if (isEmpty()) return null;
    return treeMin(tree.root()).getElement();
  }
  /** Returns the entry having greatest key (or null if map is empty). */
  public Entry<K,V> lastEntry() {
    if (isEmpty()) return null;
    return treeMax(tree.root()).getElement();
  }
  /** Returns the entry with least key greater than or equal to given key (if any). */
  public Entry<K,V> ceilingEntry(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isInternal(p)) return p.getElement();   // exact match
    while (!tree.isRoot(p)) {
      if (p == tree.left(tree.parent(p)))
        return tree.parent(p).getElement();	// parent has next greater key
      else
        p = tree.parent(p);
    }
    return null;							// no such ceiling exists
  }
  /** Returns the entry with greatest key less than or equal to given key (if any). */
  public Entry<K,V> floorEntry(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isInternal(p)) return p.getElement();   // exact match
    while (!tree.isRoot(p)) {
      if (p == tree.right(tree.parent(p)))
        return tree.parent(p).getElement();	// parent has next lesser key
      else
        p = tree.parent(p);
    }
    return null;							// no such floor exists
  }
  /** Returns the entry with greatest key strictly less than given key (if any). */
  public Entry<K,V> lowerEntry(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isInternal(p) && tree.isInternal(tree.left(p)))
      return treeMax(tree.left(p)).getElement();	// this is the predecessor to p
    // otherwise, we had failed search, or match with no left child
    while (!tree.isRoot(p)) {
      if (p == tree.right(tree.parent(p)))
        return tree.parent(p).getElement();	// parent has next lesser key
      else
        p = tree.parent(p);
    }
    return null;							// no such lesser key exists
  }
  /** Returns the entry with least key strictly greater than given key (if any). */
  public Entry<K,V> higherEntry(K key) throws IllegalArgumentException {
    checkKey(key);					// may throw IllegalArgumentException
    Position<Entry<K,V>> p = treeSearch(tree.root(), key);
    if (tree.isInternal(p) && tree.isInternal(tree.right(p)))
      return treeMin(tree.right(p)).getElement();	// this is the successor to p
    // otherwise, we had failed search, or match with no right child
    while (!tree.isRoot(p)) {
      if (p == tree.left(tree.parent(p)))
        return tree.parent(p).getElement();	// parent has next lesser key
      else
        p = tree.parent(p);
    }
    return null;							// no such greater key exists
  }

  // Support for iteration
  /** Returns an iterable collection of all key-value entries of the map.*/
  public Iterable<Entry<K,V>> entrySet() {
    ArrayList<Entry<K,V>> buffer = new ArrayList<>(size());
    for (Position<Entry<K,V>> p : tree.inorder())
      if (tree.isInternal(p)) buffer.add(p.getElement());
    return buffer;
  }
  /** Returns an iterable of entries with keys in range [fromKey, toKey).*/
  public Iterable<Entry<K,V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
    checkKey(fromKey);				// may throw IllegalArgumentException
    checkKey(toKey);				// may throw IllegalArgumentException
    ArrayList<Entry<K,V>> buffer = new ArrayList<>(size());
    if (compare(fromKey, toKey) < 0)		// ensure that fromKey < toKey
      subMapRecurse(fromKey, toKey, tree.root(), buffer);
    return buffer;
  }
  // utility to fill subMap buffer recursively (while maintaining order)
  private void subMapRecurse(K fromKey, K toKey, Position<Entry<K,V>> p,
                              ArrayList<Entry<K,V>> buffer) {
    if (tree.isInternal(p))
      if (compare(p.getElement(), fromKey) < 0)
        // p's key is less than fromKey, so any relevant entries are to the right
        subMapRecurse(fromKey, toKey, tree.right(p), buffer);
      else {
        subMapRecurse(fromKey, toKey, tree.left(p), buffer); // first consider left subtree
        if (compare(p.getElement(), toKey) < 0) {	// p is within range
          buffer.add(p.getElement());	// add it to buffer, and consider
          subMapRecurse(fromKey, toKey, tree.right(p), buffer); // right subtree as well
        }
      }
  }

  /** Recomputes the height of the given position based on its children's heights. */
  private void recomputeHeight(Position<Entry<K,V>> p) {
    tree.setHeight(p, 1 + Math.max(tree.getHeight(tree.left(p)), tree.getHeight(tree.right(p))));
  }
  /** Returns whether a position has balance factor between -1 and 1 inclusive. */
  private boolean isBalanced(Position<Entry<K,V>> p) {
    return Math.abs(tree.getHeight(tree.left(p)) - tree.getHeight(tree.right(p))) <= 1;
  }
  /** Returns a child of p with height no smaller than that of the other child. */
  private Position<Entry<K,V>> tallerChild(Position<Entry<K,V>> p) {
    if (tree.getHeight(tree.left(p)) > tree.getHeight(tree.right(p)))
      return tree.left(p);     // clear winner
    if (tree.getHeight(tree.left(p)) < tree.getHeight(tree.right(p)))
      return tree.right(p);    // clear winner
    // equal height children; break tie while matching parent's orientation
    if (tree.isRoot(p))
      return tree.left(p);		// choice is irrelevant
    if (p == tree.left(tree.parent(p)))
      return tree.left(p);		// return aligned child
    else
      return tree.right(p);
  }
  /**
   * Utility used to rebalance after an insert or removal operation. This
   * traverses the path upward from p, performing a trinode restructuring
   * when imbalance is found, continuing until balance is restored.
   */
  private void rebalance(Position<Entry<K,V>> p) {
    int oldHeight, newHeight;
    do {
      oldHeight = tree.getHeight(p);	// not yet recalculated if internal
      if (!isBalanced(p)) {				// imbalance detected
        // perform trinode restructuring, setting p to resulting root,
        // and recompute new local heights after the restructuring
        p = tree.restructure(tallerChild(tallerChild(p)));
        recomputeHeight(tree.left(p));
        recomputeHeight(tree.right(p));
      }
      recomputeHeight(p);
      newHeight = tree.getHeight(p);
      p = tree.parent(p);
    } while (oldHeight != newHeight && p != null);
  }
}
