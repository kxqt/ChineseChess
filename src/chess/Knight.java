package chess;

import java.util.HashMap;

import data.ChessBoard;
import data.Position;

//马
public class Knight extends Chess {

	private HashMap<Position, Integer> correctMove; // 要移动的位置和别马脚的位置的映射
	{
		correctMove = new HashMap<>();
		correctMove.put(new Position(1, -2), 1);
		correctMove.put(new Position(2, -1), 5);
		correctMove.put(new Position(1, 2), 7);
		correctMove.put(new Position(2, 1), 5);
		correctMove.put(new Position(-1, -2), 1);
		correctMove.put(new Position(-2, -1), 3);
		correctMove.put(new Position(-1, 2), 7);
		correctMove.put(new Position(-2, 1), 3);
	}

	public Knight(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfBound(to) || Position.isEqual(this.getPos(), to)) {
			// 如果目标位置超出棋盘，或目标位置与当前位置相同
			return false;
		}
		for (Position pos : correctMove.keySet()) {
			int tmp = correctMove.get(pos);
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos)
					&& board.getNeighbours(this.getPos())[tmp / 3][tmp % 3] == 0) {
				// 按规则能移动到指定位置并且不别马脚
				return true;
			}
		}
		return false;
	}

}