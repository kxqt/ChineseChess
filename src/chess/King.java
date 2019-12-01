package chess;

import constants.ChessConstants;
import data.ChessBoard;
import data.Position;

//帅
public class King extends Chess {

	private Position[] correctMove = { // 能移动的方向
			new Position(0, 1), new Position(0, -1), new Position(1, 0), new Position(-1, 0) };

	public King(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfPalace(to, this.getOwner()) || Position.isEqual(this.getPos(), to)) {
			// 如果目标位置超出九宫格，或目标位置与当前位置相同
			return false;
		}
		for (Position pos : correctMove) {
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos)) {
				// 按规则能移动到指定位置
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isEatable(ChessBoard board, Chess food) {
		return this.getOwner() != food.getOwner()
				&& (this.isMoveable(board, food.getPos()) || (food.getType() == ChessConstants.KING
						&& board.getNumOfChessOnLine(this.getPos(), food.getPos()) == 0)); // 飞将过河
	}

}
