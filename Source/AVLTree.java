package Source; /** Note that from a design perspective, this class should ideally go into
 the AVLMap class as a nested class. */



/** A specialized version of LinkedBinaryTree for AVL trees. */
public class AVLTree<K,V> extends LinkedBinaryTree<Entry<K,V>> {
  //-------------- nested AVLNode class --------------
  // this extends the inherited LinkedBinaryTree.Node class
  protected static class AVLNode<E> extends Node<E> {
    int height=0;
    AVLNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
      super(e, parent, leftChild, rightChild);
    }
    public int getHeight( ) { return height; }
    public void setHeight(int value) { height = value; }
  } //--------- end of nested AVLNode class ---------

  // positional-based methods related to height field
  public int getHeight(Position<Entry<K,V>> p) {
    return ((AVLNode<Entry<K,V>>) p).getHeight( );
  }
  public void setHeight(Position<Entry<K,V>> p, int value) {
    ((AVLNode<Entry<K,V>>) p).setHeight(value);
  }
  // Override node factory function to produce an AVLNode (rather than a Node)
  protected Node<Entry<K,V>> createNode(Entry<K,V> e,
									Node<Entry<K,V>> parent,
									Node<Entry<K,V>> left,
									Node<Entry<K,V>> right) {
    return new AVLNode<>(e, parent, left, right);
  }
  /** Relinks a parent node with its oriented child node. */
  private void relink(Node<Entry<K,V>> parent, Node<Entry<K,V>> child,
									boolean makeLeftChild) {
    child.setParent(parent);
    if (makeLeftChild)
      parent.setLeft(child);
    else
      parent.setRight(child);
  }
  /** Rotates Position p above its parent. */
  public void rotate(Position<Entry<K,V>> p) {
    Node<Entry<K,V>> x = validate(p);
    Node<Entry<K,V>> y = x.getParent( );	// we assume this exists
    Node<Entry<K,V>> z = y.getParent( );	// grandparent (possibly null)
    if (z == null) {
      root = x;								// x becomes root of the tree
      x.setParent(null);
    } else
      relink(z, x, y == z.getLeft( ));		// x becomes direct child of z
    // now rotate x and y, including transfer of middle subtree
    if (x == y.getLeft( )) {
      relink(y, x.getRight( ), true);		// x’s right child becomes y’s left
      relink(x, y, false);					// y becomes x’s right child
    } else {
      relink(y, x.getLeft( ), false);		// x’s left child becomes y’s right
      relink(x, y, true);					// y becomes left child of x
    }
  }
  /** Performs a trinode restructuring of Position x with its parent/grandparent. */
  public Position<Entry<K,V>> restructure(Position<Entry<K,V>> x) {
    Position<Entry<K,V>> y = parent(x);
    Position<Entry<K,V>> z = parent(y);
    if ((x == right(y)) == (y == right(z))) { // matching alignments
      rotate(y);							// single rotation (of y)
      return y;								// y is new subtree root
    } else {								// opposite alignments
      rotate(x);							// double rotation (of x)
      rotate(x);
      return x;								// x is new subtree root
    }
  }
}
