package chess;

import data.ChessBoard;
import data.Position;

//士
public class Guard extends Chess {

	private Position[] correctMove = { // 能够移动的方向
			new Position(1, 1), new Position(-1, 1), new Position(1, -1), new Position(-1, -1) };

	public Guard(int owner, int type, Position pos) {
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

}
