package chess;

import constants.ChessConstants;
import data.ChessBoard;
import data.Position;

//˧
public class King extends Chess {

	private Position[] correctMove = { // ���ƶ��ķ���
			new Position(0, 1), new Position(0, -1), new Position(1, 0), new Position(-1, 0) };

	public King(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfPalace(to, this.getOwner()) || Position.isEqual(this.getPos(), to)) {
			// ���Ŀ��λ�ó����Ź��񣬻�Ŀ��λ���뵱ǰλ����ͬ
			return false;
		}
		for (Position pos : correctMove) {
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos)) {
				// ���������ƶ���ָ��λ��
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isEatable(ChessBoard board, Chess food) {
		return this.getOwner() != food.getOwner()
				&& (this.isMoveable(board, food.getPos()) || (food.getType() == ChessConstants.KING
						&& board.getNumOfChessOnLine(this.getPos(), food.getPos()) == 0)); // �ɽ�����
	}

}
