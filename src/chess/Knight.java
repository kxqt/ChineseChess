package chess;

import java.util.HashMap;

import data.ChessBoard;
import data.Position;

//��
public class Knight extends Chess {

	private HashMap<Position, Integer> correctMove; // Ҫ�ƶ���λ�úͱ���ŵ�λ�õ�ӳ��
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
			// ���Ŀ��λ�ó������̣���Ŀ��λ���뵱ǰλ����ͬ
			return false;
		}
		for (Position pos : correctMove.keySet()) {
			int tmp = correctMove.get(pos);
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos)
					&& board.getNeighbours(this.getPos())[tmp / 3][tmp % 3] == 0) {
				// ���������ƶ���ָ��λ�ò��Ҳ������
				return true;
			}
		}
		return false;
	}

}