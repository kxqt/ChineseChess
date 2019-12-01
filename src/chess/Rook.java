package chess;

import data.ChessBoard;
import data.Position;

//��
public class Rook extends Chess {

	public Rook(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		// ���δ�������̷�Χ����Ŀ��λ���뵱ǰλ����ͬһֱ�ߣ����м�û�������ڵ�����Ŀ��λ���뵱ǰλ�ò���ͬ
		return !board.isOutOfBound(to) && board.getNumOfChessOnLine(this.getPos(), to) == 0
				&& !Position.isEqual(this.getPos(), to);
	}

}
