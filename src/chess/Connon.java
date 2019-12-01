package chess;

import data.ChessBoard;
import data.Position;

//炮
public class Connon extends Chess {

	public Connon(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		// 如果未超出棋盘范围，且目标位置与当前位置在同一直线，且中间没有棋子遮挡，且目标位置与当前位置不相同
		return !board.isOutOfBound(to) && board.getNumOfChessOnLine(this.getPos(), to) == 0
				&& !Position.isEqual(this.getPos(), to);
	}

	@Override
	protected boolean isEatable(ChessBoard board, Chess food) {
		// 如果未超出棋盘范围，且目标位置与当前位置在同一直线，且中间恰有一颗棋子作炮台，且目标位置与当前位置不相同
		return this.getOwner() != food.getOwner() && !board.isOutOfBound(food.getPos())
				&& board.getNumOfChessOnLine(this.getPos(), food.getPos()) == 1;
	}
}
