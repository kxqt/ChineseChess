package chess;

import data.ChessBoard;
import data.Position;

// 卒
public class Pawn extends Chess {

	public Pawn(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfBound(to) || Position.isEqual(this.getPos(), to)) {
			// 如果目标位置超出棋盘，或目标位置与当前位置相同
			return false;
		}

		Position diff = Position.subtract(to, super.getPos());
		if (super.getOwner() == board.getTerritoryOwner(super.getPos())) { // 未过河
			return Position.isEqual(diff, super.getFowardDirection());
		} else {
			return Position.isEqual(diff, super.getFowardDirection()) || // 前进
					Position.isEqual(diff, new Position(1, 0)) || // 右移
					Position.isEqual(diff, new Position(-1, 0)); // 左移
		}
	}

}
