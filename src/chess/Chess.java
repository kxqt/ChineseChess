package chess;

import constants.ChessBoardConstants;
import data.ChessBoard;
import data.Position;

public abstract class Chess {
	private int owner; // 棋子所属的玩家
	private int type; // 棋子的类型
	private Position pos; // 棋子所在的位置

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

	// 得到棋子前进的方向
	protected Position getFowardDirection() {
		if (owner == ChessBoardConstants.PLAYER1) { // 如果是红方
			return new Position(0, -1);
		} else { // 如果是黑方
			return new Position(0, 1);
		}
	}

	// 判断棋子是否能够移动到某个位置
	protected abstract boolean isMoveable(ChessBoard board, Position to);

	// 移动棋子并返回棋子是否移动成功
	public boolean move(ChessBoard board, Position to) {
		if (this.isMoveable(board, to)) { // 如果棋子能够移动
			this.setPos(to);
			return true;
		}
		return false;
	}

	// 判断棋子是否能够吃到另一颗棋子（会被重写）
	protected boolean isEatable(ChessBoard board, Chess food) {
		return this.getOwner() != food.getOwner() && this.isMoveable(board, food.getPos()); // 如果两个棋子不是同一方且本棋子能够移动到被吃棋子所在的位置
	}

	// 吃棋子
	public boolean eat(ChessBoard board, Chess food) { // 未移去food
		if (this.isEatable(board, food)) { // 如果棋子能吃
			this.setPos(food.getPos()); // 移动棋子
			return true;
		}
		return false;
	}
}
