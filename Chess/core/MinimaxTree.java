package Chess.core;

import Source.*;
import Source.Position;

import java.util.HashSet;

public class MinimaxTree extends AbstractTree<Integer> {
    private static class Node implements Position<Integer> {
        private Integer score;
        private Node parent;
        private HashSet<Node> children = new HashSet<>();

        public Node(Integer score, Node above) {
            this.score = score;
            parent = above;
        }
        public Integer getElement() { return score; }
        public Node getParent() { return parent; }
        public HashSet<Node> getChildren() { return children; }
        public void setElement(Integer score) { this.score = score; }
        public void setParent(Node parentNode) { parent = parentNode; }
        public void addChild(Node child) {children.add(child);}
    }
    private Node createNode(Integer score, Node parent) {
        return new Node(score, parent);
    }

    private Node root = null;
    private int size = 0;
    public MinimaxTree() { }
    private Node validate(Position<Integer> p) throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node node = (Node) p;
        if (node.getParent() == node)
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public Position<Integer> root() {
        return root;
    }
    @Override
    public Position<Integer> parent(Position<Integer> p) throws IllegalArgumentException {
        Node node = validate(p);
        return node.getParent();
    }
    @Override
    public Iterable<Position<Integer>> children(Position<Integer> p) {
        Node parent = validate(p);
        Iterable<? extends Position<Integer>> list = parent.getChildren();
        return (Iterable<Position<Integer>>) list;
    }

    @Override
    public int numChildren(Position<Integer> p) throws IllegalArgumentException {
        Node parent = validate(p);
        return parent.getChildren().size();
    }


    public Position<Integer> addRoot(Integer score) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = createNode(score, null);
        size = 1;
        return root;
    }
    public Position<Integer> addChild(Position<Integer> p,Integer score)
            throws IllegalArgumentException {
        Node parent = validate(p);
        Node child = createNode(score, parent);
        parent.addChild(child);
        size++;
        return child;
    }
    public Integer set(Position<Integer> p, int score) throws IllegalArgumentException {
        Node node = validate(p);
        Integer temp = node.getElement();
        node.setElement(score);
        return temp;
    }
    public void attach(Position<Integer> p, MinimaxTree tree) throws IllegalArgumentException {
        Node node = validate(p);
        if (isInternal(p)) throw new IllegalArgumentException("p must be a leaf");
        size += tree.size();
        if (!tree.isEmpty()) {
            tree.root.setParent(node);
            node.addChild(tree.root);
            tree.root = null;
            tree.size = 0;
        }
    }

    public Integer remove(Position<Integer> p) throws IllegalArgumentException {
        Node node = validate(p);
        if (!isExternal(p))
            throw new IllegalArgumentException("p is not external");
        size--;
        if(!isRoot(p)){
            node.getParent().getChildren().remove(node);
        }
        Integer temp = node.getElement();
        node.setElement(null);
        node.setParent(node);
        node.children = null;
        return temp;
    }
}