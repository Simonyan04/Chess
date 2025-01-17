package Chess.core;

/**
 * Represents a move in a board game, encapsulating an origin and a destination position.
 * This class is useful for tracking movements of pieces on a board.
 */
public class Move implements Cloneable{
    private Position origin;
    private Position destination;

    /**
     * Default constructor. Initializes a move without specific origin and destination positions.
     */
    public Move(){
    }

    /**
     * Constructor with specific origin and destination positions.
     *
     * @param origin The starting position of the move.
     * @param destination The ending position of the move.
     */
    public Move(Position origin, Position destination){
        this.origin = new Position(origin);
        this.destination = new Position(destination);
    }

    /**
     * Copy constructor. Creates a new am.aua.chess.core.Move object with the same origin and destination as another move.
     *
     * @param that The am.aua.chess.core.Move object to copy.
     */
    public Move(Move that){
        this.origin = new Position(that.origin);
        this.destination = new Position(that.destination);
    }

    /**
     * Returns a new am.aua.chess.core.Position object representing the origin of the move.
     *
     * @return A copy of the origin am.aua.chess.core.Position object.
     */
    public Position getOrigin(){
        return new Position(origin);
    }

    /**
     * Returns a new am.aua.chess.core.Position object representing the destination of the move.
     *
     * @return A copy of the destination am.aua.chess.core.Position object.
     */
    public Position getDestination(){
        return new Position(destination);
    }

    /**
     * Returns a string representation of the move, showing the origin and destination positions.
     *
     * @return The string representation of the move in the format "origin destination".
     */
    public String toString(){
        return origin.toString() + " " + destination.toString();
    }

    @Override
    public Move clone() {
        try {
            Move clone = (Move) super.clone();
            clone.origin = new Position(origin);
            clone.destination = new Position(destination);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
