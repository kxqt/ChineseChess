package chess;

import constants.ChessBoardConstants;
import data.ChessBoard;
import data.Position;

public abstract class Chess {
	private int owner; // �������������
	private int type; // ���ӵ�����
	private Position pos; // �������ڵ�λ��

	public Chess(int owner, int type, Position pos) {
		super();
		this.owner = owner;
		this.type = type;
		this.pos = pos;
	}

	public int getOwner() {
		return owner;
	}

	public int getType() {
		return type;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	// �õ�����ǰ���ķ���
	protected Position getFowardDirection() {
		if (owner == ChessBoardConstants.PLAYER1) { // ����Ǻ췽
			return new Position(0, -1);
		} else { // ����Ǻڷ�
			return new Position(0, 1);
		}
	}

	// �ж������Ƿ��ܹ��ƶ���ĳ��λ��
	protected abstract boolean isMoveable(ChessBoard board, Position to);

	// �ƶ����Ӳ����������Ƿ��ƶ��ɹ�
	public boolean move(ChessBoard board, Position to) {
		if (this.isMoveable(board, to)) { // ��������ܹ��ƶ�
			this.setPos(to);
			return true;
		}
		return false;
	}

	// �ж������Ƿ��ܹ��Ե���һ�����ӣ��ᱻ��д��
	protected boolean isEatable(ChessBoard board, Chess food) {
		return this.getOwner() != food.getOwner() && this.isMoveable(board, food.getPos()); // ����������Ӳ���ͬһ���ұ������ܹ��ƶ��������������ڵ�λ��
	}

	// ������
	public boolean eat(ChessBoard board, Chess food) { // δ��ȥfood
		if (this.isEatable(board, food)) { // ��������ܳ�
			this.setPos(food.getPos()); // �ƶ�����
			return true;
		}
		return false;
	}
}
