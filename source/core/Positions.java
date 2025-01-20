package source.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Positions implements Iterable<Position>, Cloneable{
    private class PositionIterator implements Iterator<Position> {
        private final Positions snapshot = new Positions(board);
        private Position cursor = snapshot.first();
        private Position recent = null;
        public boolean hasNext() { return (cursor != null);}
        public Position next() throws NoSuchElementException {
            if (cursor == null) throw new NoSuchElementException("nothing left");
            recent = cursor;
            snapshot.remove(cursor);
            cursor = snapshot.first();
            return recent;
        }
        public void remove() throws IllegalStateException {
            if (recent == null) throw new IllegalStateException("nothing to remove");
            Positions.this.remove(recent);
            recent = null;
        }
    }
    private long board;
    Positions(){
        board = 0;
    }
    Positions(long positions){
        board = positions;
    }
    public boolean contains(Position p){
        return (board & p.getPosition()) != 0;
    }
    public void add(Position... p){
        for (Position position : p) {
            add(position);
        }
    }
    public boolean isEmpty(){
        return board == 0;
    }
    public void add(Position p){
        board |= p.getPosition();
    }
    public void add(Positions p){
        board |= p.board;
    }
    public void add(int x, int y) {board |= 1L << (y * 8 + x);}
    public void remove(Position p){
        board &= ~p.getPosition();
    }
    public void remove(Positions p){
        board &= ~p.board;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        if(board == 0){
            sb.append("]");
            return sb.toString();
        }
        for (Position p : this) sb.append(p.toString()).append(", ");
        sb.replace(sb.length() - 2, sb.length(), "]");
        return sb.toString();
    }
    public int size(){
        return Long.bitCount(board);
    }
    public Position first(){
        if(board == 0) return null;
        int a = Long.numberOfTrailingZeros(board);
        return new Position(a%8, a/8);
    }
    @Override
    public Iterator<Position> iterator() {
        return new PositionIterator();
    }
    @Override
    public Positions clone() {
        try {
            return (Positions) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
