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
		time = new GameTime(10); // 默认为10min
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
	
	// 增加棋子
	public void addChess(ChessLabel chess) {
		chesses.add(chess);
	}
	
	// 清空棋子列表
	public void clearChesses() {
		chesses.clear();
	}
	
	// 判断是否输棋
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
