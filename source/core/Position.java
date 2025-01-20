package source.core;

public class Position {
    private long position;

    public Position(){
        position = 0;
    }

    public Position(int x, int y){
        position = 1L << (y * 8 + x);
    }

    public Position(Position that){
        this.position = that.position;
    }
    public Position(long position){
        this.position = position;
    }

    public byte getX() {
        int idx = Long.numberOfTrailingZeros(position);
        return (byte)(idx % 8);
    }
    public byte getY(){
        int idx = Long.numberOfTrailingZeros(position);
        return (byte)(idx / 8);
    }
    public long getPosition(){
        return position;
    }
    public void setX(int x){
        byte y = getY();
        position = 1L << (y * 8 + x);
    }
    public void setY(int y){
        byte x = getX();
        position = 1L << (y * 8 + x);
    }
    public Position shift(int dx, int dy) {
        int newX = getX() + dx;
        if (newX < 0 || newX > 7) return null;

        int newY = getY() + dy;
        if (newY < 0 || newY > 7) return null;

        return new Position(newX, newY);
    }


    public String toString(){
        return "" + (char)(getX() + 'A') + (char)(getY() + '1');
    }
    public static Position generateFromString(String position){
        position = position.toLowerCase();
        int x = (position.charAt(0) - 'a');
        int y = (position.charAt(1) - '0' - 1);
        if(position.length() != 2 || x<0 || x>Chess.COLUMN - 1 || y<0 || y>Chess.ROW - 1){
            return null;
        }
        return new Position(x, y);
    }

    public static Position generateFromRankAndFile(int rank, int file) {
        if (rank >= 0 && rank < Chess.ROW && file >= 0 && file < Chess.COLUMN)
            return new Position(rank, file);
        return null;
    }

    public boolean equals(Object that){
        if (that == null){
            return false;
        }
        else if (that.getClass() != getClass()){
            return false;
        }
        else{
            Position position = (Position)that;
            return (this.getPosition() == position.getPosition());
        }
    }

}