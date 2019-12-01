package chess;

import java.util.HashMap;

import data.ChessBoard;
import data.Position;

//��
public class Bishop extends Chess {

	private HashMap<Position, Integer> correctMove;		//Ҫ�ƶ���λ�ú������۵�λ�õ�ӳ��
	{
		correctMove = new HashMap<>();
		correctMove.put(new Position(2, -2), 2);
		correctMove.put(new Position(2, 2), 8);
		correctMove.put(new Position(-2, -2), 0);
		correctMove.put(new Position(-2, 2), 6);
	}
	
	public Bishop(int owner, int type, Position pos) {
		super(owner, type, pos);
	}

	@Override
	protected boolean isMoveable(ChessBoard board, Position to) {
		if (board.isOutOfBound(to) || board.getTerritoryOwner(to) != this.getOwner() || Position.isEqual(this.getPos(), to)) {
			 // ���������񣬻���Ŀ��λ���ڶԷ����̣�����Ŀ��λ���뵱ǰλ���غ�
			return false;
		}
		for (Position pos : correctMove.keySet()) {
			int tmp = correctMove.get(pos);
			if (Position.isEqual(Position.subtract(to, this.getPos()), pos) && board.getNeighbours(this.getPos())[tmp / 3][tmp % 3] == 0) {
				//���������ƶ���ָ��λ�ò��Ҳ���������
				return true;
			}
		}
		return false;
	}

}
