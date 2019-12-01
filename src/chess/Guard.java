package chess;

import data.ChessBoard;
import data.Position;

//ʿ
public class Guard extends Chess {

	private Position[] correctMove = { // �ܹ��ƶ��ķ���
			new Position(1, 1), new Position(-1, 1), new Position(1, -1), new Position(-1, -1) };

	public Guard(int owner, int type, Position pos) {
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

}
