package data;

import java.util.HashMap;

import constants.ChessBoardConstants;
import constants.ChessConstants;
import ui.ChessLabel;

public class LogSentence {
	private ChessLabel moveChess; // �ƶ�������
	private Position from; // ��ʼλ��
	private Position to; // ����λ��
	private ChessLabel food;// ���Ե�����

	HashMap<Integer, String> chessname = new HashMap<>(); // ���ӳ����뺺�ֵ�ӳ��
	{
		chessname.put(ChessConstants.KING, "��˧");
		chessname.put(ChessConstants.GUARD, "ʿ��");
		chessname.put(ChessConstants.BISHOP, "����");
		chessname.put(ChessConstants.ROOK, "����");
		chessname.put(ChessConstants.KNIGHT, "����");
		chessname.put(ChessConstants.CONNON, "����");
		chessname.put(ChessConstants.PAWN, "���");
	}
	HashMap<Integer, String> number = new HashMap<>();// ���ֳ����뺺�ֵ�ӳ��
	{
		number.put(1, "һ");
		number.put(2, "��");
		number.put(3, "��");
		number.put(4, "��");
		number.put(5, "��");
		number.put(6, "��");
		number.put(7, "��");
		number.put(8, "��");
		number.put(9, "��");
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

	// �������¼ת��Ϊ�ַ���
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

	// �������¼ת��Ϊ����
	public String toStandardString() { // δ�����ֶ�����һ���ߵ�����
		StringBuffer str = new StringBuffer();
		int player = moveChess.getChess().getOwner();
		int chessType = moveChess.getChess().getType();
		if (player == ChessBoardConstants.PLAYER1) {
			str.append(chessname.get(chessType).charAt(1));
			str.append(number.get(ChessConstants.MAX_X - from.getX()));
			if (to.getY() < from.getY()) {
				str.append('��');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(number.get(from.getY() - to.getY()));
				} else {
					str.append(number.get(ChessConstants.MAX_X - to.getX()));
				}
			} else if (to.getY() > from.getY()) {
				str.append('��');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(number.get(to.getY() - from.getY()));
				} else {
					str.append(number.get(ChessConstants.MAX_X - to.getX()));
				}
			} else {
				str.append('ƽ');
				str.append(number.get(ChessConstants.MAX_X - to.getX()));
			}
		} else if (player == ChessBoardConstants.PLAYER2) {
			str.append(chessname.get(chessType).charAt(0));
			str.append(1 + from.getX());
			if (to.getY() < from.getY()) {
				str.append('��');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(from.getY() - to.getY());
				} else {
					str.append(1 + to.getX());
				}
			} else if (to.getY() > from.getY()) {
				str.append('��');
				if (chessType != ChessConstants.KNIGHT && chessType != ChessConstants.GUARD
						&& chessType != ChessConstants.BISHOP) {
					str.append(to.getY() - from.getY());
				} else {
					str.append(1 + to.getX());
				}
			} else {
				str.append('ƽ');
				str.append(1 + to.getX());
			}
		}
		return str.toString();
	}

}
