package chess;

import data.ChessBoard;
import data.Position;

// ��
public class Pawn extends Chess {

	public Pawn(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfBound(to) || Position.isEqual(this.getPos(), to)) {
			// ���Ŀ��λ�ó������̣���Ŀ��λ���뵱ǰλ����ͬ
			return false;
		}

		Position diff = Position.subtract(to, super.getPos());
		if (super.getOwner() == board.getTerritoryOwner(super.getPos())) { // δ����
			return Position.isEqual(diff, super.getFowardDirection());
		} else {
			return Position.isEqual(diff, super.getFowardDirection()) || // ǰ��
					Position.isEqual(diff, new Position(1, 0)) || // ����
					Position.isEqual(diff, new Position(-1, 0)); // ����
		}
	}

}
