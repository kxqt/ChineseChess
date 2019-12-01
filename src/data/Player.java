package data;

import java.util.ArrayList;

import chess.Chess;
import constants.ChessConstants;
import ui.ChessLabel;

public class Player {
	private ArrayList<ChessLabel> chesses;
	private GameTime time;

	public Player() {
		chesses = new ArrayList<>();
		time = new GameTime(10); // Ĭ��Ϊ10min
	}
	
	public Player(GameTime time) {
		this.chesses = new ArrayList<>();
		this.time = time;
	}
	
	public Player(ArrayList<ChessLabel> chesses, GameTime time) {
		super();
		this.chesses = chesses;
		this.time = time;
	}
	
	public ArrayList<ChessLabel> getChesses() {
		return chesses;
	}
	
	public GameTime getTime() {
		return time;
	}
	
	// ��������
	public void addChess(ChessLabel chess) {
		chesses.add(chess);
	}
	
	// ��������б�
	public void clearChesses() {
		chesses.clear();
	}
	
	// �ж��Ƿ�����
	public boolean isloser() {
		if (time.getTime() <= 0) {
			return true;
		}
		for (ChessLabel c : chesses) {
			if (c.getChess().getType() == ChessConstants.KING) {
				return !c.isVisible();
			}
		}
		return true;
	}
}
