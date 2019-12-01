package data;

public interface ChessBoard {
	public abstract int getNumOfChessOnLine(Position from, Position to);

	public abstract int[][] getNeighbours(Position pos);
	
	public abstract int getTerritoryOwner(Position pos);

	public abstract boolean isOutOfBound(Position pos);
	
	public abstract boolean isOutOfPalace(Position pos, int player);
}
