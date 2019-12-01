package data;

import java.util.HashMap;

import constants.ChessBoardConstants;
import constants.ChessConstants;
import ui.ChessLabel;

public class LogSentence {
	private ChessLabel moveChess; // 移动的棋子
	private Position from; // 起始位置
	private Position to; // 最终位置
	private ChessLabel food;// 被吃的棋子

	HashMap<Integer, String> chessname = new HashMap<>(); // 棋子常量与汉字的映射
	{
		chessname.put(ChessConstants.KING, "将帅");
		chessname.put(ChessConstants.GUARD, "士仕");
		chessname.put(ChessConstants.BISHOP, "象相");
		chessname.put(ChessConstants.ROOK, "车车");
		chessname.put(ChessConstants.KNIGHT, "马马");
		chessname.put(ChessConstants.CONNON, "炮炮");
		chessname.put(ChessConstants.PAWN, "卒兵");
	}
	HashMap<Integer, String> number = new HashMap<>();// 数字常量与汉字的映射
	{
		number.put(1, "一");
		number.put(2, "二");
		number.put(3, "三");
		number.put(4, "四");
		number.put(5, "五");
		number.put(6, "六");
		number.put(7, "七");
		number.put(8, "八");
		number.put(9, "九");
	}

	public LogSentence(ChessLabel moveChess, Position from, Position to, ChessLabel food) {
		super();
		this.moveChess = moveChess;
		this.from = from;
		this.to = to;
		this.food = food;
	}

	public ChessLabel getMoveChess() {
		return moveChess;
	}

	public Position getFrom() {
		return from;
	}

	public Position getTo() {
		return to;
	}

	public ChessLabel getFood() {
		return food;
	}

	// 将走棋记录转化为字符串
	@Override
	public String toString() {
		if (food == null) {
			return String.format("%d,%d,(%d,%d),(%d,%d),%d", moveChess.getChess().getType(),
					moveChess.getChess().getOwner(), from.getX(), from.getY(), to.getX(), to.getY(),
					ChessConstants.NULL);
		} else {
			return String.format("%d,%d,(%d,%d),(%d,%d),%d", moveChess.getChess().getType(),
					moveChess.getChess().getOwner(), from.getX(), from.getY(), to.getX(), to.getY(),
					food.getChess().getType());
		}
	}

	// 将走棋记录转化为棋谱
	public String toStandardString() { // 未能区分多子在一条线的问题
		StringBuffer str = new StringBuffer();
		int player = moveChess.getChess().getOwner();
		int chessType = moveChess.getChess().getType();
		if (player == ChessBoardConstants.PLAYER1) {
			str.append(chessname.get(chessType).charAt(1));
			str.append(number.get(ChessConstants.MAX_X - from.getX()));
			if (to.getY() < from.getY()) {
				str.append('进');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(number.get(from.getY() - to.getY()));
				} else {
					str.append(number.get(ChessConstants.MAX_X - to.getX()));
				}
			} else if (to.getY() > from.getY()) {
				str.append('退');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(number.get(to.getY() - from.getY()));
				} else {
					str.append(number.get(ChessConstants.MAX_X - to.getX()));
				}
			} else {
				str.append('平');
				str.append(number.get(ChessConstants.MAX_X - to.getX()));
			}
		} else if (player == ChessBoardConstants.PLAYER2) {
			str.append(chessname.get(chessType).charAt(0));
			str.append(1 + from.getX());
			if (to.getY() < from.getY()) {
				str.append('退');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(from.getY() - to.getY());
				} else {
					str.append(1 + to.getX());
				}
			} else if (to.getY() > from.getY()) {
				str.append('进');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(to.getY() - from.getY());
				} else {
					str.append(1 + to.getX());
				}
			} else {
				str.append('平');
				str.append(1 + to.getX());
			}
		}
		return str.toString();
	}

}
