package chess;

import java.util.HashMap;

import data.ChessBoard;
import data.Position;

//象
public class Bishop extends Chess {

	private HashMap<Position, Integer> correctMove;		//要移动的位置和塞象眼的位置的映射
	{
		correctMove = new HashMap<>();
		correctMove.put(new Position(2, -2), 2);
		correctMove.put(new Position(2, 2), 8);
		correctMove.put(new Position(-2, -2), 0);
		correctMove.put(new Position(-2, 2), 6);
	}
	
	public Bishop(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfBound(to) || board.getTerritoryOwner(to) != this.getOwner() || Position.isEqual(this.getPos(), to)) {
			 // 如果超出框格，或者目标位置在对方底盘，或者目标位置与当前位置重合
			return false;
		}
		for (Position pos : correctMove.keySet()) {
			int tmp = correctMove.get(pos);
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos) && board.getNeighbours(this.getPos())[tmp / 3][tmp % 3] == 0) {
				//按规则能移动到指定位置并且不被塞象眼
				return true;
			}
		}
		return false;
	}

}
