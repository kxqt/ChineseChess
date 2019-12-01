package chess;

import data.ChessBoard;
import data.Position;

//车
public class Rook extends Chess {

	public Rook(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		// 如果未超出棋盘范围，且目标位置与当前位置在同一直线，且中间没有棋子遮挡，且目标位置与当前位置不相同
		return !board.isOutOfBound(to) && board.getNumOfChessOnLine(this.getPos(), to) == 0
				&& !Position.isEqual(this.getPos(), to);
	}

}
